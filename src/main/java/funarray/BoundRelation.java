package funarray;

public enum BoundRelation {
  LESS,
  GREATER,
  LESS_EQUAL,
  GREATER_EQUAL,
  EQUAL,
  NOT_EQUAL;

  public BoundRelation inverseRelation() {
    return switch (this) {
      case LESS -> GREATER;
      case GREATER -> LESS;
      case LESS_EQUAL -> GREATER_EQUAL;
      case GREATER_EQUAL -> LESS_EQUAL;
      case EQUAL -> EQUAL;
      case NOT_EQUAL -> NOT_EQUAL;
    };
  }

  public BoundRelation complementRelation() {
    return switch (this) {
      case LESS -> GREATER_EQUAL;
      case GREATER -> LESS_EQUAL;
      case LESS_EQUAL -> GREATER;
      case GREATER_EQUAL -> LESS;
      case EQUAL -> NOT_EQUAL;
      case NOT_EQUAL -> EQUAL;
    };
  }
}
