package base.infint;

final class NegativeInfinity extends InfInt {
  @Override
  public String toString() {
    return "-∞";
  }

  @Override
  public int compareTo(InfInt other) {
    if (other instanceof NegativeInfinity) {
      return 0;
    }
    return -1;
  }
}
