package analysis.expression.nonassociative;

import analysis.expression.Expression;
import base.DomainValue;
import java.util.Map;
import java.util.function.Function;

public class Modulo extends Expression {

  private final Expression left;
  private final Expression right;

  public Modulo(Expression left, Expression right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public <VariableT extends DomainValue<VariableT>> VariableT evaluate(Map<String, VariableT> variableValues, Function<Integer, VariableT> constantToVariableValueConversion) {
    return left.evaluate(variableValues, constantToVariableValueConversion).modulo(right.evaluate(variableValues, constantToVariableValueConversion));
  }

  @Override
  public String toString() {
    return "(%s) %% (%s)".formatted(left.toString(), right.toString());
  }
}
