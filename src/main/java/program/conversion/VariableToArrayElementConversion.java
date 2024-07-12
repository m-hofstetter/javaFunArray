package program.conversion;

import base.DomainValue;

@FunctionalInterface
public interface VariableToArrayElementConversion<ELEMENT_VALUE_DOMAIN extends DomainValue<ELEMENT_VALUE_DOMAIN>, VARIABLE_DOMAIN extends DomainValue<VARIABLE_DOMAIN>> {
  ELEMENT_VALUE_DOMAIN convert(VARIABLE_DOMAIN value);
}
