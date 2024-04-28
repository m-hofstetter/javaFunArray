package funarray;

import static base.TriBoolean.TRUE;

import base.IntegerWithInfinity;
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

  static Bound of(Expression... expressions) {
    return new Bound(Set.of(expressions));
  }

  static Bound ofConstant(IntegerWithInfinity constant) {
    return Bound.of(new Expression(Variable.ZERO_VALUE, constant));
  }

  static Bound ofConstant(int constant) {
    return Bound.ofConstant(new IntegerWithInfinity(constant));
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
  public Bound addToVariableInFunArray(Variable variable, int value) {
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

  @Override
  public String toString() {
    return "{%s}".formatted(
            String.join(" ", expressions.stream().map(Expression::toString).toList()));
  }
}
