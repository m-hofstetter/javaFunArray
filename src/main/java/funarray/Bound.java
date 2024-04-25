package funarray;

import base.IntegerWithInfinity;

import java.util.Set;
import java.util.stream.Collectors;

import static base.TriBoolean.TRUE;

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

  public Bound addToVariable(Variable variable, int value) {
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
