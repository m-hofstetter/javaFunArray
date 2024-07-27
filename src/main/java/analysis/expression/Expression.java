package analysis.expression;

import base.DomainValue;
import funarray.EnvState;
import funarray.NormalExpression;
import java.util.function.Function;

public abstract class Expression<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> {

  protected final Function<Integer, VariableT> constantToVariableValueConversion;
  protected final Function<ElementT, VariableT> arrayElementValueToVariableValueConversion;
  protected final VariableT unknown;

  public Expression(
          Function<Integer, VariableT> constantToVariableValueConversion,
          Function<ElementT, VariableT> arrayElementValueToVariableValueConversion,
          VariableT unknown
  ) {
    this.constantToVariableValueConversion = constantToVariableValueConversion;
    this.arrayElementValueToVariableValueConversion = arrayElementValueToVariableValueConversion;
    this.unknown = unknown;
  }

  public abstract NormalExpression normalise(EnvState<ElementT, VariableT> environment) throws NormaliseExpressionException;

  public abstract VariableT evaluate(EnvState<ElementT, VariableT> environment);
}
