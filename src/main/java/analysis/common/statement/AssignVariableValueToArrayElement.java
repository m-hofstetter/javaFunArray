package analysis.common.statement;

import analysis.common.AnalysisResult;
import analysis.common.Program;
import base.DomainValue;
import funarray.Environment;
import funarray.Expression;
import funarray.Variable;
import java.util.function.Function;

public record AssignVariableValueToArrayElement<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        Expression<VARIABLE> arrayIndex,
        Variable<VARIABLE> variable,
        Function<VARIABLE, ELEMENT> variableToElementValueConversion) implements Program<ELEMENT, VARIABLE> {

  public static final String PROTOCOL_TEMPLATE = """
          A[%s] ← %s
          \033[0;36m%s\033[0m""";

  @Override
  public AnalysisResult<ELEMENT, VARIABLE> run(Environment<ELEMENT, VARIABLE> startingState) {
    var value = startingState.getVariable(variable().name()).value();
    var resultState = startingState.assignArrayElement(arrayIndex, variableToElementValueConversion.apply(value));
    var protocol = PROTOCOL_TEMPLATE.formatted(arrayIndex, variable, resultState);
    return new AnalysisResult<>(resultState, protocol);
  }
}
