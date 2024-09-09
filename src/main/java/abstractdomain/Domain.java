package abstractdomain;

import abstractdomain.exception.ConcretizationException;

public interface Domain<T extends DomainValue<T>> {
  T abstract_(long concreteValue);

  long concretize(T abstractValue) throws ConcretizationException;

  T getUnknown();

  T getUnreachable();

  T getZeroValue();

  Relation<T> lessThan();

  Relation<T> lessEqualThan();

  Relation<T> greaterThan();

  Relation<T> greaterEqualThan();

  Relation<T> equalTo();

  Relation<T> unequalTo();

}
