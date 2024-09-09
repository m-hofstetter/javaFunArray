package analysis.common.condition;

import static funarray.BoundRelation.LESS_EQUAL;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;

public final class LessEqualThan<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> extends Condition<ElementT, VariableT> {

  public LessEqualThan(Expression<ElementT, VariableT> left,
                       Expression<ElementT, VariableT> right,
                       AnalysisContext<ElementT, VariableT> context) {
    super(left, right, context, LESS_EQUAL, context.getVariableDomain().lessEqualThan(), "≤");
  }
}
