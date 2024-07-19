package analysis.common;

import base.DomainValue;
import funarray.EnvState;

public interface Analysis<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> {
  AnalysisResult<ElementT, VariableT> run(EnvState<ElementT, VariableT> startingState);
}
