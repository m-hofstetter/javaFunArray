package program;

import base.infint.InfInt;
import base.interval.Interval;
import funarray.Environment;
import funarray.Variable;

public record IncrementVariable(Variable variable, InfInt amount) implements Program {

  @Override
  public Environment<Interval> run(Environment<Interval> startingState) {
    var updatedState = startingState.addToVariable(variable, amount);
    System.out.printf("%s ← %s + %s\n", variable, variable, amount);
    updatedState.consolePrintOut();
    return updatedState;
  }
}
