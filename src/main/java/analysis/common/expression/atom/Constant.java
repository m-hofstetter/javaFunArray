package analysis.common.expression.atom;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import funarray.NormalExpression;
import funarray.State;
import java.util.Set;

public record Constant<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        int constant,
        AnalysisContext<ElementT, VariableT> context
) implements Expression<ElementT, VariableT> {

  @Override
  public Set<NormalExpression> normalise(State<ElementT, VariableT> environment) {
    return Set.of();
  }

  @Override
  public VariableT evaluate(State<ElementT, VariableT> environment) {
    return context.getVariableDomain().abstract_(constant);
  }

  @Override
  public String toString() {
    return Integer.toString(constant);
  }
}
