package program.conditions;

import funarray.Expression;

public final class ExpressionLessEqualThanExpression extends ExpressionInequality {

  public ExpressionLessEqualThanExpression(Expression left, Expression right) {
    this.left = left;
    this.right = right;
    this.inequality = (l, r) -> l <= r;
    this.inverseInequality = (l, r) -> l > r;
  }
}
