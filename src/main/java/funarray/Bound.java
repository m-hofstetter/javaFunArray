package funarray;

import static base.TriBoolean.TRUE;

import base.infint.InfInt;
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

  public Bound(Expression... expressions) {
    this(Set.of(expressions));
  }

  public Bound(InfInt constant) {
    this(new Expression(Variable.ZERO_VALUE, constant));
  }

  public Bound(int constant) {
    this(InfInt.of(constant));
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
  public Bound addToVariableInFunArray(Variable variable, InfInt value) {
    return new Bound(
            expressions.stream()
                    .map(e -> e.addToVariableInFunArray(variable, value))
                    .collect(Collectors.toSet())
    );
  }

  public boolean expressionEquals(Expression expression) {
    return expressions.contains(expression);
  }

  public boolean expressionIsLessEqualThan(Expression expression) {
    return expressions().stream().anyMatch(e -> e.isLessEqualThan(expression) == TRUE);
  }

  public boolean expressionIsGreaterEqualThan(Expression expression) {
    return expressions().stream().anyMatch(e -> e.isGreaterEqualThan(expression) == TRUE);
  }

  public boolean expressionIsGreaterThan(Expression expression) {
    return expressions().stream().anyMatch(e -> e.isGreaterThan(expression) == TRUE);
  }

  @Override
  public String toString() {
    return "{%s}".formatted(
            String.join(" ", expressions.stream().map(Expression::toString).toList()));
  }

  public boolean containsSubset(Bound subSet) {
    return expressions.containsAll(subSet.expressions);
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
   * Returns a new bound containing all expressions from both bounds.
   *
   * @param other the other bound.
   * @return the joined bound.
   */
  public Bound join(Bound other) {
    var joinedExpressionSet = other.expressions.stream()
            .filter(e -> !this.expressions.contains(e))
            .collect(Collectors.toSet());

    joinedExpressionSet.addAll(this.expressions);

    return new Bound(joinedExpressionSet);
  }

  /**
   * Returns a new bound containing all expressions the list of specified bounds.
   *
   * @param bounds the list of bounds.
   * @return the joined bound.
   */
  public static Bound join(List<Bound> bounds) {
    var bound = new Bound(Set.of());
    for (Bound b : bounds) {
      bound = bound.join(b);
    }
    return bound;
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
   * bounds in the specified lsit.
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
