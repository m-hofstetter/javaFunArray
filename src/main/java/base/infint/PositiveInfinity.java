package base.infint;

final class PositiveInfinity extends Infinity {
  @Override
  public String toString() {
    return "∞";
  }

  @Override
  public InfInt negate() {
    return new NegativeInfinity();
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof PositiveInfinity;
  }

  @Override
  public int compareTo(InfInt other) {
    if (other instanceof PositiveInfinity) {
      return 0;
    }
    return 1;
  }
}
