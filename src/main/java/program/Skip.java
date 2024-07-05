package program;

import funarray.Environment;

public record Skip() implements Program {
  @Override
  public Environment run(Environment startingState) {
    System.out.println("SKIP");
    return startingState;
  }
}
