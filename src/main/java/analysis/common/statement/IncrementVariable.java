package analysis.common.statement;

import analysis.common.AnalysisResult;
import analysis.common.Program;
import base.DomainValue;
import base.infint.InfInt;
import funarray.Environment;
import funarray.VariableReference;

public record IncrementVariable<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        VariableReference variable,
        InfInt amount) implements Program<ELEMENT, VARIABLE> {

  public static final String PROTOCOL_TEMPLATE = """
          %s ← %s + %s
          \033[0;36m%s\033[0m""";

  @Override
  public AnalysisResult<ELEMENT, VARIABLE> run(Environment<ELEMENT, VARIABLE> startingState) {
    var resultState = startingState.addToVariable(variable, amount);
    var protocol = PROTOCOL_TEMPLATE.formatted(variable, variable, amount, resultState);
    return new AnalysisResult<>(resultState, protocol);
  }
}
