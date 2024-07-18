package analysis.common.condition;

import base.DomainValue;
import funarray.EnvState;
import funarray.Expression;
import java.util.function.Function;

public record ArrayElementLessThanExpression<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        Expression index, Expression comparand,
        Function<VARIABLE, ELEMENT> valueConversion) implements Condition<ELEMENT, VARIABLE> {

  @Override
  public EnvState<ELEMENT, VARIABLE> satisfy(EnvState<ELEMENT, VARIABLE> state) {
    var value = state.getArrayElement(index);
    value = value.satisfyLessThan(valueConversion.apply(state.calculateExpression(comparand)));
    return state.assignArrayElement(index, value);
  }

  @Override
  public EnvState<ELEMENT, VARIABLE> satisfyComplement(EnvState<ELEMENT, VARIABLE> state) {
    var value = state.getArrayElement(index);
    value = value.satisfyGreaterEqualThan(valueConversion.apply(state.calculateExpression(comparand)));
    return state.assignArrayElement(index, value);
  }

  @Override
  public String toString() {
    return String.format("A[%s] < %s", index, comparand);
  }
}
