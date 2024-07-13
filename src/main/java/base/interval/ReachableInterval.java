package base.interval;

import base.infint.InfInt;


final class ReachableInterval extends Interval {

  private final InfInt lowerLimit;
  private final InfInt upperLimit;

  public ReachableInterval(InfInt lowerLimit, InfInt upperLimit) {
    this.lowerLimit = lowerLimit;
    this.upperLimit = upperLimit;
  }

  @Override
  public Interval join(Interval other) {
    if (other instanceof ReachableInterval reachableOther) {
      var lower = InfInt.min(this.lowerLimit, reachableOther.lowerLimit);
      var upper = InfInt.max(this.upperLimit, reachableOther.upperLimit);
      return new ReachableInterval(lower, upper);
    }
    return this;
  }


  @Override
  public Interval meet(Interval other) {
    if (other instanceof ReachableInterval reachableOther) {
      var lower = InfInt.max(this.lowerLimit, reachableOther.lowerLimit);
      var upper = InfInt.min(this.upperLimit, reachableOther.upperLimit);

      if (!lower.isGreaterThan(upper)) {
        return new ReachableInterval(lower, upper);
      }
    }

    return new Unreachable();
  }

  @Override
  public Interval widen(Interval other) {
    if (other instanceof ReachableInterval reachableOther) {
      var lower = this.lowerLimit;
      var upper = this.upperLimit;

      if (reachableOther.lowerLimit.isLessThan(this.lowerLimit)) {
        lower = InfInt.negInf();
      }
      if (reachableOther.upperLimit.isGreaterThan(this.upperLimit)) {
        upper = InfInt.posInf();
      }
      return new ReachableInterval(lower, upper);
    }
    return this;
  }

  @Override
  public Interval narrow(Interval other) {
    if (other instanceof ReachableInterval reachableOther) {
      var lower = this.lowerLimit;
      var upper = this.upperLimit;
      if (this.lowerLimit.isNegInf()) {
        lower = reachableOther.lowerLimit;
      }
      if (this.upperLimit.isPosInf()) {
        upper = reachableOther.upperLimit;
      }
      return new ReachableInterval(lower, upper);
    }
    return new Unreachable();
  }

  @Override
  public String toString() {
    return "[%s, %s]".formatted(lowerLimit, upperLimit);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof ReachableInterval reachableOther) {
      return this.lowerLimit.equals(reachableOther.lowerLimit)
              && this.upperLimit.equals(reachableOther.upperLimit);
    }
    return false;
  }

  @Override
  public Interval add(Interval value) {
    if (value instanceof ReachableInterval reachableValue) {
      return new ReachableInterval(lowerLimit.add(reachableValue.lowerLimit), upperLimit.add(reachableValue.upperLimit));
    }
    return new Unreachable();
  }

  @Override
  public Interval inverse() {
    return new ReachableInterval(upperLimit.negate(), lowerLimit.negate());
  }

  @Override
  public Interval subtract(Interval value) {
    return add(value.inverse());
  }
}
