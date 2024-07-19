package analysis.common.statement;

import analysis.common.Analysis;
import analysis.common.AnalysisResult;
import base.DomainValue;
import funarray.EnvState;
import funarray.VariableReference;

public record IncrementVariable<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        VariableReference variable,
        int amount) implements Analysis<ElementT, VariableT> {

  public static final String PROTOCOL_TEMPLATE = """
          %s ← %s + %s
          \033[0;36m%s\033[0m""";

  @Override
  public AnalysisResult<ElementT, VariableT> run(EnvState<ElementT, VariableT> startingState) {
    var resultState = startingState.addToVariable(variable, amount);
    var protocol = PROTOCOL_TEMPLATE.formatted(variable, variable, amount, resultState);
    return new AnalysisResult<>(resultState, protocol);
  }
}
