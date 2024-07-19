package base.interval;

import base.DomainValue;
import base.infint.InfInt;

/**
 * An abstract domain representing integers as intervals.
 */
public abstract sealed class Interval implements DomainValue<Interval> permits ReachableInterval, Unreachable {

  public static Interval unreachable() {
    return new Unreachable();
  }

  public static Interval unknown() {
    return new ReachableInterval(InfInt.negInf(), InfInt.posInf());
  }

  public static Interval of(InfInt lowerLimit, InfInt upperLimit) {
    return new ReachableInterval(lowerLimit, upperLimit);
  }

  public static Interval of(InfInt lowerLimit, int upperLimit) {
    return new ReachableInterval(lowerLimit, InfInt.of(upperLimit));
  }

  public static Interval of(int lowerLimit, InfInt upperLimit) {
    return new ReachableInterval(InfInt.of(lowerLimit), upperLimit);
  }

  public static Interval of(int lowerLimit, int upperLimit) {
    return new ReachableInterval(InfInt.of(lowerLimit), InfInt.of(upperLimit));
  }

  public static Interval of(int bothLimits) {
    return new ReachableInterval(InfInt.of(bothLimits), InfInt.of(bothLimits));
  }
}
