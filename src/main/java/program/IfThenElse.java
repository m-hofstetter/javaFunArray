package program;

import base.interval.Interval;
import funarray.Environment;
import program.conditions.Condition;

public record IfThenElse(Condition condition, Program ifProgram,
                         Program elseProgram) implements Program {
  @Override
  public Environment<Interval> run(Environment<Interval> startingState) {
    System.out.printf("IF %s THEN DO:\n", condition.toString());

    var satisifiedState = condition.satisfy(startingState);
    satisifiedState.consolePrintOut();
    var stateIf = ifProgram.run(satisifiedState);

    System.out.print("ELSE DO:\n");

    var satisfiedStateComplement = condition.satisfyComplement(startingState);
    satisfiedStateComplement.consolePrintOut();
    var stateElse = elseProgram.run(satisfiedStateComplement);

    System.out.print("END ELSEIF\n");

    var joinedState = stateIf.join(stateElse);
    joinedState.consolePrintOut();
    return joinedState;
  }
}
