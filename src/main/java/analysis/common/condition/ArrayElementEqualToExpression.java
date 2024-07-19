package analysis.common.condition;

import base.DomainValue;
import funarray.EnvState;
import funarray.Expression;
import java.util.function.Function;

public record ArrayElementEqualToExpression<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        Expression index,
        Expression comparand,
        Function<VariableT, ElementT> valueConversion) implements Condition<ElementT, VariableT> {

  @Override
  public EnvState<ElementT, VariableT> satisfy(EnvState<ElementT, VariableT> state) {
    var value = state.getArrayElement(index);
    value = value.satisfyEqual(valueConversion.apply(state.calculateExpression(comparand)));
    return state.assignArrayElement(index, value);
  }

  @Override
  public EnvState<ElementT, VariableT> satisfyComplement(EnvState<ElementT, VariableT> state) {
    var value = state.getArrayElement(index);
    value = value.satisfyNotEqual(valueConversion.apply(state.calculateExpression(comparand)));
    return state.assignArrayElement(index, value);
  }

  @Override
  public String toString() {
    return String.format("A[%s] = %s", index, comparand);
  }
}
