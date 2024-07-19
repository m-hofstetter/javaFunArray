package analysis.common.statement;

import analysis.common.Analysis;
import analysis.common.AnalysisResult;
import base.DomainValue;
import funarray.EnvState;
import funarray.Expression;
import funarray.VariableReference;
import java.util.function.Function;

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
