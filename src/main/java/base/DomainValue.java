package base;

public interface DomainValue {
  static DomainValue unreachable() {
    throw new IllegalStateException("Method unreachable() not implemented.");
  }

  static DomainValue unknown() {
    throw new IllegalStateException("Method unreachable() not implemented.");
  }

  DomainValue join(DomainValue other);

  DomainValue meet(DomainValue other);

  DomainValue widen(DomainValue other);

  DomainValue narrow(DomainValue other);
}
