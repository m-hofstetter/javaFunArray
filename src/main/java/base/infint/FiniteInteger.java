package base.infint;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PRIVATE)
final class FiniteInteger extends InfInt {

  private final int value;

  FiniteInteger(int value) {
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
  public InfInt negate() {
    return new FiniteInteger(-value);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof FiniteInteger f && f.value == this.value;
  }
}
