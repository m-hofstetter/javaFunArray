package program;

import funarray.Environment;
import funarray.Expression;

public record AssignArrayElementValueToArrayElement(Expression arrayIndexSource,
                                                    Expression arrayIndexTarget) implements Program {

  @Override
  public Environment run(Environment startingState) {
    var arrayElementValue = startingState.getArrayElement(arrayIndexSource);
    var updatedState = startingState.assignArrayElement(arrayIndexTarget, arrayElementValue);
    System.out.printf("A[%s] ← A[%s]\n", arrayIndexTarget, arrayIndexSource);
    System.out.println(updatedState);
    return updatedState;
  }
}
