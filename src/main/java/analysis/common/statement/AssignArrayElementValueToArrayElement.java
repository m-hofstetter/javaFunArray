package analysis.common.statement;

import analysis.common.Analysis;
import analysis.common.AnalysisResult;
import base.DomainValue;
import funarray.EnvState;
import funarray.Expression;

public record AssignArrayElementValueToArrayElement<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        Expression arrayIndexSource,
        Expression arrayIndexTarget) implements Analysis<ElementT, VariableT> {
  public static final String PROTOCOL_TEMPLATE = """
          A[%s] ← A[%s]
          \033[0;36m%s\033[0m""";

  @Override
  public AnalysisResult<ElementT, VariableT> run(EnvState<ElementT, VariableT> startingState) {
    var resultState = startingState.assignArrayElement(
            arrayIndexTarget,
            startingState.getArrayElement(arrayIndexSource)
    );
    var protocol = PROTOCOL_TEMPLATE.formatted(arrayIndexTarget, arrayIndexSource, resultState);
    return new AnalysisResult<>(resultState, protocol);
  }
}
