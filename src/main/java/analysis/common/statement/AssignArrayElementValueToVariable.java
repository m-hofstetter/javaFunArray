package analysis.common.statement;

import analysis.common.Analysis;
import analysis.common.AnalysisResult;
import base.DomainValue;
import funarray.Environment;
import funarray.Expression;
import funarray.VariableReference;
import java.util.function.Function;

public record AssignArrayElementValueToVariable<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        Expression arrayIndex,
        VariableReference variable,
        Function<ELEMENT, VARIABLE> elementValueToVariableConversion) implements Analysis<ELEMENT, VARIABLE> {

  public static final String PROTOCOL_TEMPLATE = """
          %s ← A[%s]
          \033[0;36m%s\033[0m""";

  @Override
  public AnalysisResult<ELEMENT, VARIABLE> run(Environment<ELEMENT, VARIABLE> startingState) {
    var arrayElementValue = startingState.getArrayElement(arrayIndex);
    var resultState = startingState.assignVariable(variable, elementValueToVariableConversion.apply(arrayElementValue));
    var protocol = PROTOCOL_TEMPLATE.formatted(variable, arrayIndex, resultState);
    return new AnalysisResult<>(resultState, protocol);
  }
}
