package analysis.common.condition;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import funarray.State;

public record EqualTo<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(Expression<ElementT, VariableT> left,
                                                  Expression<ElementT, VariableT> right,
                                                  AnalysisContext<ElementT, VariableT> context) implements Condition<ElementT, VariableT> {

  @Override
  public State<ElementT, VariableT> satisfy(State<ElementT, VariableT> state) {
    state = satisfyForSingleSide(left, right, context, state, DomainValue::satisfyEqual, DomainValue::satisfyEqual);
    state = satisfyForSingleSide(right, left, context, state, DomainValue::satisfyEqual, DomainValue::satisfyEqual);
    state = satisfyBoundOrder(state, left, right, State::satisfyExpressionEqualToInBoundOrder);
    return state;
  }

  @Override
  public State<ElementT, VariableT> satisfyComplement(State<ElementT, VariableT> state) {
    state = satisfyForSingleSide(left, right, context, state, DomainValue::satisfyNotEqual, DomainValue::satisfyNotEqual);
    state = satisfyForSingleSide(right, left, context, state, DomainValue::satisfyNotEqual, DomainValue::satisfyNotEqual);
    state = satisfyBoundOrder(state, right, left, State::satisfyExpressionUnequalToInBoundOrder);
    return state;
  }

  @Override
  public String toString() {
    return "%s = %s".formatted(left, right);
  }
}
