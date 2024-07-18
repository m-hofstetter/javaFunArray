package analysis.common.condition;

import base.DomainValue;
import funarray.EnvState;

public interface Condition<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>> {
  EnvState<ELEMENT, VARIABLE> satisfy(EnvState<ELEMENT, VARIABLE> state);

  EnvState<ELEMENT, VARIABLE> satisfyComplement(EnvState<ELEMENT, VARIABLE> state);
}
