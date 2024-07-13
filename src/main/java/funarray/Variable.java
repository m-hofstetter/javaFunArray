package funarray;

import base.interval.Interval;

/**
 * The abstract representation of a variable.
 *
 * @param value the value of the variable.
 * @param name  the name of the variable.
 */
public record Variable(Interval value, String name) {

  public boolean equals(Variable other) {
    return this.value().equals(other.value()) && this.name().equals(other.name());
  }

  @Override
  public String toString() {
    return name();
  }
}
