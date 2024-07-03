package program;

import base.infint.InfInt;
import funarray.Environment;
import funarray.Variable;

public record IncrementVariable(Variable variable, InfInt amount) implements Program {

  @Override
  public Environment run(Environment startingState) {
    var updatedState = startingState.addToVariable(variable, amount);
    System.out.printf("%s ← %s + %s\n", variable, variable, amount);
    System.out.println(updatedState);
    return updatedState;
  }
}
