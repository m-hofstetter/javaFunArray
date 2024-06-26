package program;

import funarray.Environment;
import program.conditions.Condition;

public record While(Condition condition, Program program) implements Program {

  public static final int FIXPOINT_ANALYSIS_LOOP_HARD_LIMIT = 100;
  public static final int WIDENING_LOOP_HARD_LIMIT = 1000;

  @Override
  public Environment run(Environment startingState) {

    var previousState = startingState;
    var currentState = startingState;

    for (int i = 0; i < FIXPOINT_ANALYSIS_LOOP_HARD_LIMIT; i++) {
      currentState = program.run(currentState);

      if (previousState.equals(currentState)) {
        // fixpoint has been reached
        return condition.satisfyComplement(currentState);
      }
      previousState = currentState;
    }

    previousState = startingState;
    currentState = startingState;
    for (int i = 0; i < WIDENING_LOOP_HARD_LIMIT; i++) {
      currentState = program.run(currentState);
      previousState.widen(currentState);

      if (previousState.equals(currentState)) {
        // fixpoint has been reached
        return condition.satisfyComplement(currentState);
      }
      previousState = currentState;
    }

    throw new RuntimeException("Could not find fixpoint after %d widenings.".formatted(WIDENING_LOOP_HARD_LIMIT));
  }
}
