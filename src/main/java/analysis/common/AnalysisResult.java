package analysis.common;

import base.DomainValue;
import funarray.EnvState;

public record AnalysisResult<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        EnvState<ELEMENT, VARIABLE> resultState, String protocol) {
}
