package analysis.common.expression.atom;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import analysis.common.expression.Assignable;
import analysis.common.expression.Expression;
import funarray.NormalExpression;
import funarray.state.ReachableState;
import funarray.state.State;
import funarray.varref.Reference;
import java.util.Set;

public record Variable<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(Reference variableRef,
                                                  AnalysisContext<ElementT, VariableT> context)
        implements Assignable<ElementT, VariableT> {

  public Variable(String variableRef, AnalysisContext<ElementT, VariableT> context) {
    this(Reference.of(variableRef), context);
  }

  @Override
  public Set<NormalExpression> normalise(State<ElementT, VariableT> environment) {
    return Set.of(new NormalExpression(variableRef, 0));
  }

  @Override
  public VariableT evaluate(State<ElementT, VariableT> environment) {
    return environment.getVariableValue(variableRef);
  }

  @Override
  public String toString() {
    return variableRef.toString();
  }

  @Override
  public State<ElementT, VariableT> assign(Expression<ElementT, VariableT> value, State<ElementT, VariableT> state) {
    var normalExpressions = value.normalise(state);
    if (normalExpressions.isEmpty()) {
      return state.assignVariable(variableRef, value.evaluate(state));
    } else {
      return state.assignVariable(variableRef, normalExpressions);
    }
  }
}
