package analysis.common.statement;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import analysis.common.expression.Assignable;
import analysis.common.expression.associative.Addition;
import analysis.common.expression.atom.Constant;
import java.util.Set;

public class Increment<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> extends Assign<ElementT, VariableT> {

  public Increment(Assignable<ElementT, VariableT> target, AnalysisContext<ElementT, VariableT> context) {
    super(new Addition<>(Set.of(target, new Constant<>(1, context)), context), target);
  }
}
