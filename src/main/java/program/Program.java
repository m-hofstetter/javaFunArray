package program;

import base.interval.Interval;
import funarray.Environment;

public interface Program {
  Environment<Interval, Interval> run(Environment<Interval, Interval> startingState);
}
