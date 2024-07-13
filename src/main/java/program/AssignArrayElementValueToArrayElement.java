package program;

import base.interval.Interval;
import funarray.Environment;
import funarray.Expression;

public record AssignArrayElementValueToArrayElement(Expression<Interval> arrayIndexSource,
                                                    Expression<Interval> arrayIndexTarget) implements Program {

  @Override
  public Environment<Interval, Interval> run(Environment<Interval, Interval> startingState) {
    var arrayElementValue = startingState.getArrayElement(arrayIndexSource);
    var updatedState = startingState.assignArrayElement(arrayIndexTarget, arrayElementValue);
    System.out.printf("A[%s] ← A[%s]\n", arrayIndexTarget, arrayIndexSource);
    updatedState.consolePrintOut();
    return updatedState;
  }
}
