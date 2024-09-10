package analysis.common.expression.atom;

import abstractdomain.DomainValue;
import abstractdomain.ValueRelation;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import funarray.NormalExpression;
import funarray.state.State;
import funarray.varref.Reference;
import java.util.Set;

public record Constant<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        int constant,
        AnalysisContext<ElementT, VariableT> context
) implements Expression<ElementT, VariableT> {

  @Override
  public Set<NormalExpression> normalise(State<ElementT, VariableT> environment) {
    return Set.of(new NormalExpression(Reference.zero(), constant));
  }

  @Override
  public VariableT evaluate(State<ElementT, VariableT> environment) {
    return context.getVariableDomain().abstract_(constant);
  }

  @Override
  public Set<State<ElementT, VariableT>> satisfy(
          Expression<ElementT, VariableT> comparand,
          ValueRelation<VariableT> relation,
          State<ElementT, VariableT> state
  ) {
    return switch (relation.isSatisfied(this.evaluate(state), comparand.evaluate(state))) {
      case TRUE, UNKNOWN -> Set.of(state);
      case FALSE -> Set.of();
    };
  }

  @Override
  public String toString() {
    return Integer.toString(constant);
  }
}
