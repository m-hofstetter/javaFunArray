package program;

import base.interval.Interval;
import funarray.Environment;
import java.util.List;

public record Block(List<Program> statements) implements Program {

  public Block {
    statements = List.copyOf(statements);
  }

  public Block(Program... statements) {
    this(List.of(statements));
  }

  @Override
  public Environment<Interval> run(Environment<Interval> startingState) {
    for (Program s : statements) {
      startingState = s.run(startingState);
    }
    return startingState;
  }
}
