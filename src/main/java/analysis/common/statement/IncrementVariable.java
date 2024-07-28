package analysis.common.statement;

import abstractdomain.DomainValue;
import analysis.common.Analysis;
import analysis.common.AnalysisResult;
import funarray.EnvState;

/**
 * Atomic statement for incrementing a variable.
 *
 * @param variable    the variable.
 * @param amount      the amount.
 * @param <ElementT>  the domain to abstract array element values with.
 * @param <VariableT> the domain to abstract variable values with.
 */
public record IncrementVariable<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        String variable,
        int amount) implements Analysis<ElementT, VariableT> {

  public static final String PROTOCOL_TEMPLATE = """
          %s ← %s + %s
          \033[0;36m%s\033[0m""";

  @Override
  public AnalysisResult<ElementT, VariableT> run(EnvState<ElementT, VariableT> startingState) {
    var resultState = startingState.addToVariable(variable, amount);
    var protocol = PROTOCOL_TEMPLATE.formatted(variable, variable, amount, resultState);
    return new AnalysisResult<>(resultState, protocol);
  }
}
