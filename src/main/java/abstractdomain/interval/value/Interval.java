package abstractdomain.interval.value;

import abstractdomain.DomainValue;
import base.infint.InfInt;

/**
 * An abstract domain representing integers as intervals.
 */
public abstract sealed class Interval
        implements DomainValue<Interval> permits ReachableInterval, Unreachable {

  public static Interval unreachable() {
    return new Unreachable();
  }

  public static Interval unknown() {
    return new ReachableInterval(InfInt.negInf(), InfInt.posInf());
  }

  public static Interval of(InfInt lowerLimit, InfInt upperLimit) {
    return new ReachableInterval(lowerLimit, upperLimit);
  }

  public static Interval of(InfInt lowerLimit, long upperLimit) {
    return new ReachableInterval(lowerLimit, InfInt.of(upperLimit));
  }

  public static Interval of(long lowerLimit, InfInt upperLimit) {
    return new ReachableInterval(InfInt.of(lowerLimit), upperLimit);
  }

  public static Interval of(long lowerLimit, long upperLimit) {
    return new ReachableInterval(InfInt.of(lowerLimit), InfInt.of(upperLimit));
  }

  public static Interval of(long bothLimits) {
    return new ReachableInterval(InfInt.of(bothLimits), InfInt.of(bothLimits));
  }
}
