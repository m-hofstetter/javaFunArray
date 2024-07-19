package analysis.common.controlstructure;

import analysis.common.Analysis;
import analysis.common.AnalysisResult;
import base.DomainValue;
import funarray.EnvState;
import java.util.ArrayList;
import java.util.List;

public record Block<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        List<Analysis<VariableT, ElementT>> statements) implements Analysis<VariableT, ElementT> {

  public Block {
    statements = List.copyOf(statements);
  }

  @Override
  public AnalysisResult<VariableT, ElementT> run(EnvState<VariableT, ElementT> startingState) {
    var protocolSteps = new ArrayList<String>();
    for (Analysis<VariableT, ElementT> s : statements) {
      var stepResult = s.run(startingState);
      protocolSteps.add(stepResult.protocol());
      startingState = stepResult.resultState();
    }
    return new AnalysisResult<>(startingState, String.join("\n", protocolSteps));
  }
}
