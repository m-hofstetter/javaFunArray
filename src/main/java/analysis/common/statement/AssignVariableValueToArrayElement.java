package analysis.common.statement;

import analysis.common.Analysis;
import analysis.common.AnalysisResult;
import base.DomainValue;
import funarray.EnvState;
import funarray.Expression;
import funarray.VariableReference;
import java.util.function.Function;

public record AssignVariableValueToArrayElement<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        Expression arrayIndex,
        VariableReference variable,
        Function<VariableT, ElementT> variableToElementValueConversion)
        implements Analysis<ElementT, VariableT> {

  public static final String PROTOCOL_TEMPLATE = """
          A[%s] ← %s
          \033[0;36m%s\033[0m""";

  @Override
  public AnalysisResult<ElementT, VariableT> run(EnvState<ElementT, VariableT> startingState) {
    var value = startingState.getVariableValue(variable);
    var resultState = startingState.assignArrayElement(
            arrayIndex,
            variableToElementValueConversion.apply(value)
    );
    var protocol = PROTOCOL_TEMPLATE.formatted(arrayIndex, variable, resultState);
    return new AnalysisResult<>(resultState, protocol);
  }
}
