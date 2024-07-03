package program;

import funarray.Environment;
import funarray.Expression;
import funarray.Variable;

public record AssignArrayElementValueToVariable(Expression arrayIndex,
                                                Variable variable) implements Program {

  @Override
  public Environment run(Environment startingState) {
    var arrayElementValue = startingState.getArrayElement(arrayIndex);
    var updatedState = startingState.assignVariable(variable, arrayElementValue);
    System.out.printf("%s ← A[%s]\n", variable, arrayIndex);
    System.out.println(updatedState);
    return updatedState;
  }
}
