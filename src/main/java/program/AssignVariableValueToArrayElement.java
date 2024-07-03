package program;

import funarray.Environment;
import funarray.Expression;
import funarray.Variable;

public record AssignVariableValueToArrayElement(Expression arrayIndex,
                                                Variable variable) implements Program {

  @Override
  public Environment run(Environment startingState) {
    return startingState.assignArrayElement(arrayIndex, variable.value());
  }
}
