package analysis.expression;

public class NormaliseExpressionException extends Exception {
  public NormaliseExpressionException(Expression<?, ?> expression) {
    super("Cannot normalise expression: " + expression.toString());
  }
}
