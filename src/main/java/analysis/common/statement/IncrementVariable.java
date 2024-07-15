package analysis.common.statement;

import analysis.common.Program;
import base.DomainValue;
import base.infint.InfInt;
import funarray.Environment;
import funarray.Variable;

public record IncrementVariable<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        Variable<VARIABLE> variable,
        InfInt amount) implements Program<ELEMENT, VARIABLE> {

  @Override
  public Environment<ELEMENT, VARIABLE> run(Environment<ELEMENT, VARIABLE> startingState) {
    return startingState.addToVariable(variable, amount);
  }
}
