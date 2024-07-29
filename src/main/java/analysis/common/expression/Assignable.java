package analysis.common.expression;

import abstractdomain.DomainValue;
import funarray.EnvState;

public interface Assignable<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> extends Expression<ElementT, VariableT> {
  EnvState<ElementT, VariableT> assign(
          Expression<ElementT, VariableT> value,
          EnvState<ElementT, VariableT> environmentState
  );
}
