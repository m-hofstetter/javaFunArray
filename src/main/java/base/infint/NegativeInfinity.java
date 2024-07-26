package base.infint;

/**
 * Negative infinity value in {@link InfInt}.
 */
public final class NegativeInfinity extends Infinity {
  @Override
  public String toString() {
    return "-∞";
  }

  @Override
  public InfInt multiply(InfInt value) {
    if (value.isNegInf()) {
      return InfInt.posInf();
    }
    return this;
  }

  @Override
  public InfInt divide(InfInt value) {
    if (value.isGreaterThan(InfInt.of(0))) {
      return InfInt.negInf();
    } else if (value.equals(InfInt.of(0))) {
      throw new ArithmeticException("Cannot divide by zero");
    } else {
      return InfInt.posInf();
    }
  }

  @Override
  public InfInt negate() {
    return new PositiveInfinity();
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof NegativeInfinity;
  }

  @SuppressWarnings("ComparatorMethodParameterNotUsed")
  @Override
  public int compareTo(InfInt other) {
    if (other instanceof NegativeInfinity) {
      return 0;
    }
    return -1;
  }


}
