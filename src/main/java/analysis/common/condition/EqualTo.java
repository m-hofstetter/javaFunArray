package analysis.common.condition;

import static funarray.BoundRelation.EQUAL;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;

public final class EqualTo<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> extends Condition<ElementT, VariableT> {

  public EqualTo(Expression<ElementT, VariableT> left,
                 Expression<ElementT, VariableT> right,
                 AnalysisContext<ElementT, VariableT> context) {
    super(left, right, context, EQUAL, context.getVariableDomain().equalTo(), "=");
  }
}
