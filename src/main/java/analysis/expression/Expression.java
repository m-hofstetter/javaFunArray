package analysis.expression;

import base.DomainValue;
import java.util.Map;
import java.util.function.Function;

public abstract class Expression {

  public Expression transformToNormalForm() {
    return null;
  }

  public abstract <VariableT extends DomainValue<VariableT>>
  VariableT evaluate(Map<String, VariableT> variableValues,
                     Function<Integer, VariableT> constantToVariableValueConversion);
}
