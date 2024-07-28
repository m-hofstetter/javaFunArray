package analysis.common.expression.atom;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import funarray.EnvState;
import funarray.NormalExpression;
import java.util.Set;

public class Variable<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> extends Expression<ElementT, VariableT> {

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
}
