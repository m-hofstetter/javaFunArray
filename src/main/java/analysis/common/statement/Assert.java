package analysis.common.statement;

import static analysis.common.AnalysisResult.AssertionResult.noAssert;

import abstractdomain.DomainValue;
import analysis.common.Analysis;
import analysis.common.AnalysisResult;
import analysis.common.condition.Condition;
import funarray.state.State;

public record Assert<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        Condition<ElementT, VariableT> condition) implements Analysis<ElementT, VariableT> {

  public static final String PROTOCOL_TEMPLATE = """
          ASSERT %s
          %s""";

  public static final String POSITIVE_ASSERTION_TEMPLATE = "\033[0;32mCondition %s is definitely true.\033[0m";
  public static final String NEGATIVE_ASSERTION_TEMPLATE = "\033[0;31mCondition %s is definitely false.\033[0m";
  public static final String INDETERMINABLE_ASSERTION_TEMPLATE = "\033[0;35mCondition %s is indeterminable.\033[0m";

  @Override
  public AnalysisResult<ElementT, VariableT> run(State<ElementT, VariableT> startingState) {
    var assertionResult = condition.isSatisfied(startingState);

    var assertion = switch (assertionResult) {
      case TRUE -> noAssert().positiveAssert();
      case UNKNOWN -> noAssert().indeterminableAssert();
      case FALSE -> noAssert().negativeAssert();
    };

    var protocol = switch (assertionResult) {
      case TRUE -> POSITIVE_ASSERTION_TEMPLATE.formatted(condition);
      case UNKNOWN -> INDETERMINABLE_ASSERTION_TEMPLATE.formatted(condition);
      case FALSE -> NEGATIVE_ASSERTION_TEMPLATE.formatted(condition);
    };

    return new AnalysisResult<>(startingState, protocol, assertion);
  }
}
