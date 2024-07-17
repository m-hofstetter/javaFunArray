package base.infint;

final class NegativeInfinity extends Infinity {
  @Override
  public String toString() {
    return "-∞";
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
