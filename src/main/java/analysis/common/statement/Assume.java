package analysis.common.statement;

import abstractdomain.DomainValue;
import analysis.common.Analysis;
import analysis.common.AnalysisResult;
import analysis.common.condition.Condition;
import funarray.EnvState;

public record Assume<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        Condition<ElementT, VariableT> condition) implements Analysis<ElementT, VariableT> {

  public static final String PROTOCOL_TEMPLATE = """
          ASSUME %s
          \033[0;36m%s\033[0m""";

  @Override
  public AnalysisResult<ElementT, VariableT> run(EnvState<ElementT, VariableT> startingState) {
    var satisifed = condition.satisfy(startingState);
    var protocol = PROTOCOL_TEMPLATE.formatted(condition, satisifed);
    return new AnalysisResult<>(satisifed, protocol);
  }
}
