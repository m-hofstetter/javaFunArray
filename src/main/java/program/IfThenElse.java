package program;

import funarray.Environment;
import program.conditions.Condition;

public record IfThenElse(Condition condition, Program ifProgram,
                         Program elseProgram) implements Program {
  @Override
  public Environment run(Environment startingState) {
    var stateIf = ifProgram.run(condition.satisfy(startingState));
    var stateElse = elseProgram.run(condition.satisfyComplement(startingState));
    return stateIf.join(stateElse);
  }
}
