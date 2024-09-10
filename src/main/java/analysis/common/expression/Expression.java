package analysis.common.expression;

import abstractdomain.DomainValue;
import abstractdomain.ValueRelation;
import funarray.NormalExpression;
import funarray.state.State;
import java.util.Set;

public interface Expression<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> {

  Set<NormalExpression> normalise(State<ElementT, VariableT> environment);

  VariableT evaluate(State<ElementT, VariableT> environment);

  Set<State<ElementT, VariableT>> satisfy(Expression<ElementT, VariableT> comparand, ValueRelation<VariableT> relation, State<ElementT, VariableT> state);
}
