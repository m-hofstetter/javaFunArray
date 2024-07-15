package analysis.common.statement;

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

  @Override
  public Environment<ELEMENT, VARIABLE> run(Environment<ELEMENT, VARIABLE> startingState) {
    var value = startingState.getVariable(variable().name()).value();
    return startingState.assignArrayElement(arrayIndex, variableToElementValueConversion.apply(value));
  }
}
