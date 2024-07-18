package analysis.common;

import base.DomainValue;
import funarray.EnvState;

public interface Analysis<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>> {
  AnalysisResult<ELEMENT, VARIABLE> run(EnvState<ELEMENT, VARIABLE> startingState);
}
