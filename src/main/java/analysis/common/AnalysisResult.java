package analysis.common;

import base.DomainValue;
import funarray.Environment;

public record AnalysisResult<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        Environment<ELEMENT, VARIABLE> resultState, String protocol) {
}
