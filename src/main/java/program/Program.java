package program;

import base.interval.Interval;
import funarray.Environment;

public interface Program {
  Environment<Interval> run(Environment<Interval> startingState);
}
