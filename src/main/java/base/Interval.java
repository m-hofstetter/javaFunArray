package base;

import static base.IntegerWithInfinity.NEGATIVE_INFINITY;
import static base.IntegerWithInfinity.POSITIVE_INFINITY;

/**
 * An abstract domain representing integers as intervals.
 *
 * @param lowerLimit the lower limit.
 * @param upperLimit the upper limit.
 */
public record Interval(IntegerWithInfinity lowerLimit, IntegerWithInfinity upperLimit) {

  public static final Interval UNREACHABLE = new Interval(null, null);

  public Interval(int lowerLimit, int upperLimit) {
    this(new IntegerWithInfinity(lowerLimit), new IntegerWithInfinity(upperLimit));
  }

  public Interval(IntegerWithInfinity lowerLimit, int upperLimit) {
    this(lowerLimit, new IntegerWithInfinity(upperLimit));
  }

  public Interval(int lowerLimit, IntegerWithInfinity upperLimit) {
    this(new IntegerWithInfinity(lowerLimit), upperLimit);
  }

  public static Interval getUnknown() {
    return new Interval(NEGATIVE_INFINITY, POSITIVE_INFINITY);
  }

  /**
   * Joins two intervals so that all values from either are present in the resulting interval.
   *
   * @param other the other interval.
   * @return the joint of both intervals.
   */
  public Interval join(Interval other) {

    if (this == UNREACHABLE) {
      return other;
    }
    if (other == UNREACHABLE) {
      return this;
    }

    var lower = IntegerWithInfinity.min(this.lowerLimit(), other.lowerLimit());
    var upper = IntegerWithInfinity.max(this.upperLimit(), other.upperLimit());

    return new Interval(lower, upper);
  }

  /**
   * Meets two intervals so that only values present in either are in the resulting interval.
   *
   * @param other the other interval.
   * @return the meet of both intervals.
   */
  public Interval meet(Interval other) {
    if (this == UNREACHABLE || other == UNREACHABLE) {
      return UNREACHABLE;
    }

    var lower = IntegerWithInfinity.max(this.lowerLimit(), other.lowerLimit());
    var upper = IntegerWithInfinity.min(this.upperLimit(), other.upperLimit());

    if (lower.isGreaterThan(upper)) {
      return UNREACHABLE;
    }

    return new Interval(lower, upper);
  }

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
  public Interval widen(Interval other) {
    if (this == UNREACHABLE) {
      return other;
    }
    if (other == UNREACHABLE) {
      return this;
    }
    var lower = this.lowerLimit();
    var upper = this.upperLimit();

    if (other.lowerLimit().isLessThan(this.lowerLimit())) {
      lower = NEGATIVE_INFINITY;
    }
    if (other.upperLimit().isGreaterThan(this.upperLimit())) {
      upper = POSITIVE_INFINITY;
    }
    return new Interval(lower, upper);
  }

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
  public Interval narrow(Interval other) {
    if (this == UNREACHABLE || other == UNREACHABLE) {
      return UNREACHABLE;
    }

    var lower = this.lowerLimit();
    var upper = this.upperLimit();

    if (this.lowerLimit().equals(NEGATIVE_INFINITY)) {
      lower = other.lowerLimit();
    }
    if (this.upperLimit().equals(POSITIVE_INFINITY)) {
      upper = other.upperLimit();
    }

    return new Interval(lower, upper);
  }

  @Override
  public String toString() {
    if (this == UNREACHABLE) {
      return "⊥";
    }
    return "[%s, %s]".formatted(lowerLimit, upperLimit);
  }

  @Override
  public boolean equals(Object other) {
    if (other.getClass() != Interval.class) {
      return false;
    }
    if (this == UNREACHABLE) {
      return other == UNREACHABLE;
    }
    var otherInterval = (Interval) other;
    return this.lowerLimit().equals(otherInterval.lowerLimit())
        && this.upperLimit().equals(otherInterval.upperLimit());
  }

  /**
   * The abstract transformation for adding to an interval.
   *
   * @param value the value to be added.
   * @return the transformed interval.
   */
  public Interval add(int value) {
    IntegerWithInfinity newLowerLimit;
    if (lowerLimit == NEGATIVE_INFINITY) {
      newLowerLimit = NEGATIVE_INFINITY;
    } else if (lowerLimit == POSITIVE_INFINITY) {
      newLowerLimit = POSITIVE_INFINITY;
    } else {
      newLowerLimit = new IntegerWithInfinity(lowerLimit.value() + value);
    }

    IntegerWithInfinity newUpperLimit;
    if (upperLimit == NEGATIVE_INFINITY) {
      newUpperLimit = NEGATIVE_INFINITY;
    } else if (upperLimit == POSITIVE_INFINITY) {
      newUpperLimit = POSITIVE_INFINITY;
    } else {
      newUpperLimit = new IntegerWithInfinity(upperLimit.value() + value);
    }

    return new Interval(newLowerLimit, newUpperLimit);
  }
}
