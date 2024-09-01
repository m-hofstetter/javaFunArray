package analysis.common.expression.associative;

import abstractdomain.DomainValue;
import abstractdomain.exception.ConcretizationException;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import funarray.EnvState;
import funarray.NormalExpression;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class Multiplication<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> extends AssociativeOperation<ElementT, VariableT> {

  public Multiplication(Collection<Expression<ElementT, VariableT>> factors,
                        AnalysisContext<ElementT, VariableT> context) {
    super(factors, context);
  }

  @Override
  public Set<NormalExpression> normalise(EnvState<ElementT, VariableT> environment) {
    var values = operands.stream()
            .map(e -> e.evaluate(environment))
            .collect(Collectors.toSet());

    var product = values.stream().reduce(context.getVariableDomain().abstract_(1), DomainValue::multiply);

    try {
      return Set.of(new NormalExpression("0", context.getVariableDomain().concretize(product)));
    } catch (ConcretizationException _) {
      return Set.of();
    }
  }

  @Override
  public VariableT evaluate(EnvState<ElementT, VariableT> environment) {
    return evaluate(environment, VariableT::multiply);
  }

  @Override
  public String toString() {
    return toString(" * ");
  }
}
