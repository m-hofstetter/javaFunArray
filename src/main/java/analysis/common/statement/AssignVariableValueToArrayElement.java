package analysis.common.statement;

import abstractdomain.DomainValue;
import analysis.common.Analysis;
import analysis.common.AnalysisContext;
import analysis.common.AnalysisResult;
import funarray.EnvState;
import funarray.NormalExpression;

/**
 * Atomic statement for assigning the value of a variable to an array element.
 *
 * @param variable                         the variable.
 * @param arrayIndex                       the target index.
 * @param variableToElementValueConversion a translation function from the variable to the array
 *                                         element domain.
 * @param <ElementT>                       the domain to abstract array element values with.
 * @param <VariableT>                      the domain to abstract variable values with.
 */
public record AssignVariableValueToArrayElement<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        String arrRef,
        NormalExpression arrayIndex,
        String varRef,
        AnalysisContext<ElementT, VariableT> context)
        implements Analysis<ElementT, VariableT> {

  public static final String PROTOCOL_TEMPLATE = """
          A[%s] ← %s
          \033[0;36m%s\033[0m""";

  @Override
  public AnalysisResult<ElementT, VariableT> run(EnvState<ElementT, VariableT> startingState) {
    var value = startingState.getVariableValue(varRef);
    var resultState = startingState.assignArrayElement(
            arrRef,
            arrayIndex,
            context.convertVariableValueToArrayElementValue(value)
    );
    var protocol = PROTOCOL_TEMPLATE.formatted(arrayIndex, varRef, resultState);
    return new AnalysisResult<>(resultState, protocol);
  }
}
