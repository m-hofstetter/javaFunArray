package relation;

import abstractdomain.DomainValue;
import abstractdomain.TriBool;
import funarray.FunArray;
import funarray.NormalExpression;

public class GreaterEqual<T extends DomainValue<T>> extends NonStrictInequality<T> {

  public GreaterEqual() {
    super(T::satisfyGreaterEqualThan, T::greaterEqualThan, LessEqual::new, Less::new);
  }

  @Override
  public <ElementT extends DomainValue<ElementT>> FunArray<ElementT> satisfy(FunArray<ElementT> before, NormalExpression left, NormalExpression right) {
    return satisfyOrder(before, right, left);
  }

  @Override
  public <ElementT extends DomainValue<ElementT>> TriBool isSatisfied(FunArray<ElementT> array, NormalExpression left, NormalExpression right) {
    return isOrderSatisfied(array, right, left);
  }
}
