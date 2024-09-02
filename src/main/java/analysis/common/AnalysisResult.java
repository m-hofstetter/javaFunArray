package analysis.common;

import abstractdomain.DomainValue;
import funarray.State;

/**
 * The result of a {@link Analysis}.
 *
 * @param resultState the state at the very end of the program.
 * @param protocol    the protocol of every step of the analysis.
 * @param <ElementT>  the domain to abstract array element values with.
 * @param <VariableT> the domain to abstract variable values with.
 */
public record AnalysisResult<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        State<ElementT, VariableT> resultState, String protocol) {
}
