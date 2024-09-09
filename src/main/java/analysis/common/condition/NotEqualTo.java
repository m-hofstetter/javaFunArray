package analysis.common.condition;

import static funarray.BoundRelation.NOT_EQUAL;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;

public final class NotEqualTo<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> extends Condition<ElementT, VariableT> {

  public NotEqualTo(Expression<ElementT, VariableT> left,
                    Expression<ElementT, VariableT> right,
                    AnalysisContext<ElementT, VariableT> context) {
    super(left, right, context, NOT_EQUAL, context.getVariableDomain().unequalTo(), "≠");
  }
}
