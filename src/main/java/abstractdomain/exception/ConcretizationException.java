package abstractdomain.exception;

import abstractdomain.DomainValue;

public class ConcretizationException extends Exception {
  public ConcretizationException(DomainValue<?> domainValue) {
    super("Cannot concretise abstract value %s, since it represents more than one concrete value.".formatted(domainValue));
  }
}
