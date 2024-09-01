package analysis.common.expression.atom;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import funarray.EnvState;
import funarray.NormalExpression;
import java.util.Set;
import lombok.Getter;

@Getter
public record Constant<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        int constant,
        AnalysisContext<ElementT, VariableT> context
) implements Expression<ElementT, VariableT> {

  @Override
  public Set<NormalExpression> normalise(EnvState<ElementT, VariableT> environment) {
    return Set.of();
  }

  @Override
  public VariableT evaluate(EnvState<ElementT, VariableT> environment) {
    return context.getVariableDomain().abstract_(constant);
  }

  @Override
  public String toString() {
    return Integer.toString(constant);
  }
}
