package funarray;

import base.infint.InfInt;
import base.interval.Interval;

/**
 * The abstract representation of a variable.
 *
 * @param value the value of the variable.
 * @param name  the name of the variable.
 */
public record Variable(Interval value, String name) {

  public Variable(InfInt from, InfInt to, String name) {
    this(Interval.of(from, to), name);
  }

  public Variable(InfInt value, String name) {
    this(Interval.of(value, value), name);
  }

  public Variable(int from, int to, String name) {
    this(InfInt.of(from), InfInt.of(to), name);
  }

  public Variable(int value, String name) {
    this(InfInt.of(value), name);
  }

  public static Variable ZERO_VALUE = new Variable(Interval.of(0, 0), "0");

  public boolean equals(Variable other) {
    return this.value().equals(other.value()) && this.name().equals(other.name());
  }

  @Override
  public String toString() {
    return name();
  }

  public String toStringWithValue() {
    return "%s: %s".formatted(name(), value());
  }

  public Variable join(Variable other) {
    if (!this.name.equals(other.name)) {
      throw new IllegalStateException("Cannot join two different variables.");
    }
    return new Variable(value.join(other.value), name);
  }
}
