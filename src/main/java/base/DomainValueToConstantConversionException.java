package base;

public class DomainValueToConstantConversionException extends Exception {
  public DomainValueToConstantConversionException(DomainValue<?> domainValue) {
    super("Cannot convert domain value %s to constant.".formatted(domainValue));
  }
}
