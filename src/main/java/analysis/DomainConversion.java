package analysis;

import base.DomainValue;

@FunctionalInterface
public interface DomainConversion<FROM extends DomainValue<FROM>, TO extends DomainValue<TO>> {
  TO convert(FROM value);
}
