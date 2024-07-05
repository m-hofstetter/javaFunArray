package program.conditions;

import funarray.Environment;
import funarray.Expression;

public final class ExpressionLessThanExpression extends ExpressionInequality {

  public ExpressionLessThanExpression(Expression left, Expression right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public Environment satisfy(Environment input) {
    var modifiedFunArray = lessThan(left, right, input.funArray());
    return new Environment(modifiedFunArray, input.variables());
  }

  @Override
  public Environment satisfyComplement(Environment input) {
    var modifiedFunArray = lessEqualThan(right, left, input.funArray());
    return new Environment(modifiedFunArray, input.variables());
  }

  @Override
  public String toString() {
    return String.format("%s < %s", left, right);
  }
}
