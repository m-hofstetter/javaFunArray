package analysis.common.expression.associative;

import abstractdomain.DomainValue;
import abstractdomain.exception.ConcretizationException;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import funarray.NormalExpression;
import funarray.state.State;
import funarray.varref.Reference;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import relation.Relation;

public class Multiplication<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> extends AssociativeOperation<ElementT, VariableT> {

  public Multiplication(Collection<Expression<ElementT, VariableT>> factors,
                        AnalysisContext<ElementT, VariableT> context) {
    super(factors, context);
  }

  @Override
  public Set<NormalExpression> normalise(State<ElementT, VariableT> environment) {
    var values = operands.stream()
            .map(e -> e.evaluate(environment))
            .collect(Collectors.toSet());

    var product = values.stream().reduce(context.getVariableDomain().abstract_(1), DomainValue::multiply);

    try {
      return Set.of(new NormalExpression(Reference.zero(), context.getVariableDomain().concretize(product)));
    } catch (ConcretizationException _) {
      return Set.of();
    }
  }

  @Override
  public VariableT evaluate(State<ElementT, VariableT> environment) {
    return evaluate(environment, VariableT::multiply);
  }

  @Override
  public Set<State<ElementT, VariableT>> satisfy(Expression<ElementT, VariableT> comparand, Relation<VariableT> relation, State<ElementT, VariableT> state) {
    return Set.of(state);
  }

  @Override
  public String toString() {
    return toString(" * ");
  }
}
