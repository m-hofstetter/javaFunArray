package analysis.common.condition;

import abstractdomain.DomainValue;
import funarray.EnvState;
import funarray.Expression;

/**
 * A condition deciding whether an expression is less than another expression.
 *
 * @param left        the expression on the left side of the comparison.
 * @param right       the expression on the right side of the comparison.
 * @param <ElementT>  the domain to abstract array element values with.
 * @param <VariableT> the domain to abstract variable values with.
 */
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
