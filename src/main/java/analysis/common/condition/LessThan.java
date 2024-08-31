package analysis.common.condition;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import funarray.EnvState;

public record LessThan<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(Expression<ElementT, VariableT> left,
                                                  Expression<ElementT, VariableT> right,
                                                  AnalysisContext<ElementT, VariableT> context) implements Condition<ElementT, VariableT> {

  @Override
  public EnvState<ElementT, VariableT> satisfy(EnvState<ElementT, VariableT> state) {
    state = satisfyForSingleSide(left, right, context, state, DomainValue::satisfyLessThan, DomainValue::satisfyLessThan);
    state = satisfyForSingleSide(right, left, context, state, DomainValue::satisfyGreaterEqualThan, DomainValue::satisfyGreaterEqualThan);
    state = satisfyBoundOrder(state, left, right, EnvState::satisfyExpressionLessThanInBoundOrder);
    return state;
  }

  @Override
  public EnvState<ElementT, VariableT> satisfyComplement(EnvState<ElementT, VariableT> state) {
    state = satisfyForSingleSide(left, right, context, state, DomainValue::satisfyGreaterEqualThan, DomainValue::satisfyGreaterEqualThan);
    state = satisfyForSingleSide(right, left, context, state, DomainValue::satisfyLessThan, DomainValue::satisfyLessThan);
    state = satisfyBoundOrder(state, right, left, EnvState::satisfyExpressionLessEqualThanInBoundOrder);
    return state;
  }

  @Override
  public String toString() {
    return "%s < %s".formatted(left, right);
  }
}
