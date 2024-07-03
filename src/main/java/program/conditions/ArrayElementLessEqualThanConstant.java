package program.conditions;

import base.infint.InfInt;
import base.interval.Interval;
import funarray.Expression;

public class ArrayElementLessEqualThanConstant extends ArrayElementInequality {
  private final InfInt lessEqualThan;

  public ArrayElementLessEqualThanConstant(InfInt lessEqualThan, Expression index) {
    this.lessEqualThan = lessEqualThan;
    this.index = index;
    this.constraintsIf = Interval.of(InfInt.negInf(), lessEqualThan);
    this.constraintsElse = Interval.of(lessEqualThan.add(1), InfInt.posInf());
  }

  @Override
  public String toString() {
    return String.format("A[%s] <= %s", index, lessEqualThan);
  }
}
