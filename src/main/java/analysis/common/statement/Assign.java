package analysis.common.statement;

import static analysis.common.AnalysisResult.AssertionResult.noAssert;

import abstractdomain.DomainValue;
import analysis.common.Analysis;
import analysis.common.AnalysisResult;
import analysis.common.expression.Assignable;
import analysis.common.expression.Expression;
import funarray.state.State;

public class Assign<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> implements Analysis<ElementT, VariableT> {

  public static final String PROTOCOL_TEMPLATE = """
          %s ← %s
          \033[0;36m%s\033[0m""";

  private final Expression<ElementT, VariableT> source;
  private final Assignable<ElementT, VariableT> target;

  public Assign(
          Expression<ElementT, VariableT> source,
          Assignable<ElementT, VariableT> target
  ) {
    this.source = source;
    this.target = target;
  }

  @Override
  public AnalysisResult<ElementT, VariableT> run(State<ElementT, VariableT> startingState) {
    var modifiedState = target.assign(source, startingState);
    var protocol = PROTOCOL_TEMPLATE.formatted(target, source, modifiedState);
    return new AnalysisResult<>(modifiedState, protocol, noAssert());
  }
}
