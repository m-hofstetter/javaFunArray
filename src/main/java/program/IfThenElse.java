package program;

import funarray.Environment;
import program.conditions.Condition;

public record IfThenElse(Condition condition, Program ifProgram,
                         Program elseProgram) implements Program {
  @Override
  public Environment run(Environment startingState) {
    System.out.printf("IF %s THEN DO:\n", condition.toString());

    var satisifiedState = condition.satisfy(startingState);
    System.out.println(satisifiedState);
    var stateIf = ifProgram.run(satisifiedState);

    System.out.print("ELSE DO:\n");

    var satisfiedStateComplement = condition.satisfyComplement(startingState);
    System.out.println(satisfiedStateComplement);
    var stateElse = elseProgram.run(satisfiedStateComplement);

    System.out.print("END ELSEIF\n");

    var joinedState = stateIf.join(stateElse);
    System.out.println(joinedState);
    return joinedState;
  }
}
