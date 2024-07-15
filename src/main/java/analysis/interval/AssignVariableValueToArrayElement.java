package analysis.interval;

import analysis.common.Program;
import base.interval.Interval;
import funarray.Environment;
import funarray.Expression;
import funarray.Variable;

public record AssignVariableValueToArrayElement(Expression<Interval> arrayIndex,
                                                Variable<Interval> variable) implements Program {

  @Override
  public Environment<Interval, Interval> run(Environment<Interval, Interval> startingState) {
    var value = startingState.getVariable(variable().name()).value();
    var updatedState = startingState.assignArrayElement(arrayIndex, value);
    System.out.printf("A[%s] ← %s\n", arrayIndex, variable);
    updatedState.consolePrintOut();
    return updatedState;
  }
}
