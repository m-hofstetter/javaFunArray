package funarray;

import base.Interval;
import base.infint.InfInt;

/**
 * The abstract representation of a variable.
 *
 * @param value the value of the variable.
 * @param name  the name of the variable.
 */
public record Variable(Interval value, String name) {

  public Variable(InfInt from, InfInt to, String name) {
    this(new Interval(from, to), name);
  }

  public Variable(InfInt value, String name) {
    this(new Interval(value, value), name);
  }

  public Variable(int from, int to, String name) {
    this(InfInt.of(from), InfInt.of(to), name);
  }

  public Variable(int value, String name) {
    this(InfInt.of(value), name);
  }

  public static Variable ZERO_VALUE = new Variable(new Interval(0, 0), "0");

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
}
