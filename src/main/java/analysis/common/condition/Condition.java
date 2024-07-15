package analysis.common.condition;

import base.DomainValue;
import funarray.Environment;

public interface Condition<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>> {
  Environment<ELEMENT, VARIABLE> satisfy(Environment<ELEMENT, VARIABLE> input);

  Environment<ELEMENT, VARIABLE> satisfyComplement(Environment<ELEMENT, VARIABLE> input);
}
