package relation;

import abstractdomain.DomainValue;
import abstractdomain.TriBool;
import funarray.FunArray;
import funarray.NormalExpression;

public class LessEqual<T extends DomainValue<T>> extends NonStrictInequality<T> {

  public LessEqual() {
    super(T::satisfyLessEqualThan, T::lessEqualThan, GreaterEqual::new, Greater::new);
  }

  @Override
  public <ElementT extends DomainValue<ElementT>> FunArray<ElementT> satisfy(FunArray<ElementT> before, NormalExpression left, NormalExpression right) {
    return satisfyOrder(before, left, right);
  }

  @Override
  public <ElementT extends DomainValue<ElementT>> TriBool isSatisfied(FunArray<ElementT> array, NormalExpression left, NormalExpression right) {
    return isOrderSatisfied(array, left, right);
  }
}
