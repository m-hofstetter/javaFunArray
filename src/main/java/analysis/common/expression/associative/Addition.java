package analysis.common.expression.associative;

import abstractdomain.DomainValue;
import abstractdomain.exception.ConcretizationException;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import funarray.EnvState;
import funarray.NormalExpression;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Addition<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> extends AssociativeOperation<ElementT, VariableT> {

  public Addition(Set<Expression<ElementT, VariableT>> summands,
                  AnalysisContext<ElementT, VariableT> context) {
    super(summands, context);
  }

  @Override
  public Set<NormalExpression> normalise(EnvState<ElementT, VariableT> environment) {
    return operands.stream().flatMap(operand -> {
      var sumOfOthers = operands.stream()
              .filter(e -> !e.equals(operand))
              .map(e -> e.evaluate(environment))
              .reduce(context.getVariableDomain().abstract_(0), VariableT::add);

      try {
        var concreteSum = context.getVariableDomain().concretize(sumOfOthers);
        return operand.normalise(environment).stream()
                .map(e -> new NormalExpression(e.varRef(), e.constant() + concreteSum));
      } catch (ConcretizationException e) {
        return Stream.empty();
      }
    }).collect(Collectors.toSet());
  }

  @Override
  public VariableT evaluate(EnvState<ElementT, VariableT> environment) {
    return evaluate(environment, VariableT::add);
  }

  @Override
  public String toString() {
    return toString(" + ");
  }
}
