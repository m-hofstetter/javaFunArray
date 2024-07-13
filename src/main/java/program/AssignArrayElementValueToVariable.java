package program;

import base.interval.Interval;
import funarray.Environment;
import funarray.Expression;
import funarray.Variable;

public record AssignArrayElementValueToVariable(Expression arrayIndex,
                                                Variable variable) implements Program {

  @Override
  public Environment<Interval> run(Environment<Interval> startingState) {
    var arrayElementValue = startingState.getArrayElement(arrayIndex);
    var updatedState = startingState.assignVariable(variable, arrayElementValue);
    System.out.printf("%s ← A[%s]\n", variable, arrayIndex);
    updatedState.consolePrintOut();
    return updatedState;
  }
}
