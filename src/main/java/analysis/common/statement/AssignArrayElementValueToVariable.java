package analysis.common.statement;

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
  public Environment<ELEMENT, VARIABLE> run(Environment<ELEMENT, VARIABLE> startingState) {
    var arrayElementValue = startingState.getArrayElement(arrayIndex);
    return startingState.assignVariable(variable, elementValueToVariableConversion.apply(arrayElementValue));
  }
}
