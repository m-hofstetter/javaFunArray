package base.interval;

import base.infint.InfInt;

final class Unreachable extends Interval {

  @Override
  public Interval join(Interval other) {
    return other;
  }

  @Override
  public Interval meet(Interval other) {
    return this;
  }

  @Override
  public Interval widen(Interval other) {
    return other;
  }

  @Override
  public Interval narrow(Interval other) {
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
  public Interval add(Interval value) {
    return this;
  }

  @Override
  public Interval inverse() {
    return this;
  }

  @Override
  public Interval subtract(Interval value) {
    return this;
  }

  @Override
  public Interval addConstant(InfInt constant) {
    return this;
  }

  @Override
  public Interval subtractConstant(InfInt constant) {
    return this;
  }

  @Override
  public Interval satisfyLessEqualThan(Interval other) {
    return this;
  }

  @Override
  public Interval satisfyGreaterEqualThan(Interval other) {
    return this;
  }

  @Override
  public Interval satisfyLessThan(Interval other) {
    return this;
  }

  @Override
  public Interval satisfyGreaterThan(Interval other) {
    return this;
  }
}
