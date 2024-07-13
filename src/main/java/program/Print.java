package program;

import base.interval.Interval;
import funarray.Environment;

public record Print() implements Program {
  @Override
  public Environment<Interval> run(Environment<Interval> startingState) {
    System.out.println(startingState);
    return startingState;
  }
}
