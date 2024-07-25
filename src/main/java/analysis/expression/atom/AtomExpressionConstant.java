package analysis.expression.atom;

import analysis.expression.Expression;
import base.DomainValue;
import java.util.Map;
import java.util.function.Function;

public class AtomExpressionConstant extends Expression {

  private final int constant;

  public AtomExpressionConstant(int constant) {
    this.constant = constant;
  }

  @Override
  public <VariableT extends DomainValue<VariableT>> VariableT evaluate(Map<String, VariableT> variableValues, Function<Integer, VariableT> constantToVariableValueConversion) {
    return constantToVariableValueConversion.apply(constant);
  }

  @Override
  public String toString() {
    if (constant >= 0) {
      return Integer.toString(constant);
    }
    return "(%s)".formatted(constant);
  }
}
