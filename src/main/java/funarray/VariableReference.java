package funarray;

/**
 * The abstract representation of a variable reference.
 *
 * @param name  the name of the variable.
 */
public record VariableReference(String name) {

  public boolean equals(VariableReference other) {
    return this.name.equals(other.name);
  }

  public static VariableReference zero() {
    return new VariableReference("0");
  }

  @Override
  public String toString() {
    return name();
  }
}
