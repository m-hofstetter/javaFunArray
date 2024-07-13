package funarray;

import base.DomainValue;

/**
 * The abstract representation of a variable.
 *
 * @param value the value of the variable.
 * @param name  the name of the variable.
 */
public record Variable<T extends DomainValue<T>>(T value, String name) {

  public boolean equals(Variable<T> other) {
    return this.value().equals(other.value()) && this.name().equals(other.name());
  }

  @Override
  public String toString() {
    return name();
  }
}
