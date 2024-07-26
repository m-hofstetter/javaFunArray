package base.infint;

import lombok.Getter;

/**
 * A finite integer.
 */
@Getter
public final class FiniteInteger extends InfInt {

  private final int value;

  public FiniteInteger(int value) {
    this.value = value;
  }

  @Override
  public int compareTo(InfInt other) {
    return switch (other) {
      case NegativeInfinity n -> 1;
      case PositiveInfinity p -> -1;
      case FiniteInteger f -> Integer.compare(value, f.getValue());
    };
  }

  @Override
  public String toString() {
    return Integer.toString(value);
  }

  @Override
  public InfInt add(InfInt value) {
    return switch (value) {
      case FiniteInteger f -> new FiniteInteger(this.value + f.value);
      case Infinity i -> i;
    };
  }

  @Override
  public InfInt multiply(InfInt value) {
    return switch (value) {
      case FiniteInteger f -> new FiniteInteger(this.value * f.value);
      case Infinity i -> i.multiply(this);
    };
  }

  @Override
  public InfInt divide(InfInt value) {
    return switch (value) {
      case FiniteInteger f -> new FiniteInteger(this.value / f.value);
      case Infinity i -> InfInt.of(0);
    };
  }

  /*
   * Modulo with negative operands is defined as in the C language:
   * The sign of the divisor is ignored. The operation is conducted as if the dividend was positive
   * and its sign is then applied to the result. This differs from the java implementation where if
   * either operand is negative, the result will also be negative.
   */
  @Override
  public InfInt modulo(InfInt value) {
    return switch (value) {
      case FiniteInteger f -> {
        boolean dividendIsPositive = this.value >= 0;
        var dividend = dividendIsPositive ? this.value : -this.value;
        var divisor = f.value >= 0 ? f.value : -f.value;
        var mod = dividend % divisor;
        mod = dividendIsPositive ? mod : -mod;
        yield new FiniteInteger(mod);
      }
      case Infinity i -> i.modulo(this);
    };
  }

  @Override
  public InfInt negate() {
    return new FiniteInteger(-value);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof FiniteInteger f && f.value == this.value;
  }
}
