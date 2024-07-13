package program.conditions;

import base.interval.Interval;
import funarray.Environment;

public interface Condition {
  Environment<Interval> satisfy(Environment<Interval> input);

  Environment<Interval> satisfyComplement(Environment<Interval> input);
}
