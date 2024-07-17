package analysis.common.statement;

import analysis.common.AnalysisResult;
import analysis.common.Program;
import base.DomainValue;
import funarray.Environment;
import funarray.Expression;

public record AssignArrayElementValueToArrayElement<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        Expression arrayIndexSource,
        Expression arrayIndexTarget) implements Program<ELEMENT, VARIABLE> {
  public static final String PROTOCOL_TEMPLATE = """
          A[%s] ← A[%s]
          \033[0;36m%s\033[0m""";

  @Override
  public AnalysisResult<ELEMENT, VARIABLE> run(Environment<ELEMENT, VARIABLE> startingState) {
    var resultState = startingState.assignArrayElement(arrayIndexTarget, startingState.getArrayElement(arrayIndexSource));
    var protocol = PROTOCOL_TEMPLATE.formatted(arrayIndexTarget, arrayIndexSource, resultState);
    return new AnalysisResult<>(resultState, protocol);
  }
}
