package funarray.varref;

public sealed interface Reference permits VariableReference, ZeroReference {
  static Reference of(String ref) {
    return new VariableReference(ref);
  }

  static Reference zero() {
    return ZeroReference.INSTANCE;
  }
}
