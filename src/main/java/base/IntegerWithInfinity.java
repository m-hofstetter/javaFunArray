package base;

/**
 * A utility class for integers with infinities.
 *
 * @param value the value.
 */
public record IntegerWithInfinity(Integer value) implements Comparable<IntegerWithInfinity> {

  public static final IntegerWithInfinity POSITIVE_INFINITY = new IntegerWithInfinity(null);
  public static final IntegerWithInfinity NEGATIVE_INFINITY = new IntegerWithInfinity(null);

  /**
   * A utility function returning the minimum of two integer with infinity handling.
   *
   * @param a the first integer
   * @param b the second integer
   * @return the minimum of both integers.
   */
  public static IntegerWithInfinity min(IntegerWithInfinity a, IntegerWithInfinity b) {
    if (a == NEGATIVE_INFINITY || b == NEGATIVE_INFINITY) {
      return NEGATIVE_INFINITY;
    }
    if (a == POSITIVE_INFINITY) {
      return b;
    }
    if (b == POSITIVE_INFINITY) {
      return a;
    }
    return new IntegerWithInfinity(Integer.min(a.value(), b.value()));
  }

  /**
   * A utility function returning the maximum of two integer with infinity handling.
   *
   * @param a the first integer
   * @param b the second integer
   * @return the maximum of both integers.
   */
  public static IntegerWithInfinity max(IntegerWithInfinity a, IntegerWithInfinity b) {
    if (a == POSITIVE_INFINITY || b == POSITIVE_INFINITY) {
      return POSITIVE_INFINITY;
    }
    if (a == NEGATIVE_INFINITY) {
      return b;
    }
    if (b == NEGATIVE_INFINITY) {
      return a;
    }
    return new IntegerWithInfinity(Integer.max(a.value(), b.value()));
  }

  @Override
  public String toString() {
    if (this == NEGATIVE_INFINITY) {
      return "-∞";
    }
    if (this == POSITIVE_INFINITY) {
      return "∞";
    }
    return value.toString();
  }

  @Override
  public boolean equals(Object other) {
    try {
      var otherIntegerWithInfinity = (IntegerWithInfinity) other;
      if (this == POSITIVE_INFINITY) {
        return other == POSITIVE_INFINITY;
      }
      if (this == NEGATIVE_INFINITY) {
        return other == NEGATIVE_INFINITY;
      }
      return this.value().equals(otherIntegerWithInfinity.value());
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public int compareTo(IntegerWithInfinity other) {
    if (other == null) {
      throw new NullPointerException();
    }
    if (this.equals(other)) {
      return 0;
    }
    if (this == NEGATIVE_INFINITY) {
      return -1;
    }
    if (this == POSITIVE_INFINITY) {
      return 1;
    }
    return this.value().compareTo(other.value());
  }

  public boolean isLessThan(IntegerWithInfinity other) {
    return this.compareTo(other) < 0;
  }

  public boolean isGreaterThan(IntegerWithInfinity other) {
    return this.compareTo(other) > 0;
  }
}
