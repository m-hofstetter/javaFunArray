package analysis.common.expression;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import funarray.EnvState;
import funarray.NormalExpression;
import java.util.Set;

public abstract class Expression<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> {

  protected final AnalysisContext<ElementT, VariableT> context;

  public Expression(AnalysisContext<ElementT, VariableT> context) {
    this.context = context;
  }

  public abstract Set<NormalExpression> normalise(EnvState<ElementT, VariableT> environment);

  public abstract VariableT evaluate(EnvState<ElementT, VariableT> environment);
}
