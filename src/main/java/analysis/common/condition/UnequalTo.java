package analysis.common.condition;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import funarray.EnvState;

public record UnequalTo<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(Expression<ElementT, VariableT> left,
                                                  Expression<ElementT, VariableT> right,
                                                  AnalysisContext<ElementT, VariableT> context) implements Condition<ElementT, VariableT> {

  @Override
  public EnvState<ElementT, VariableT> satisfy(EnvState<ElementT, VariableT> state) {
    state = satisfyForSingleSide(left, right, context, state, DomainValue::satisfyNotEqual, DomainValue::satisfyNotEqual);
    state = satisfyForSingleSide(right, left, context, state, DomainValue::satisfyNotEqual, DomainValue::satisfyNotEqual);
    state = satisfyBoundOrder(state, left, right, EnvState::satisfyExpressionUnequalToInBoundOrder);
    return state;
  }

  @Override
  public EnvState<ElementT, VariableT> satisfyComplement(EnvState<ElementT, VariableT> state) {
    state = satisfyForSingleSide(left, right, context, state, DomainValue::satisfyEqual, DomainValue::satisfyEqual);
    state = satisfyForSingleSide(right, left, context, state, DomainValue::satisfyEqual, DomainValue::satisfyEqual);
    state = satisfyBoundOrder(state, right, left, EnvState::satisfyExpressionEqualToInBoundOrder);
    return state;
  }

}
