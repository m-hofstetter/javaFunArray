package analysis.expression.atom;

import analysis.expression.Expression;
import base.DomainValue;
import funarray.EnvState;
import funarray.NormalExpression;
import java.util.function.Function;

public class Variable<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> extends Expression<ElementT, VariableT> {

  private final String variableRef;

  public Variable(
          Function<Integer, VariableT> constantToVariableValueConversion,
          Function<ElementT, VariableT> arrayElementValueToVariableValueConversion,
          VariableT unknown,
          String variableRef
  ) {
    super(constantToVariableValueConversion, arrayElementValueToVariableValueConversion, unknown);
    this.variableRef = variableRef;
  }

  @Override
  public NormalExpression normalise(EnvState<ElementT, VariableT> environment) {
    return new NormalExpression(variableRef, 0);
  }

  @Override
  public VariableT evaluate(EnvState<ElementT, VariableT> environment) {
    return environment.getVariableValue(variableRef);
  }

  @Override
  public String toString() {
    return variableRef;
  }
}
