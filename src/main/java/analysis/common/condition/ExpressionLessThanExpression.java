package analysis.common.condition;

import base.DomainValue;
import funarray.Environment;
import funarray.Expression;

public record ExpressionLessThanExpression<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        Expression left,
        Expression right) implements Condition<ELEMENT, VARIABLE> {

  @Override
  public Environment<ELEMENT, VARIABLE> satisfy(Environment<ELEMENT, VARIABLE> input) {
    return input.satisfyExpressionLessThan(left, right);
  }

  @Override
  public Environment<ELEMENT, VARIABLE> satisfyComplement(Environment<ELEMENT, VARIABLE> input) {
    return input.satisfyExpressionLessEqualThan(right, left);
  }

  @Override
  public String toString() {
    return String.format("%s < %s", left, right);
  }
}
