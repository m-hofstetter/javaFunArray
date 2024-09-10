package abstractdomain;

import abstractdomain.exception.ConcretizationException;

public interface Domain<T extends DomainValue<T>> {
  T abstract_(long concreteValue);

  long concretize(T abstractValue) throws ConcretizationException;

  T getUnknown();

  T getUnreachable();

  T getZeroValue();

  ValueRelation<T> lessThan();

  ValueRelation<T> lessEqualThan();

  ValueRelation<T> greaterThan();

  ValueRelation<T> greaterEqualThan();

  ValueRelation<T> equalTo();

  ValueRelation<T> unequalTo();

}
