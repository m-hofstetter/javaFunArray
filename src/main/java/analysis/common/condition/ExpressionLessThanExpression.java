package analysis.common.condition;

import base.DomainValue;
import funarray.EnvState;
import funarray.Expression;

public record ExpressionLessThanExpression<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        Expression left,
        Expression right) implements Condition<ELEMENT, VARIABLE> {

  @Override
  public EnvState<ELEMENT, VARIABLE> satisfy(EnvState<ELEMENT, VARIABLE> state) {
    return state.satisfyExpressionLessThan(left, right);
  }

  @Override
  public EnvState<ELEMENT, VARIABLE> satisfyComplement(EnvState<ELEMENT, VARIABLE> state) {
    return state.satisfyExpressionLessEqualThan(right, left);
  }

  @Override
  public String toString() {
    return String.format("%s < %s", left, right);
  }
}
