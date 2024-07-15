package analysis.interval.condition;

import analysis.common.condition.Condition;
import base.interval.Interval;
import funarray.Environment;
import funarray.Expression;

public abstract class ArrayElementInequality implements Condition {
  Interval constraintsIf;
  Interval constraintsElse;
  Expression<Interval> index;

  @Override
  public Environment<Interval, Interval> satisfy(Environment<Interval, Interval> input) {
    var elementValue = input.getArrayElement(index);
    elementValue = constraintsIf.meet(elementValue);
    return input.assignArrayElement(index, elementValue);
  }

  @Override
  public Environment<Interval, Interval> satisfyComplement(Environment<Interval, Interval> input) {
    var elementValue = input.getArrayElement(index);
    elementValue = constraintsElse.meet(elementValue);
    return input.assignArrayElement(index, elementValue);
  }

  @Override
  public abstract String toString();
}
