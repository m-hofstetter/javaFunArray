package abstractdomain.interval.value;

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
  public Interval negate() {
    return this;
  }

  @Override
  public Interval subtract(Interval value) {
    return this;
  }

  @Override
  public Interval addConstant(int constant) {
    return this;
  }

  @Override
  public Interval subtractConstant(int constant) {
    return this;
  }

  @Override
  public Interval multiply(Interval other) {
    return this;
  }

  @Override
  public Interval multiplyByConstant(int constant) {
    return this;
  }

  @Override
  public Interval divide(Interval other) {
    return this;
  }

  @Override
  public Interval divideByConstant(int constant) {
    return this;
  }

  @Override
  public Interval modulo(Interval other) {
    return this;
  }

  @Override
  public Interval modulo(int constant) {
    return this;
  }

  @Override
  public Interval absoluteValue() {
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

  @Override
  public Interval satisfyEqual(Interval other) {
    return this;
  }

  @Override
  public Interval satisfyNotEqual(Interval other) {
    return this;
  }

  @Override
  public boolean isReachable() {
    return false;
  }
}
