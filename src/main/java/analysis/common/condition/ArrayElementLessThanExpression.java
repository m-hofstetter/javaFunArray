package analysis.common.condition;

import base.DomainValue;
import funarray.Environment;
import funarray.Expression;
import java.util.function.Function;

public record ArrayElementLessThanExpression<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        Expression index, Expression comparand,
        Function<VARIABLE, ELEMENT> valueConversion) implements Condition<ELEMENT, VARIABLE> {

  @Override
  public Environment<ELEMENT, VARIABLE> satisfy(Environment<ELEMENT, VARIABLE> input) {
    var value = input.getArrayElement(index);
    value = value.satisfyLessThan(valueConversion.apply(input.calculateExpression(comparand)));
    return input.assignArrayElement(index, value);
  }

  @Override
  public Environment<ELEMENT, VARIABLE> satisfyComplement(Environment<ELEMENT, VARIABLE> input) {
    var value = input.getArrayElement(index);
    value = value.satisfyGreaterEqualThan(valueConversion.apply(input.calculateExpression(comparand)));
    return input.assignArrayElement(index, value);
  }

  @Override
  public String toString() {
    return String.format("A[%s] < %s", index, comparand);
  }
}
