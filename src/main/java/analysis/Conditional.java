package analysis;

import base.DomainValue;
import funarray.Environment;
import funarray.Expression;

public interface Conditional<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>> {
  Environment<ELEMENT, VARIABLE> satisfy(Environment<ELEMENT, VARIABLE> state);

  Environment<ELEMENT, VARIABLE> satisfyComplement(Environment<ELEMENT, VARIABLE> state);

  record LessEqualThan<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
          Expression<VARIABLE> left,
          Expression<VARIABLE> right) implements Conditional<ELEMENT, VARIABLE> {

    @Override
    public Environment<ELEMENT, VARIABLE> satisfy(Environment<ELEMENT, VARIABLE> state) {
      return state.satisfyExpressionLessEqualThan(left, right);
    }

    @Override
    public Environment<ELEMENT, VARIABLE> satisfyComplement(Environment<ELEMENT, VARIABLE> state) {
      return state.satisfyExpressionLessThan(right, left);
    }
  }

  record LessThan<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
          Expression<VARIABLE> left,
          Expression<VARIABLE> right) implements Conditional<ELEMENT, VARIABLE> {

    @Override
    public Environment<ELEMENT, VARIABLE> satisfy(Environment<ELEMENT, VARIABLE> state) {
      return state.satisfyExpressionLessThan(left, right);
    }

    @Override
    public Environment<ELEMENT, VARIABLE> satisfyComplement(Environment<ELEMENT, VARIABLE> state) {
      return state.satisfyExpressionLessEqualThan(right, left);
    }
  }
}


