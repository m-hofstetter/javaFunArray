package funarray.varref;

public class VariableReference implements Reference {

  private final String name;

  VariableReference(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other instanceof VariableReference otherVariableReference) {
      return this.name.equals(otherVariableReference.name);
    }
    return false;
  }
}
