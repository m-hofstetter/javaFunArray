package analysis.common.statement;

import analysis.common.Analysis;
import analysis.common.AnalysisResult;
import base.DomainValue;
import funarray.EnvState;
import funarray.Expression;
import funarray.VariableReference;
import java.util.function.Function;

public record AssignArrayElementValueToVariable<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        Expression arrayIndex,
        VariableReference variable,
        Function<ElementT, VariableT> elementValueToVariableConversion)
        implements Analysis<ElementT, VariableT> {

  public static final String PROTOCOL_TEMPLATE = """
          %s ← A[%s]
          \033[0;36m%s\033[0m""";

  @Override
  public AnalysisResult<ElementT, VariableT> run(EnvState<ElementT, VariableT> startingState) {
    var arrayElementValue = startingState.getArrayElement(arrayIndex);
    var resultState = startingState.assignVariable(
            variable,
            elementValueToVariableConversion.apply(arrayElementValue)
    );
    var protocol = PROTOCOL_TEMPLATE.formatted(variable, arrayIndex, resultState);
    return new AnalysisResult<>(resultState, protocol);
  }
}
