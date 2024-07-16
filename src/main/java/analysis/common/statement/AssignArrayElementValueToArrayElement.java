package analysis.common.statement;

import analysis.common.AnalysisResult;
import analysis.common.Program;
import base.DomainValue;
import funarray.Environment;
import funarray.Expression;

public record AssignArrayElementValueToArrayElement<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        Expression<VARIABLE> arrayIndexSource,
        Expression<VARIABLE> arrayIndexTarget) implements Program<ELEMENT, VARIABLE> {

  @Override
  public AnalysisResult<ELEMENT, VARIABLE> run(Environment<ELEMENT, VARIABLE> startingState) {
    var resultState = startingState.assignArrayElement(arrayIndexTarget, startingState.getArrayElement(arrayIndexSource));
    var protocol = "A[%s] ← A[%s]".formatted(arrayIndexTarget, arrayIndexSource);
    return new AnalysisResult<>(resultState, protocol);
  }
}
