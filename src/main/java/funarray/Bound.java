package funarray;

import base.DomainValue;
import base.infint.InfInt;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A segment bound in a {@link FunArray}.
 *
 * @param expressions the expressions contained within.
 */
public record Bound<T extends DomainValue<T>>(Set<Expression<T>> expressions) {
  public Bound {
    expressions = Set.copyOf(expressions);
  }

  @SafeVarargs
  public Bound(Expression<T>... expressions) {
    this(Set.of(expressions));
  }

  /**
   * If a variable v changes reversibly to f(v), all occurrences of v in an FunArray have to be
   * replaced by f^-1(v), so that it still represents the Array correctly. See Cousot et al. 2011.
   * For adding to a variable v by an amount i, this means replacing alle occurrences of v by v-1.
   *
   * @param variable the variable v.
   * @param value    the value by which it is being increased.
   * @return the altered bound.
   */
  public Bound<T> addToVariableInFunArray(Variable<T> variable, InfInt value) {
    return new Bound<>(
            expressions.stream()
                    .map(e -> e.addToVariableInFunArray(variable, value))
                    .collect(Collectors.toSet())
    );
  }

  public Bound<T> insertExpression(Variable<T> variable, Expression<T> expression) {
    var modifiedExpressions = expressions.stream()
            .filter(e -> !e.containsVariable(variable))
            .collect(Collectors.toSet());

    modifiedExpressions.stream()
            .filter(e -> e.containsVariable(expression.variable()))
            .findAny()
            .ifPresent(e -> modifiedExpressions.add(new Expression(variable, e.constant().subtract(expression.constant()))));

    return new Bound(modifiedExpressions);
  }

  public Bound<T> removeVariableOccurrences(Variable<T> variable) {
    var modifiedExpressions = expressions.stream()
            .filter(e -> !e.containsVariable(variable))
            .collect(Collectors.toSet());
    return new Bound(modifiedExpressions);
  }

  public boolean contains(Expression<T> expression) {
    return expressions().stream().anyMatch(e -> e.equals(expression));
  }

  public boolean expressionIsLessEqualThan(Expression<T> expression) {
    return expressions().stream().anyMatch(e -> e.isLessEqualThan(expression));
  }

  public boolean expressionIsGreaterEqualThan(Expression<T> expression) {
    return expressions().stream().anyMatch(e -> e.isGreaterEqualThan(expression));
  }

  @Override
  public String toString() {
    return "{%s}".formatted(
            String.join(" ", expressions.stream().map(Expression::toString).sorted().toList()));
  }

  /**
   * Returns a new bound of all expressions from a bound that are not contained in another specified
   * bound.
   *
   * @param other the other bound.
   * @return the complement.
   */
  public Bound<T> getComplementBound(Bound<T> other) {
    return new Bound(
            expressions.stream()
                    .filter(e -> !other.expressions.contains(e))
                    .collect(Collectors.toSet())
    );
  }

  /**
   * Returns a new bound containing all expressions the list of specified bounds.
   *
   * @param bounds the list of bounds.
   * @return the joined bound.
   */
  public static <T extends DomainValue<T>> Bound<T> join(List<Bound<T>> bounds) {
    return new Bound<>(
            bounds.stream().flatMap(b -> b.expressions().stream()).collect(Collectors.toSet())
    );
  }

  /**
   * Returns a new bound containing all expressions that are present in both bounds.
   *
   * @param other the other bound.
   * @return the intersection of both bound.
   */
  public Bound<T> intersect(Bound<T> other) {
    var meetExpressionSet = other.expressions.stream()
            .filter(this.expressions::contains)
            .collect(Collectors.toSet());

    return new Bound<>(meetExpressionSet);
  }

  /**
   * Returns a new bound containing all expressions from this, that are also present in one of the
   * bounds in the specified lsit.
   *
   * @param list the list of bounds.
   * @return the intersected bound.
   */
  public Bound<T> intersect(List<Bound<T>> list) {
    var joinedList = join(list);
    return this.intersect(joinedList);
  }

  public boolean isEmpty() {
    return expressions.isEmpty();
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Bound otherBound) {
      return this.expressions.size() == otherBound.expressions.size()
              && otherBound.expressions.containsAll(this.expressions);
    }
    return false;
  }
}
