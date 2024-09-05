package funarray.varref;

public final class ZeroReference implements Reference {
  public static final ZeroReference INSTANCE = new ZeroReference();

  private ZeroReference() {
  }

  @Override
  public String toString() {
    return "\uD835\uDCCB₀";
  }
}
