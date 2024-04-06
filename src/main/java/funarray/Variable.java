package funarray;

import base.Interval;

/**
 * The abstract representation of a variable.
 *
 * @param value the value of the variable.
 * @param name  the name of the variable.
 */
public record Variable(Interval value, String name) {

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
