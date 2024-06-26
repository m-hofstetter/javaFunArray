package program.conditions;

import base.interval.Interval;
import funarray.Environment;
import funarray.Expression;

public abstract class ArrayElementInequality implements Condition {
  Interval constraintsIf;
  Interval constraintsElse;
  Expression index;

  @Override
  public Environment satisfy(Environment input) {
    var elementValue = input.getArrayElement(index);
    elementValue = constraintsIf.meet(elementValue);
    return input.assignArrayElement(index, elementValue);
  }

  @Override
  public Environment satisfyComplement(Environment input) {
    var elementValue = input.getArrayElement(index);
    elementValue = constraintsElse.meet(elementValue);
    return input.assignArrayElement(index, elementValue);
  }

  @Override
  public boolean isMet(Environment input) {
    return input.equals(satisfy(input));
  }

}
