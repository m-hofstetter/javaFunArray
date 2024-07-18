package analysis.common;

import base.DomainValue;
import funarray.EnvState;

public record AnalysisResult<ElementT extends DomainValue<ElementT>, VariableT extends DomainValue<VariableT>>(
        EnvState<ElementT, VariableT> resultState, String protocol) {
}
