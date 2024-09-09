package analysis.common.expression;

import abstractdomain.DomainValue;
import funarray.state.State;

public interface Assignable<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> extends Expression<ElementT, VariableT> {
  State<ElementT, VariableT> assign(
          Expression<ElementT, VariableT> value,
          State<ElementT, VariableT> state
  );
}
