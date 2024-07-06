package program;

import funarray.Environment;
import funarray.Expression;
import funarray.Variable;

public record AssignVariableValueToArrayElement(Expression arrayIndex,
                                                Variable variable) implements Program {

  @Override
  public Environment run(Environment startingState) {
    var updatedState = startingState.assignArrayElement(arrayIndex, variable.value());
    System.out.printf("A[%s] ← %s\n", arrayIndex, variable);
    updatedState.consolePrintOut();
    return updatedState;
  }
}
