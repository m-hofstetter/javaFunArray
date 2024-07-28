package analysis.common.statement;

import abstractdomain.DomainValue;
import analysis.common.Analysis;
import analysis.common.AnalysisContext;
import analysis.common.AnalysisResult;
import funarray.EnvState;
import funarray.NormalExpression;

/**
 * Atomic statement for assigning the value of an array element to a variable.
 *
 * @param arrayIndex                       the index of the array element.
 * @param variable                         the variable.
 * @param elementValueToVariableConversion a translation function from the variable to the array
 *                                         element domain.
 * @param <ElementT>                       the domain to abstract array element values with.
 * @param <VariableT>                      the domain to abstract variable values with.
 */
public record AssignArrayElementValueToVariable<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        String arrRef,
        NormalExpression arrayIndex,
        String varRef,
        AnalysisContext<ElementT, VariableT> context)
        implements Analysis<ElementT, VariableT> {

  public static final String PROTOCOL_TEMPLATE = """
          %s ← A[%s]
          \033[0;36m%s\033[0m""";

  @Override
  public AnalysisResult<ElementT, VariableT> run(EnvState<ElementT, VariableT> startingState) {
    var arrayElementValue = startingState.getArrayElement(arrRef, arrayIndex);
    var resultState = startingState.assignVariable(
            varRef,
            arrRef,
            context.convertArrayElementValueToVariableValue(arrayElementValue)
    );
    var protocol = PROTOCOL_TEMPLATE.formatted(varRef, arrayIndex, resultState);
    return new AnalysisResult<>(resultState, protocol);
  }
}
