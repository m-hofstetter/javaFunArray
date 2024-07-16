package analysis.common.controlstructure;

import analysis.common.AnalysisResult;
import analysis.common.Program;
import base.DomainValue;
import funarray.Environment;
import java.util.ArrayList;
import java.util.List;

public record Block<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        List<Program<VARIABLE, ELEMENT>> statements) implements Program<VARIABLE, ELEMENT> {

  public Block {
    statements = List.copyOf(statements);
  }

  @Override
  public AnalysisResult<VARIABLE, ELEMENT> run(Environment<VARIABLE, ELEMENT> startingState) {
    var protocolSteps = new ArrayList<String>();
    for (Program<VARIABLE, ELEMENT> s : statements) {
      var stepResult = s.run(startingState);
      protocolSteps.add(stepResult.protocol());
      startingState = stepResult.resultState();
    }
    return new AnalysisResult<>(startingState, String.join("\n", protocolSteps));
  }
}
