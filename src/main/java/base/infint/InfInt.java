package base.infint;

/**
 * A utility class for integers with infinities.
 */
public abstract sealed class InfInt implements Comparable<InfInt>
        permits NegativeInfinity, PositiveInfinity, FiniteInteger {

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
}
