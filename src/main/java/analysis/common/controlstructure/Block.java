package analysis.common.controlstructure;

import analysis.common.Analysis;
import analysis.common.AnalysisResult;
import base.DomainValue;
import funarray.Environment;
import java.util.ArrayList;
import java.util.List;

public record Block<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        List<Analysis<VARIABLE, ELEMENT>> statements) implements Analysis<VARIABLE, ELEMENT> {

  public Block {
    statements = List.copyOf(statements);
  }

  @Override
  public AnalysisResult<VARIABLE, ELEMENT> run(Environment<VARIABLE, ELEMENT> startingState) {
    var protocolSteps = new ArrayList<String>();
    for (Analysis<VARIABLE, ELEMENT> s : statements) {
      var stepResult = s.run(startingState);
      protocolSteps.add(stepResult.protocol());
      startingState = stepResult.resultState();
    }
    return new AnalysisResult<>(startingState, String.join("\n", protocolSteps));
  }
}
