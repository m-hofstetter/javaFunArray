package program;

import funarray.Environment;
import funarray.Expression;

public record AssignArrayElementValueToArrayElement(Expression arrayIndexSource,
                                                    Expression arrayIndexTarget) implements Program {

  @Override
  public Environment run(Environment startingState) {
    var arrayElementValue = startingState.getArrayElement(arrayIndexSource);
    return startingState.assignArrayElement(arrayIndexTarget, arrayElementValue);
  }
}
