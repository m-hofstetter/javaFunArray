package base.infint;

/**
 * A utility class for integers with infinities.
 */
public abstract sealed class InfInt implements Comparable<InfInt>
        permits Infinity, FiniteInteger {

  public static InfInt of(long value) {
    return new FiniteInteger(value);
  }

  public static InfInt posInf() {
    return new PositiveInfinity();
  }

  public static InfInt negInf() {
    return new NegativeInfinity();
  }

  /**
   * A utility function returning the minimum of multiple integers with infinity.
   *
   * @param ints the integers.
   * @return the maximum of both integers.
   */
  public static InfInt min(InfInt... ints) {
    if (ints.length <= 0) {
      throw new IllegalArgumentException("Cannot find minimum for no InfInts provided.");
    }
    var min = ints[0];
    for (int i = 1; i < ints.length; i++) {
      if (ints[i].compareTo(min) <= 0) {
        min = ints[i];
      }
    }
    return min;
  }

  /**
   * A utility function returning the maximum of multiple integers with infinity.
   *
   * @param ints the integers.
   * @return the maximum of both integers.
   */
  public static InfInt max(InfInt... ints) {
    if (ints.length <= 0) {
      throw new IllegalArgumentException("Cannot find maximum for no InfInts provided.");
    }
    var max = ints[0];
    for (int i = 1; i < ints.length; i++) {
      if (ints[i].compareTo(max) >= 0) {
        max = ints[i];
      }
    }
    return max;
  }

  public boolean isLessThan(InfInt other) {
    return this.compareTo(other) < 0;
  }

  public boolean isLessEqualThan(InfInt other) {
    return this.compareTo(other) <= 0;
  }

  public boolean isGreaterThan(InfInt other) {
    return this.compareTo(other) > 0;
  }

  public boolean isGreaterEqualThan(InfInt other) {
    return this.compareTo(other) >= 0;
  }

  public boolean isPosInf() {
    return this instanceof PositiveInfinity;
  }

  public boolean isNegInf() {
    return this instanceof NegativeInfinity;
  }

  public abstract InfInt add(InfInt value);

  public InfInt add(int value) {
    return this.add(InfInt.of(value));
  }

  public InfInt subtract(InfInt value) {
    return add(value.negate());
  }

  public InfInt subtract(long value) {
    return subtract(InfInt.of(value));
  }

  public abstract InfInt multiply(InfInt value);

  public InfInt multiply(long value) {
    return this.multiply(InfInt.of(value));
  }

  public abstract InfInt divide(InfInt value);

  public InfInt divide(long value) {
    return this.divide(InfInt.of(value));
  }

  /*
   * Modulo with negative operands is defined as in the C language:
   * The sign of the divisor is ignored. The operation is conducted as if the dividend was positive
   * and its sign is then applied to the result. This differs from the java implementation where if
   * either operand is negative, the result will also be negative.
   */
  public abstract InfInt modulo(InfInt value);

  public InfInt modulo(long value) {
    return this.modulo(InfInt.of(value));
  }

  public abstract InfInt negate();
}
