package base.interval;

import base.DomainValue;
import base.infint.InfInt;
import exception.DomainException;

final class Unreachable extends Interval {

  @Override
  public Interval join(DomainValue other) {
    if (other instanceof Interval otherInterval) {
      return otherInterval;
    }
    throw new DomainException("Cannot join domain values from different domains.");
  }

  @Override
  public Interval meet(DomainValue other) {
    if (!(other instanceof Interval)) {
      throw new DomainException("Cannot meet domain values from different domains.");
    }
    return this;
  }

  @Override
  public Interval widen(DomainValue other) {
    if (other instanceof Interval otherInterval) {
      return otherInterval;
    }
    throw new DomainException("Cannot widen domain values from different domains.");
  }

  @Override
  public Interval narrow(DomainValue other) {
    if (!(other instanceof Interval)) {
      throw new DomainException("Cannot narrow domain values from different domains.");
    }
    return this;
  }

  @Override
  public String toString() {
    return "⊥";
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof Unreachable;
  }

  @Override
  public Interval add(InfInt value) {
    return this;
  }
}
