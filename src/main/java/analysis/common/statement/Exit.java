package analysis.common.statement;

import static analysis.common.AnalysisResult.AssertionResult.noAssert;

import abstractdomain.DomainValue;
import analysis.common.Analysis;
import analysis.common.AnalysisResult;
import funarray.state.State;

public record Exit<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>() implements Analysis<ElementT, VariableT> {

  public static final String PROTOCOL_TEMPLATE = "EXIT\n";

  @Override
  public AnalysisResult<ElementT, VariableT> run(State<ElementT, VariableT> startingState) {
    return new AnalysisResult<>(startingState, PROTOCOL_TEMPLATE, noAssert());
  }
}
