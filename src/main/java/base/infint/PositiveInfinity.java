package base.infint;

/**
 * Positive infinity value in {@link InfInt}.
 */
public final class PositiveInfinity extends Infinity {
  @Override
  public String toString() {
    return "∞";
  }

  @Override
  public InfInt multiply(InfInt value) {
    if (value.isNegInf()) {
      return InfInt.negInf();
    }
    return this;
  }

  @Override
  public InfInt divide(InfInt value) {
    if (value.isGreaterThan(InfInt.of(0))) {
      return InfInt.posInf();
    } else if (value.equals(InfInt.of(0))) {
      throw new ArithmeticException("Cannot divide by zero");
    } else {
      return InfInt.negInf();
    }
  }

  @Override
  public InfInt negate() {
    return new NegativeInfinity();
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof PositiveInfinity;
  }

  @SuppressWarnings("ComparatorMethodParameterNotUsed")
  @Override
  public int compareTo(InfInt other) {
    if (other instanceof PositiveInfinity) {
      return 0;
    }
    return 1;
  }
}
