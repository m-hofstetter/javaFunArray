package base.infint;

/**
 * A utility class for integers with infinities.
 */
public abstract sealed class InfInt implements Comparable<InfInt>
        permits Infinity, FiniteInteger {

  public static InfInt of(int value) {
    return new FiniteInteger(value);
  }

  public static InfInt posInf() {
    return new PositiveInfinity();
  }

  public static InfInt negInf() {
    return new NegativeInfinity();
  }

  @Override
  public abstract int compareTo(InfInt other);

  /**
   * A utility function returning the minimum of two integer with infinity handling.
   *
   * @param a the first integer
   * @param b the second integer
   * @return the minimum of both integers.
   */
  public static InfInt min(InfInt a, InfInt b) {
    return a.compareTo(b) <= 0 ? a : b;
  }

  /**
   * A utility function returning the maximum of two integer with infinity handling.
   *
   * @param a the first integer
   * @param b the second integer
   * @return the maximum of both integers.
   */
  public static InfInt max(InfInt a, InfInt b) {
    return a.compareTo(b) >= 0 ? a : b;
  }

  public boolean isLessThan(InfInt other) {
    return this.compareTo(other) < 0;
  }

  public boolean isGreaterThan(InfInt other) {
    return this.compareTo(other) > 0;
  }

  @Override
  public abstract String toString();

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

  public abstract InfInt negate();

  @Override
  public abstract boolean equals(Object other);
}
