package base;

import base.infint.InfInt;

public interface DomainValue<T extends DomainValue<T>> {
  T join(T other);
  T meet(T other);
  T widen(T other);
  T narrow(T other);

  T add(T other);
  T subtract(T other);
  T addConstant(InfInt constant);
  T subtractConstant(InfInt constant);
  T inverse();

  T satisfyLessEqualThan(T other);

  T satisfyGreaterEqualThan(T other);

  T satisfyLessThan(T other);

  T satisfyGreaterThan(T other);

  T satisfyEqual(T other);

  T satisfyNotEqual(T other);
}
