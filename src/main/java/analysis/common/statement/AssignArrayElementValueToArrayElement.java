package analysis.common.statement;

import abstractdomain.DomainValue;
import analysis.common.Analysis;
import analysis.common.AnalysisResult;
import funarray.EnvState;
import funarray.NormalExpression;

/**
 * Atomic statement for assigning the value of an array element to another element.
 *
 * @param arrayIndexSource the index of the source array element.
 * @param arrayIndexTarget the index of the target.
 * @param <ElementT>       the domain to abstract array element values with.
 * @param <VariableT>      the domain to abstract variable values with.
 */
public record AssignArrayElementValueToArrayElement<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        String arrRefSource,
        NormalExpression arrayIndexSource,
        String arrRefTarget,
        NormalExpression arrayIndexTarget) implements Analysis<ElementT, VariableT> {
  public static final String PROTOCOL_TEMPLATE = """
          A[%s] ← A[%s]
          \033[0;36m%s\033[0m""";

  @Override
  public AnalysisResult<ElementT, VariableT> run(EnvState<ElementT, VariableT> startingState) {
    var resultState = startingState.assignArrayElement(
            arrRefTarget,
            arrayIndexTarget,
            startingState.getArrayElement(arrRefSource, arrayIndexSource)
    );
    var protocol = PROTOCOL_TEMPLATE.formatted(arrayIndexTarget, arrayIndexSource, resultState);
    return new AnalysisResult<>(resultState, protocol);
  }
}
