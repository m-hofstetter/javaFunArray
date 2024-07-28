package analysis.common.controlstructure;

import abstractdomain.DomainValue;
import analysis.common.Analysis;
import analysis.common.AnalysisResult;
import analysis.common.condition.Condition;
import funarray.EnvState;
import java.util.List;

/**
 * A control structure that executes either one of two given statements depending on whether a
 * condition is satisfied.
 *
 * @param condition    the condition.
 * @param ifAnalysis   the statement to execute if the condition is true.
 * @param elseAnalysis the statement to execute if the condition is false.
 * @param unreachable  the unreachable element for abstract joining.
 * @param <ElementT>   the domain to abstract array element values with.
 * @param <VariableT>  the domain to abstract variable values with.
 */
public record IfThenElse<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        Condition<ElementT, VariableT> condition,
        Analysis<ElementT, VariableT> ifAnalysis,
        Analysis<ElementT, VariableT> elseAnalysis,
        ElementT unreachable) implements Analysis<ElementT, VariableT> {

  public static final String PROTOCOL_TEMPLATE = """
          \033[1mIF\033[22m %s \033[1mTHEN DO:\033[22m
          \033[0;36m%s\033[0m%s\033[1mELSE DO:\033[22m
          \033[0;36m%s\033[0m%s\033[1mEND IF-THEN-ELSE\033[22m
          \033[0;36m%s\033[0m""";
  public static final int INDENTATION = 6;

  public IfThenElse(
          Condition<ElementT, VariableT> condition,
          Analysis<ElementT, VariableT> ifAnalysis,
          List<Analysis<ElementT, VariableT>> elseAnalyses,
          ElementT unreachable) {
    this(condition, ifAnalysis, new Block<>(elseAnalyses), unreachable);
  }

  public IfThenElse(
          Condition<ElementT, VariableT> condition,
          List<Analysis<ElementT, VariableT>> ifAnalyses,
          Analysis<ElementT, VariableT> elseAnalysis,
          ElementT unreachable) {
    this(condition, new Block<>(ifAnalyses), elseAnalysis, unreachable);
  }

  public IfThenElse(
          Condition<ElementT, VariableT> condition,
          List<Analysis<ElementT, VariableT>> ifAnalyses,
          List<Analysis<ElementT, VariableT>> elseAnalyses,
          ElementT unreachable) {
    this(condition, new Block<>(ifAnalyses), new Block<>(elseAnalyses), unreachable);
  }

  @Override
  public AnalysisResult<ElementT, VariableT> run(EnvState<ElementT, VariableT> startingState) {
    var satisfiedState = condition.satisfy(startingState);
    var resultIf = ifAnalysis.run(satisfiedState);
    var complementSatisfiedState = condition.satisfyComplement(startingState);
    var resultElse = elseAnalysis.run(complementSatisfiedState);
    var joinedState = resultIf.resultState().join(resultElse.resultState(), unreachable);

    var protocol = PROTOCOL_TEMPLATE.formatted(condition,
            satisfiedState.toString().indent(INDENTATION),
            resultIf.protocol().indent(INDENTATION),
            complementSatisfiedState.toString().indent(INDENTATION),
            resultElse.protocol().indent(INDENTATION),
            joinedState.toString());

    return new AnalysisResult<>(joinedState, protocol);
  }
}