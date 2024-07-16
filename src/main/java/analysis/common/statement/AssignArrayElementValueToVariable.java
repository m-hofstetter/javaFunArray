package analysis.common.statement;

import analysis.common.AnalysisResult;
import analysis.common.Program;
import base.DomainValue;
import funarray.Environment;
import funarray.Expression;
import funarray.Variable;
import java.util.function.Function;

public record AssignArrayElementValueToVariable<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        Expression<VARIABLE> arrayIndex,
        Variable<VARIABLE> variable,
        Function<ELEMENT, VARIABLE> elementValueToVariableConversion) implements Program<ELEMENT, VARIABLE> {

  @Override
  public AnalysisResult<ELEMENT, VARIABLE> run(Environment<ELEMENT, VARIABLE> startingState) {
    var arrayElementValue = startingState.getArrayElement(arrayIndex);
    var resultState = startingState.assignVariable(variable, elementValueToVariableConversion.apply(arrayElementValue));
    var protocol = "%s ← A[%s]".formatted(variable, arrayIndex);
    return new AnalysisResult<>(resultState, protocol);
  }
}
