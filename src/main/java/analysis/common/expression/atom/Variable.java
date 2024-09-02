package analysis.common.expression.atom;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import analysis.common.expression.Assignable;
import analysis.common.expression.Expression;
import funarray.NormalExpression;
import funarray.State;
import java.util.Set;

public record Variable<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(String variableRef,
                                                  AnalysisContext<ElementT, VariableT> context)
        implements Assignable<ElementT, VariableT> {

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
    return variableRef;
  }

  @Override
  public State<ElementT, VariableT> assign(Expression<ElementT, VariableT> value, State<ElementT, VariableT> environmentState) {
    var normalExpressions = value.normalise(environmentState);
    if (normalExpressions.isEmpty()) {
      return environmentState.assignVariable(variableRef, value.evaluate(environmentState));
    } else {
      return environmentState.assignVariable(variableRef, normalExpressions);
    }
  }
}
