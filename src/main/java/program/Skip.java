package program;

import base.interval.Interval;
import funarray.Environment;

public record Skip() implements Program {
  @Override
  public Environment<Interval, Interval> run(Environment<Interval, Interval> startingState) {
    System.out.println("SKIP");
    return startingState;
  }
}
