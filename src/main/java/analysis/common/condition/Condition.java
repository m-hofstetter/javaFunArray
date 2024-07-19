package analysis.common.condition;

import base.DomainValue;
import funarray.EnvState;

/**
 * A condition for branching control structures in an {@link analysis.common.Analysis}.
 *
 * @param <ElementT>  the domain to abstract array element values with.
 * @param <VariableT> the domain to abstract variable values with.
 */
public interface Condition<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> {

  /**
   * Satisfies the condition.
   *
   * @param state the state.
   * @return the state with the condition satisfied.
   */
  EnvState<ElementT, VariableT> satisfy(EnvState<ElementT, VariableT> state);

  /**
   * Ensures the condition is not satisfied.
   *
   * @param state the state.
   * @return the state with the condition not satisfied.
   */
  EnvState<ElementT, VariableT> satisfyComplement(EnvState<ElementT, VariableT> state);
}
