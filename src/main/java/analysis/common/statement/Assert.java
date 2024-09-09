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
    var satisifed = condition.satisfy(startingState);
    var satisifedComplement = condition.satisfyComplement(startingState);

    var assertionProtocol = "";
    var assertion = noAssert();

    if (satisifed.isReachable()) {
      if (satisifedComplement.isReachable()) {
        assertion = assertion.indeterminableAssert();
        assertionProtocol = INDETERMINABLE_ASSERTION_TEMPLATE.formatted(condition);
      } else {
        assertion = assertion.positiveAssert();
        assertionProtocol = POSITIVE_ASSERTION_TEMPLATE.formatted(condition);
      }
    } else {
      if (satisifedComplement.isReachable()) {
        assertion = assertion.negativeAssert();
        assertionProtocol = NEGATIVE_ASSERTION_TEMPLATE.formatted(condition);
      } else {
        throw new IllegalStateException();
      }
    }

    var protocol = PROTOCOL_TEMPLATE.formatted(condition, assertionProtocol);
    return new AnalysisResult<>(startingState, protocol, assertion);
  }
}
