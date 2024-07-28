package analysis.common.condition;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import funarray.EnvState;
import funarray.NormalExpression;

/**
 * A condition deciding whether an array element is equal to an expression.
 *
 * @param index           the index of the array element.
 * @param comparand       the expression comparand on the right side of the comparison.
 * @param valueConversion a translation function from the variable to the array element domain.
 * @param <ElementT>      the domain to abstract array element values with.
 * @param <VariableT>     the domain to abstract variable values with.
 */
public record ArrayElementEqualToExpression<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        String arrRef,
        NormalExpression index,
        NormalExpression comparand,
        AnalysisContext<ElementT, VariableT> context) implements Condition<ElementT, VariableT> {

  @Override
  public EnvState<ElementT, VariableT> satisfy(EnvState<ElementT, VariableT> state) {
    var value = state.getArrayElement(arrRef, index);
    value = value.satisfyEqual(context.convertVariableValueToArrayElementValue(state.calculateExpression(comparand)));
    return state.assignArrayElement(arrRef, index, value);
  }

  @Override
  public EnvState<ElementT, VariableT> satisfyComplement(EnvState<ElementT, VariableT> state) {
    var value = state.getArrayElement(arrRef, index);
    value = value.satisfyNotEqual(context.convertVariableValueToArrayElementValue(state.calculateExpression(comparand)));
    return state.assignArrayElement(arrRef, index, value);
  }

  @Override
  public String toString() {
    return String.format("A[%s] = %s", index, comparand);
  }
}
