package analysis.common.condition;

import static funarray.BoundRelation.GREATER_EQUAL;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;

public final class GreaterEqualThan<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> extends Condition<ElementT, VariableT> {

  public GreaterEqualThan(Expression<ElementT, VariableT> left,
                          Expression<ElementT, VariableT> right,
                          AnalysisContext<ElementT, VariableT> context) {
    super(left, right, context, GREATER_EQUAL, context.getVariableDomain().greaterEqualThan(), "≥");
  }
}
