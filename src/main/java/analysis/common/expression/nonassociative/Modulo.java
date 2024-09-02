package analysis.common.expression.nonassociative;

import abstractdomain.DomainValue;
import abstractdomain.exception.ConcretizationException;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import funarray.NormalExpression;
import funarray.State;
import java.util.Set;

public class Modulo<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> implements Expression<ElementT, VariableT> {

  private final Expression<ElementT, VariableT> dividend;
  private final Expression<ElementT, VariableT> divisor;
  private final AnalysisContext<ElementT, VariableT> context;

  public Modulo(Expression<ElementT, VariableT> dividend, Expression<ElementT, VariableT> divisor, AnalysisContext<ElementT, VariableT> context) {
    this.dividend = dividend;
    this.divisor = divisor;
    this.context = context;
  }

  @Override
  public Set<NormalExpression> normalise(State<ElementT, VariableT> environment) {
    try {
      return Set.of(new NormalExpression("0", context.getVariableDomain().concretize(evaluate(environment))));
    } catch (ConcretizationException e) {
      return Set.of();
    }
  }

  @Override
  public VariableT evaluate(State<ElementT, VariableT> environment) {
    return dividend.evaluate(environment).modulo(divisor.evaluate(environment));
  }

  @Override
  public String toString() {
    return dividend + " % " + divisor;
  }
}
