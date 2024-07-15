package analysis.common.condition;

import base.interval.Interval;
import funarray.Environment;

public interface Condition {
  Environment<Interval, Interval> satisfy(Environment<Interval, Interval> input);

  Environment<Interval, Interval> satisfyComplement(Environment<Interval, Interval> input);
}
