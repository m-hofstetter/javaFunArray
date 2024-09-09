package analysis.common.condition;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import analysis.common.expression.atom.ArrayElement;
import funarray.NormalExpression;
import funarray.state.State;
import java.util.function.BinaryOperator;

/**
 * A condition for branching control structures in an {@link analysis.common.Analysis}.
 *
 * @param <ElementT>  the domain to abstract array element values with.
 * @param <VariableT> the domain to abstract variable values with.
 */
public interface Condition<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> {

  default State<ElementT, VariableT> satisfyForSingleSide(Expression<ElementT, VariableT> left,
                                                          Expression<ElementT, VariableT> right,
                                                          AnalysisContext<ElementT, VariableT> context,
                                                          State<ElementT, VariableT> state,
                                                          BinaryOperator<ElementT> operatorElement,
                                                          BinaryOperator<VariableT> operatorVariable) {
    if (left instanceof ArrayElement<ElementT, VariableT> arrayElement) {
      for (var indexNormalForm : arrayElement.index().normalise(state)) {
        state = state.satisfyForValues(
                arrayElement.arrayRef(),
                indexNormalForm,
                context.convertVariableValueToArrayElementValue(right.evaluate(state)),
                operatorElement);
      }
    } else {
      for (var leftNormalForm : left.normalise(state)) {
        state = state.satisfyForValues(leftNormalForm, right.evaluate(state), operatorVariable);
      }
    }

    return state;
  }

  default State<ElementT, VariableT> satisfyBoundOrder(State<ElementT, VariableT> state,
                                                       Expression<ElementT, VariableT> left,
                                                       Expression<ElementT, VariableT> right,
                                                       BoundOrderModification<ElementT, VariableT> modification) {
    for (NormalExpression l : left.normalise(state)) {
      for (NormalExpression r : right.normalise(state)) {
        state = modification.apply(state, l, r);
      }
    }
    return state;
  }

  /**
   * Satisfies the condition.
   *
   * @param state the state.
   * @return the state with the condition satisfied.
   */
  State<ElementT, VariableT> satisfy(State<ElementT, VariableT> state);

  /**
   * Ensures the condition is not satisfied.
   *
   * @param state the state.
   * @return the state with the condition not satisfied.
   */
  State<ElementT, VariableT> satisfyComplement(State<ElementT, VariableT> state);

  @FunctionalInterface
  interface BoundOrderModification<
          ElementT extends DomainValue<ElementT>,
          VariableT extends DomainValue<VariableT>> {
    State<ElementT, VariableT> apply(State<ElementT, VariableT> state, NormalExpression left, NormalExpression right);
  }
}
