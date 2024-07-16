package analysis.common;

import base.DomainValue;
import funarray.Environment;

public interface Program<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>> {
  AnalysisResult<ELEMENT, VARIABLE> run(Environment<ELEMENT, VARIABLE> startingState);
}
