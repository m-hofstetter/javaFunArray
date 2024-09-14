package relation;

import abstractdomain.DomainValue;
import abstractdomain.TriBool;
import funarray.FunArray;
import funarray.NormalExpression;

public class Less<T extends DomainValue<T>> extends StrictInequality<T> {

  public Less() {
    super(T::satisfyLessThan, T::lessThan, Greater::new, GreaterEqual::new);
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
