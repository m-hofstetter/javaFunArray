package funarray;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A segment bound in a {@link FunArray}.
 *
 * @param expressions the expressions contained within.
 */
public record Bound(Set<Expression> expressions) {
  public Bound {
    expressions = Set.copyOf(expressions);
  }

  public Bound(Expression expression) {
    this(Set.of(expression));
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
  public Bound addToVariableInFunArray(VariableReference variable, int value) {
    return new Bound(
            expressions.stream()
                    .map(e -> e.addToVariableInFunArray(variable, value))
                    .collect(Collectors.toSet())
    );
  }

  public Bound insertExpression(VariableReference variable, Expression expression) {
    var modifiedExpressions = expressions.stream()
            .filter(e -> !e.containsVariable(variable))
            .collect(Collectors.toSet());

    modifiedExpressions.stream()
            .filter(e -> e.containsVariable(expression.variable()))
            .findAny()
            .ifPresent(e -> modifiedExpressions.add(
                    new Expression(variable, e.constant() - expression.constant())
            ));

    return new Bound(modifiedExpressions);
  }

  public Bound removeVariableOccurrences(VariableReference variable) {
    var modifiedExpressions = expressions.stream()
            .filter(e -> !e.containsVariable(variable))
            .collect(Collectors.toSet());
    return new Bound(modifiedExpressions);
  }

  public Bound restrictExpressionOccurrences(Set<Expression> expressions) {
    var modifiedExpressions = this.expressions.stream()
            .filter(expressions::contains)
            .collect(Collectors.toSet());
    return new Bound(modifiedExpressions);
  }

  public boolean contains(Expression expression) {
    return expressions().stream().anyMatch(e -> e.equals(expression));
  }

  public boolean expressionIsLessEqualThan(Expression expression) {
    return expressions().stream().anyMatch(e -> e.isLessEqualThan(expression));
  }

  public boolean expressionIsGreaterEqualThan(Expression expression) {
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
  public Bound getComplementBound(Bound other) {
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
  public static Bound join(List<Bound> bounds) {
    return new Bound(
            bounds.stream().flatMap(b -> b.expressions().stream()).collect(Collectors.toSet())
    );
  }

  /**
   * Returns a new bound containing all expressions that are present in both bounds.
   *
   * @param other the other bound.
   * @return the intersection of both bound.
   */
  public Bound intersect(Bound other) {
    var meetExpressionSet = other.expressions.stream()
            .filter(this.expressions::contains)
            .collect(Collectors.toSet());

    return new Bound(meetExpressionSet);
  }

  /**
   * Returns a new bound containing all expressions from this, that are also present in one of the
   * bounds in the specified list.
   *
   * @param list the list of bounds.
   * @return the intersected bound.
   */
  public Bound intersect(List<Bound> list) {
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
