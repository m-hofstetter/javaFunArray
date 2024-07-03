package program.conditions;

import base.infint.InfInt;
import base.interval.Interval;
import funarray.Expression;

public class ArrayElementLessEqualThanConstant extends ArrayElementInequality {
  public ArrayElementLessEqualThanConstant(InfInt lessEqualThan, Expression index) {
    this.index = index;
    this.constraintsIf = Interval.of(InfInt.negInf(), lessEqualThan);
    this.constraintsElse = Interval.of(lessEqualThan.add(1), InfInt.posInf());
  }
}
