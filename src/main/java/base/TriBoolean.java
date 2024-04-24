package base;

/**
 * An enum that represents a boolean value, that might be unknown.
 */
public enum TriBoolean {
    TRUE, FALSE, UNKNOWN;

    public TriBoolean invert() {
        return switch (this) {
            case UNKNOWN -> UNKNOWN;
            case FALSE -> TRUE;
            case TRUE -> FALSE;
        };
    }
}
