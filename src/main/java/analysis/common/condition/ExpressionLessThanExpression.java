package analysis.common.condition;

import base.DomainValue;
import funarray.EnvState;
import funarray.Expression;

public record ExpressionLessThanExpression<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        Expression left,
        Expression right) implements Condition<ElementT, VariableT> {

  @Override
  public EnvState<ElementT, VariableT> satisfy(EnvState<ElementT, VariableT> state) {
    return state.satisfyExpressionLessThan(left, right);
  }

  @Override
  public EnvState<ElementT, VariableT> satisfyComplement(EnvState<ElementT, VariableT> state) {
    return state.satisfyExpressionLessEqualThan(right, left);
  }

  @Override
  public String toString() {
    return String.format("%s < %s", left, right);
  }
}
