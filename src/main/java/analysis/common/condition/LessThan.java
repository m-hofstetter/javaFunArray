package analysis.common.condition;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import relation.Less;

public final class LessThan<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> extends Condition<ElementT, VariableT> {

  public LessThan(Expression<ElementT, VariableT> left,
                  Expression<ElementT, VariableT> right,
                  AnalysisContext<ElementT, VariableT> context) {
    super(left, right, context, new Less<>(), "<");
  }
}
