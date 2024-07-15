package analysis.interval.condition;

import analysis.common.condition.Condition;
import base.interval.Interval;
import funarray.Environment;
import funarray.Expression;

public final class ExpressionLessEqualThanExpression implements Condition {

  Expression<Interval> left;
  Expression<Interval> right;

  public ExpressionLessEqualThanExpression(Expression left, Expression right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public Environment<Interval, Interval> satisfy(Environment<Interval, Interval> input) {
    return input.satisfyExpressionLessEqualThan(left, right);
  }

  @Override
  public Environment<Interval, Interval> satisfyComplement(Environment<Interval, Interval> input) {
    return input.satisfyExpressionLessThan(right, left);
  }

  @Override
  public String toString() {
    return String.format("%s <= %s", left, right);
  }
}
