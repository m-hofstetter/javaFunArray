package base.infint;

import lombok.Getter;

@Getter
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
}
