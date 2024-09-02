package analysis.common;

import abstractdomain.DomainValue;
import funarray.State;

/**
 * An abstract interpretation analysis utilising FunArrays.
 *
 * @param <ElementT>  the domain to abstract array element values with.
 * @param <VariableT> the domain to abstract variable values with.
 */
public interface Analysis<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> {
  AnalysisResult<ElementT, VariableT> run(State<ElementT, VariableT> startingState);
}
