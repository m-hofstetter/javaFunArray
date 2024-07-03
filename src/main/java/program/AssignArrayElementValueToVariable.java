package program;

import funarray.Environment;
import funarray.Expression;
import funarray.Variable;

public record AssignArrayElementValueToVariable(Expression arrayIndex,
                                                Variable variable) implements Program {

  @Override
  public Environment run(Environment startingState) {
    var arrayElementValue = startingState.getArrayElement(arrayIndex);
    return startingState.assignVariable(variable, arrayElementValue);
  }
}
