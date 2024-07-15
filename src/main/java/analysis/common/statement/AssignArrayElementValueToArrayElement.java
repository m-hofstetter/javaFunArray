package analysis.common.statement;

import analysis.common.Program;
import base.DomainValue;
import funarray.Environment;
import funarray.Expression;

public record AssignArrayElementValueToArrayElement<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        Expression<VARIABLE> arrayIndexSource,
        Expression<VARIABLE> arrayIndexTarget) implements Program<ELEMENT, VARIABLE> {

  @Override
  public Environment<ELEMENT, VARIABLE> run(Environment<ELEMENT, VARIABLE> startingState) {
    return startingState.assignArrayElement(arrayIndexTarget, startingState.getArrayElement(arrayIndexSource));
  }
}
