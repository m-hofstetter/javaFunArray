package analysis.common.controlstructure;

import static analysis.common.AnalysisResult.AssertionResult.noAssert;

import abstractdomain.DomainValue;
import analysis.common.Analysis;
import analysis.common.AnalysisResult;
import funarray.State;
import java.util.ArrayList;
import java.util.List;

/**
 * A control structure that executes multiple statements subsequently.
 *
 * @param statements  a list of the statements to execute in order.
 * @param <ElementT>  the domain to abstract array element values with.
 * @param <VariableT> the domain to abstract variable values with.
 */
public record Block<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        List<Analysis<VariableT, ElementT>> statements) implements Analysis<VariableT, ElementT> {

  public Block {
    statements = List.copyOf(statements);
  }

  @Override
  public AnalysisResult<VariableT, ElementT> run(State<VariableT, ElementT> startingState) {
    var protocolSteps = new ArrayList<String>();
    var assertions = noAssert();
    for (Analysis<VariableT, ElementT> s : statements) {
      var stepResult = s.run(startingState);
      protocolSteps.add(stepResult.protocol());
      startingState = stepResult.resultState();
      assertions = assertions.join(stepResult.assertions());
    }
    return new AnalysisResult<>(startingState, String.join("\n", protocolSteps), assertions);
  }
}
