package analysis.common.statement;

import analysis.common.Analysis;
import analysis.common.AnalysisResult;
import base.DomainValue;
import base.infint.InfInt;
import funarray.EnvState;
import funarray.VariableReference;

public record IncrementVariable<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        VariableReference variable,
        InfInt amount) implements Analysis<ELEMENT, VARIABLE> {

  public static final String PROTOCOL_TEMPLATE = """
          %s ← %s + %s
          \033[0;36m%s\033[0m""";

  @Override
  public AnalysisResult<ELEMENT, VARIABLE> run(EnvState<ELEMENT, VARIABLE> startingState) {
    var resultState = startingState.addToVariable(variable, amount);
    var protocol = PROTOCOL_TEMPLATE.formatted(variable, variable, amount, resultState);
    return new AnalysisResult<>(resultState, protocol);
  }
}
