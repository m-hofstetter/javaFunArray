package analysis.common.statement;

import analysis.common.AnalysisResult;
import analysis.common.Program;
import base.DomainValue;
import base.infint.InfInt;
import funarray.Environment;
import funarray.Variable;

public record IncrementVariable<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        Variable<VARIABLE> variable,
        InfInt amount) implements Program<ELEMENT, VARIABLE> {

  @Override
  public AnalysisResult<ELEMENT, VARIABLE> run(Environment<ELEMENT, VARIABLE> startingState) {
    var resultState = startingState.addToVariable(variable, amount);
    var protocol = "%s ← %s + %s\n".formatted(variable, variable, amount);
    return new AnalysisResult<>(resultState, protocol);
  }
}
