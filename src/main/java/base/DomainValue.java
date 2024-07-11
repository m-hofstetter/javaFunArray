package base;

public interface DomainValue<T> {
  static <T> T unreachable() {
    throw new IllegalStateException("Method unreachable() not implemented.");
  }

  static <T> T unknown() {
    throw new IllegalStateException("Method unreachable() not implemented.");
  }

  T join(T other);

  T meet(T other);

  T widen(T other);

  T narrow(T other);
}
