package analysis.common.statement;

import abstractdomain.DomainValue;
import analysis.common.Analysis;
import analysis.common.AnalysisContext;
import analysis.common.AnalysisResult;
import analysis.common.expression.Assignable;
import analysis.common.expression.Expression;
import funarray.EnvState;

public record Assign<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        Expression<ElementT, VariableT> source,
        Assignable<ElementT, VariableT> target,
        AnalysisContext<ElementT, VariableT> context
) implements Analysis<ElementT, VariableT> {

  public static final String PROTOCOL_TEMPLATE = """
          %s ← %s
          \033[0;36m%s\033[0m""";

  @Override
  public AnalysisResult<ElementT, VariableT> run(EnvState<ElementT, VariableT> startingState) {
    var modifiedState = target.assign(source, startingState);
    var protocol = PROTOCOL_TEMPLATE.formatted(target, source, modifiedState);
    return new AnalysisResult<>(modifiedState, protocol);
  }

}
