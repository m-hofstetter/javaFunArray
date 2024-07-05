package program;

import funarray.Environment;
import program.conditions.Condition;

public record While(Condition condition, Program program) implements Program {

  public static final int WIDENING_LOOP_HARD_LIMIT = 1000;

  @Override
  public Environment run(Environment startingState) {

    var previousState = condition.satisfy(startingState);
    var currentState = condition.satisfy(startingState);

    System.out.printf("WHILE %s DO:\n", condition);
    System.out.println(currentState.toString());

    for (int i = 0; i < WIDENING_LOOP_HARD_LIMIT; i++) {
      currentState = program.run(currentState);
      currentState = previousState.widen(currentState);

      if (previousState.equals(currentState)) {
        // fixpoint has been reached
        System.out.print("END WHILE\n");
        var updatedState = condition.satisfyComplement(currentState);
        System.out.println(updatedState);
        return updatedState;
      }
      previousState = currentState;
    }

    throw new RuntimeException("Could not find fixpoint after %d widenings.".formatted(WIDENING_LOOP_HARD_LIMIT));
  }
}
