package program;

import base.infint.InfInt;
import funarray.Environment;
import funarray.Variable;

public record IncrementVariable(Variable variable, InfInt amount) implements Program {

  @Override
  public Environment run(Environment startingState) {
    return startingState.addToVariable(variable, amount);
  }
}
