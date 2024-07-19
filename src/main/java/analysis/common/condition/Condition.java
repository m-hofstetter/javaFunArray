package analysis.common.condition;

import base.DomainValue;
import funarray.EnvState;

public interface Condition<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> {

  EnvState<ElementT, VariableT> satisfy(EnvState<ElementT, VariableT> state);

  EnvState<ElementT, VariableT> satisfyComplement(EnvState<ElementT, VariableT> state);
}
