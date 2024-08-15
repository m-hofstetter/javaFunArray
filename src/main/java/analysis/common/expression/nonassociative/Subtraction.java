package analysis.common.expression.nonassociative;

import abstractdomain.DomainValue;
import abstractdomain.exception.ConcretizationException;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import funarray.EnvState;
import funarray.NormalExpression;
import java.util.Set;
import java.util.stream.Collectors;

public class Subtraction<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> implements Expression<ElementT, VariableT> {

  private final Expression<ElementT, VariableT> minuend;
  private final Expression<ElementT, VariableT> subtrahend;
  private final AnalysisContext<ElementT, VariableT> context;

  public Subtraction(Expression<ElementT, VariableT> minuend, Expression<ElementT, VariableT> subtrahend, AnalysisContext<ElementT, VariableT> context) {
    this.minuend = minuend;
    this.subtrahend = subtrahend;
    this.context = context;
  }

  @Override
  public Set<NormalExpression> normalise(EnvState<ElementT, VariableT> environment) {
    try {
      var concreteSubtrahend = context.getVariableDomain().concretize(subtrahend.evaluate(environment));
      return minuend.normalise(environment).stream()
              .map(e -> new NormalExpression(e.varRef(), e.constant() - concreteSubtrahend))
              .collect(Collectors.toSet());
    } catch (ConcretizationException e) {
      return Set.of();
    }
  }

  @Override
  public VariableT evaluate(EnvState<ElementT, VariableT> environment) {
    return minuend.evaluate(environment).subtract(subtrahend.evaluate(environment));
  }

  @Override
  public String toString() {
    return minuend.toString() + " - " + subtrahend.toString();
  }
}
