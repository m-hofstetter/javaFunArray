package analysis.expression.atom;

import analysis.expression.Expression;
import base.DomainValue;
import java.util.Map;
import java.util.function.Function;

public class AtomExpressionVariable extends Expression {

  private final String variableRef;

  public AtomExpressionVariable(String variableRef) {
    this.variableRef = variableRef;
  }

  @Override
  public <VariableT extends DomainValue<VariableT>> VariableT evaluate(Map<String, VariableT> variableValues, Function<Integer, VariableT> constantToVariableValueConversion) {
    return variableValues.get(variableRef);
  }

  @Override
  public String toString() {
    return variableRef;
  }
}
