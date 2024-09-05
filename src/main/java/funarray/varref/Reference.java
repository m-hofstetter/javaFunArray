package funarray.varref;

public interface Reference {
  static Reference of(String ref) {
    return new VariableReference(ref);
  }

  static Reference zero() {
    return ZeroReference.INSTANCE;
  }
}
