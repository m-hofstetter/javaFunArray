package program;

import funarray.Environment;

public record Print() implements Program {
  @Override
  public Environment run(Environment startingState) {
    System.out.println(startingState);
    return startingState;
  }
}
