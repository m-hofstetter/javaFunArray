package funarray.varref;

public class ZeroReference implements Reference {
  public static final ZeroReference INSTANCE = new ZeroReference();

  private ZeroReference() {
  }
}
