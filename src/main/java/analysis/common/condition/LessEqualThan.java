package analysis.common.condition;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import funarray.EnvState;

public record LessEqualThan<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(Expression<ElementT, VariableT> left,
                                                  Expression<ElementT, VariableT> right,
                                                  AnalysisContext<ElementT, VariableT> context) implements Condition<ElementT, VariableT> {

  @Override
  public EnvState<ElementT, VariableT> satisfy(EnvState<ElementT, VariableT> state) {
    state = satisfyForSingleSide(left, right, context, state, DomainValue::satisfyLessEqualThan, DomainValue::satisfyLessEqualThan);
    state = satisfyForSingleSide(right, left, context, state, DomainValue::satisfyGreaterThan, DomainValue::satisfyGreaterThan);
    state = satisfyBoundOrder(state, left, right, EnvState::satisfyExpressionLessEqualThanInBoundOrder);
    return state;
  }

  @Override
  public EnvState<ElementT, VariableT> satisfyComplement(EnvState<ElementT, VariableT> state) {
    state = satisfyForSingleSide(left, right, context, state, DomainValue::satisfyGreaterThan, DomainValue::satisfyGreaterThan);
    state = satisfyForSingleSide(right, left, context, state, DomainValue::satisfyLessEqualThan, DomainValue::satisfyLessEqualThan);
    state = satisfyBoundOrder(state, right, left, EnvState::satisfyExpressionLessThanInBoundOrder);
    return state;
  }

  @Override
  public String toString() {
    return "%s ≤ %s".formatted(left, right);
  }
}
