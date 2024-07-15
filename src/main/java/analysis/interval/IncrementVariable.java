package analysis.interval;

import analysis.common.Program;
import base.infint.InfInt;
import base.interval.Interval;
import funarray.Environment;
import funarray.Variable;

public record IncrementVariable(Variable<Interval> variable, InfInt amount) implements Program {

  @Override
  public Environment<Interval, Interval> run(Environment<Interval, Interval> startingState) {
    var updatedState = startingState.addToVariable(variable, amount);
    System.out.printf("%s ← %s + %s\n", variable, variable, amount);
    updatedState.consolePrintOut();
    return updatedState;
  }
}
