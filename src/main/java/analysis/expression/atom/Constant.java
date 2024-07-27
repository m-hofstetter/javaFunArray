package analysis.expression.atom;

import analysis.expression.Expression;
import base.DomainValue;
import funarray.EnvState;
import funarray.NormalExpression;
import java.util.function.Function;
import lombok.Getter;

@Getter
public class Constant<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> extends Expression<ElementT, VariableT> {

  private final int constant;

  public Constant(
          Function<Integer, VariableT> constantToVariableValueConversion,
          Function<ElementT, VariableT> arrayElementValueToVariableValueConversion,
          int constant,
          VariableT unknown
  ) {
    super(constantToVariableValueConversion, arrayElementValueToVariableValueConversion, unknown);
    this.constant = constant;
  }

  @Override
  public NormalExpression normalise() {
    return new NormalExpression("0", constant);
  }

  @Override
  public VariableT evaluate(EnvState<ElementT, VariableT> environment) {
    return constantToVariableValueConversion.apply(constant);
  }

  @Override
  public String toString() {
    return Integer.toString(constant);
  }
}
