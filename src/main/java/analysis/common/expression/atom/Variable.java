package analysis.common.expression.atom;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import analysis.common.expression.Assignable;
import analysis.common.expression.Expression;
import funarray.EnvState;
import funarray.NormalExpression;
import java.util.Set;

public class Variable<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>
        extends Expression<ElementT, VariableT>
        implements Assignable<ElementT, VariableT> {

  private final String variableRef;

  public Variable(String variableRef, AnalysisContext<ElementT, VariableT> context) {
    super(context);
    this.variableRef = variableRef;
  }

  @Override
  public Set<NormalExpression> normalise(EnvState<ElementT, VariableT> environment) {
    return Set.of(new NormalExpression(variableRef, 0));
  }

  @Override
  public VariableT evaluate(EnvState<ElementT, VariableT> environment) {
    return environment.getVariableValue(variableRef);
  }

  @Override
  public String toString() {
    return variableRef;
  }

  @Override
  public EnvState<ElementT, VariableT> assign(Expression<ElementT, VariableT> value, EnvState<ElementT, VariableT> environmentState) {
    var normalExpressions = value.normalise(environmentState);
    if (normalExpressions.isEmpty()) {
      return environmentState.assignVariable(variableRef, value.evaluate(environmentState));
    } else {
      return environmentState.assignVariable(variableRef, normalExpressions);
    }
  }
}
