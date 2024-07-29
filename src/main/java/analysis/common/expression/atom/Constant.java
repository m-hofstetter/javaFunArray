package analysis.common.expression.atom;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import funarray.EnvState;
import funarray.NormalExpression;
import java.util.Set;
import lombok.Getter;

@Getter
public class Constant<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> implements Expression<ElementT, VariableT> {

  private final AnalysisContext<ElementT, VariableT> context;
  private final int constant;

  public Constant(int constant, AnalysisContext<ElementT, VariableT> context) {
    this.context = context;
    this.constant = constant;
  }

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
