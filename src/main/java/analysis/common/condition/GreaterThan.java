package analysis.common.condition;

import static funarray.BoundRelation.GREATER;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;

public final class GreaterThan<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> extends Condition<ElementT, VariableT> {

  public GreaterThan(Expression<ElementT, VariableT> left,
                     Expression<ElementT, VariableT> right,
                     AnalysisContext<ElementT, VariableT> context) {
    super(left, right, context, GREATER, context.getVariableDomain().greaterThan(), ">");
  }
}
