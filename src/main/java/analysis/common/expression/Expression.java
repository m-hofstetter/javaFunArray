package analysis.common.expression;

import abstractdomain.DomainValue;
import funarray.EnvState;
import funarray.NormalExpression;
import java.util.Set;

public interface Expression<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> {

  Set<NormalExpression> normalise(EnvState<ElementT, VariableT> environment);

  VariableT evaluate(EnvState<ElementT, VariableT> environment);
}
