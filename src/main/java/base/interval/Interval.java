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

  /**
   * Joins two intervals so that all values from either are present in the resulting interval.
   *
   * @param other the other interval.
   * @return the joint of both intervals.
   */
  public abstract Interval join(Interval other);

  /**
   * Meets two intervals so that only values present in either are in the resulting interval.
   *
   * @param other the other interval.
   * @return the meet of both intervals.
   */
  public abstract Interval meet(Interval other);

  /**
   * The abstract widening operator. See: Cousot, P., Cousot, R. (1992). Comparing the Galois
   * connection and widening/narrowing approaches to abstract interpretation. In: Bruynooghe, M.,
   * Wirsing, M. (eds) Programming Language Implementation and Logic Programming. PLILP 1992.
   * Lecture Notes in Computer Science, vol 631. Springer, Berlin, Heidelberg. <a
   * href="https://doi.org/10.1007/3-540-55844-6_142">https://doi.org/10.1007/3-540-55844-6_142</a>.
   *
   * @param other the interval to widen this with.
   * @return the widened interval.
   */
  public abstract Interval widen(Interval other);

  /**
   * The abstract narrowing operator. See: Cousot, P., Cousot, R. (1992). Comparing the Galois
   * connection and widening/narrowing approaches to abstract interpretation. In: Bruynooghe, M.,
   * Wirsing, M. (eds) Programming Language Implementation and Logic Programming. PLILP 1992.
   * Lecture Notes in Computer Science, vol 631. Springer, Berlin, Heidelberg. <a
   * href="https://doi.org/10.1007/3-540-55844-6_142">https://doi.org/10.1007/3-540-55844-6_142</a>.
   *
   * @param other the interval to narrow this with.
   * @return the narrowed interval.
   */
  public abstract Interval narrow(Interval other);

  @Override
  public abstract String toString();

  @Override
  public abstract boolean equals(Object other);

  /**
   * The abstract transformation for adding to an interval.
   *
   * @param value the value to be added.
   * @return the transformed interval.
   */
  public abstract Interval add(InfInt value);
}
