package analysis.common;

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
    System.out.printf("IF %s THEN DO:\n", condition.toString());

    var satisifiedState = condition.satisfy(startingState);
    satisifiedState.consolePrintOut();
    var stateIf = ifProgram.run(satisifiedState);

    System.out.print("ELSE DO:\n");

    var satisfiedStateComplement = condition.satisfyComplement(startingState);
    satisfiedStateComplement.consolePrintOut();
    var stateElse = elseProgram.run(satisfiedStateComplement);

    System.out.print("END ELSEIF\n");

    var joinedState = stateIf.join(stateElse, unreachable);
    joinedState.consolePrintOut();
    return joinedState;
  }
}
