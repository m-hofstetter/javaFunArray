package analysis.common.controlstructure;

import analysis.common.Program;
import analysis.common.condition.Condition;
import base.DomainValue;
import funarray.Environment;
import java.util.List;

public record While<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        Condition<ELEMENT, VARIABLE> condition, Program<ELEMENT, VARIABLE> program,
        ELEMENT unreachable) implements Program<ELEMENT, VARIABLE> {

  public static final int WIDENING_LOOP_HARD_LIMIT = 1000;

  public While(Condition<ELEMENT, VARIABLE> condition,
               List<Program<ELEMENT, VARIABLE>> programs,
               ELEMENT unreachable) {
    this(condition, new Block<>(programs), unreachable);
  }

  @Override
  public Environment<ELEMENT, VARIABLE> run(Environment<ELEMENT, VARIABLE> startingState) {

    var previousState = condition.satisfy(startingState);
    var currentState = condition.satisfy(startingState);

    System.out.printf("WHILE %s DO:\n", condition);
    currentState.consolePrintOut();

    for (int i = 0; i < WIDENING_LOOP_HARD_LIMIT; i++) {
      currentState = condition.satisfy(startingState);
      currentState = program.run(currentState);
      currentState = previousState.widen(currentState, unreachable);

      if (previousState.equals(currentState)) {
        // fixpoint has been reached
        System.out.print("END WHILE\n");
        var updatedState = condition.satisfyComplement(currentState);
        updatedState.consolePrintOut();
        return updatedState;
      }
      previousState = currentState;
    }

    throw new RuntimeException("Could not find fixpoint after %d widenings.".formatted(WIDENING_LOOP_HARD_LIMIT));
  }
}
