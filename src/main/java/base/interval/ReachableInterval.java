package base.interval;

import base.infint.InfInt;
import lombok.Getter;


@Getter
public final class ReachableInterval extends Interval {

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

  @Override
  public Interval addConstant(InfInt constant) {
    return add(Interval.of(constant, constant));
  }

  @Override
  public Interval subtractConstant(InfInt constant) {
    return addConstant(constant.negate());
  }

  @Override
  public Interval satisfyLessEqualThan(Interval other) {
    if (other instanceof ReachableInterval reachableOther) {
      if (reachableOther.upperLimit.equals(InfInt.posInf())) {
        return this;
      }
      return this.meet(Interval.of(InfInt.negInf(), reachableOther.upperLimit));
    }
    return new Unreachable();
  }

  @Override
  public Interval satisfyGreaterEqualThan(Interval other) {
    if (other instanceof ReachableInterval reachableOther) {
      if (reachableOther.lowerLimit.equals(InfInt.negInf())) {
        return this;
      }
      return this.meet(Interval.of(reachableOther.lowerLimit, InfInt.posInf()));
    }
    return new Unreachable();
  }

  @Override
  public Interval satisfyLessThan(Interval other) {
    if (other instanceof ReachableInterval reachableOther) {
      if (reachableOther.lowerLimit.equals(InfInt.negInf())) {
        return new Unreachable();
      }
      return this.meet(Interval.of(InfInt.negInf(), reachableOther.lowerLimit.subtract(1)));
    }
    return new Unreachable();
  }

  @Override
  public Interval satisfyGreaterThan(Interval other) {
    if (other instanceof ReachableInterval reachableOther) {
      if (reachableOther.upperLimit.equals(InfInt.posInf())) {
        return new Unreachable();
      }
      return this.meet(Interval.of(reachableOther.upperLimit.add(1), InfInt.posInf()));
    }
    return new Unreachable();
  }
}
