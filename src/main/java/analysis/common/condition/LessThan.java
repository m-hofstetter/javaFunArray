package analysis.common.condition;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import funarray.state.State;

public record LessThan<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(Expression<ElementT, VariableT> left,
                                                  Expression<ElementT, VariableT> right,
                                                  AnalysisContext<ElementT, VariableT> context) implements Condition<ElementT, VariableT> {

  @Override
  public State<ElementT, VariableT> satisfy(State<ElementT, VariableT> state) {
    state = satisfyForSingleSide(left, right, context, state, DomainValue::satisfyLessThan, DomainValue::satisfyLessThan);
    state = satisfyForSingleSide(right, left, context, state, DomainValue::satisfyGreaterEqualThan, DomainValue::satisfyGreaterEqualThan);
    state = satisfyBoundOrder(state, left, right, State::satisfyExpressionLessThanInBoundOrder);
    return state;
  }

  @Override
  public State<ElementT, VariableT> satisfyComplement(State<ElementT, VariableT> state) {
    state = satisfyForSingleSide(left, right, context, state, DomainValue::satisfyGreaterEqualThan, DomainValue::satisfyGreaterEqualThan);
    state = satisfyForSingleSide(right, left, context, state, DomainValue::satisfyLessThan, DomainValue::satisfyLessThan);
    state = satisfyBoundOrder(state, right, left, State::satisfyExpressionLessEqualThanInBoundOrder);
    return state;
  }

  @Override
  public String toString() {
    return "%s < %s".formatted(left, right);
  }
}
