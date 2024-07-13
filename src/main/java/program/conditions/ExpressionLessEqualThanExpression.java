package program.conditions;

import base.interval.Interval;
import funarray.Environment;
import funarray.Expression;

public final class ExpressionLessEqualThanExpression extends ExpressionInequality {

  public ExpressionLessEqualThanExpression(Expression left, Expression right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public Environment<Interval, Interval> satisfy(Environment<Interval, Interval> input) {
    var modifiedFunArray = lessEqualThan(left, right, input.funArray());
    return new Environment<>(modifiedFunArray, input.variables());
  }

  @Override
  public Environment<Interval, Interval> satisfyComplement(Environment<Interval, Interval> input) {
    var modifiedFunArray = lessThan(right, left, input.funArray());
    return new Environment<>(modifiedFunArray, input.variables());
  }

  @Override
  public String toString() {
    return String.format("%s <= %s", left, right);
  }
}
