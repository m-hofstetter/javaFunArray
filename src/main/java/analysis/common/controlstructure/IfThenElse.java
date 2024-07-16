package analysis.common.controlstructure;

import analysis.common.Program;
import analysis.common.condition.Condition;
import base.DomainValue;
import funarray.Environment;
import java.util.List;

public record IfThenElse<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        Condition<ELEMENT, VARIABLE> condition, Program<ELEMENT, VARIABLE> ifProgram,
        Program<ELEMENT, VARIABLE> elseProgram,
        ELEMENT unreachable) implements Program<ELEMENT, VARIABLE> {

  public IfThenElse(
          Condition<ELEMENT, VARIABLE> condition,
          Program<ELEMENT, VARIABLE> ifProgram,
          List<Program<ELEMENT, VARIABLE>> elsePrograms,
          ELEMENT unreachable) {
    this(condition, ifProgram, new Block<>(elsePrograms), unreachable);
  }

  public IfThenElse(
          Condition<ELEMENT, VARIABLE> condition,
          List<Program<ELEMENT, VARIABLE>> ifPrograms,
          Program<ELEMENT, VARIABLE> elseProgram,
          ELEMENT unreachable) {
    this(condition, new Block<>(ifPrograms), elseProgram, unreachable);
  }

  public IfThenElse(
          Condition<ELEMENT, VARIABLE> condition,
          List<Program<ELEMENT, VARIABLE>> ifPrograms,
          List<Program<ELEMENT, VARIABLE>> elsePrograms,
          ELEMENT unreachable) {
    this(condition, new Block<>(ifPrograms), new Block<>(elsePrograms), unreachable);
  }

  @Override
  public Environment<ELEMENT, VARIABLE> run(Environment<ELEMENT, VARIABLE> startingState) {
    var stateIf = ifProgram.run(condition.satisfy(startingState));
    var stateElse = elseProgram.run(condition.satisfyComplement(startingState));
    return stateIf.join(stateElse, unreachable);
  }
}
