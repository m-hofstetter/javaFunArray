package program.conditions;

import base.infint.InfInt;
import base.interval.Interval;
import funarray.Expression;

public class ArrayElementEqualToConstant extends ArrayElementInequality {
  private final InfInt equalConstant;

  public ArrayElementEqualToConstant(InfInt equalConstant, Expression<Interval> index) {
    this.equalConstant = equalConstant;
    this.index = index;
    this.constraintsIf = Interval.of(equalConstant, equalConstant);
    this.constraintsElse = Interval.unknown();
  }

  @Override
  public String toString() {
    return String.format("A[%s] == %s", index, equalConstant);
  }
}
