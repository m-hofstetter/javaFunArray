package base;

/**
 * An enum that represents a boolean value, that might be unknown.
 */
public enum TriBoolean {
  TRUE, FALSE, UNKNOWN;

  /**
   * Inverts a TriBoolean. UNKNOWN yields UNKNOWN.
   *
   * @return the inverted value
   */
  public TriBoolean invert() {
    return switch (this) {
      case UNKNOWN -> UNKNOWN;
      case FALSE -> TRUE;
      case TRUE -> FALSE;
    };
  }
}
