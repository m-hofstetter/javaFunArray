package analysis.common;

import analysis.common.condition.Condition;
import base.interval.Interval;
import funarray.Environment;

public record While(Condition condition, Program program) implements Program {

  public static final int WIDENING_LOOP_HARD_LIMIT = 1000;

  @Override
  public Environment<Interval, Interval> run(Environment<Interval, Interval> startingState) {

    var previousState = condition.satisfy(startingState);
    var currentState = condition.satisfy(startingState);

    System.out.printf("WHILE %s DO:\n", condition);
    currentState.consolePrintOut();

    for (int i = 0; i < WIDENING_LOOP_HARD_LIMIT; i++) {
      currentState = condition.satisfy(startingState);
      currentState = program.run(currentState);
      currentState = previousState.widen(currentState, Interval.unreachable());

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
