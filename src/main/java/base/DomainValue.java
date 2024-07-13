package base;

public interface DomainValue<T extends DomainValue<T>> {
  T join(T other);
  T meet(T other);
  T widen(T other);
  T narrow(T other);
}
