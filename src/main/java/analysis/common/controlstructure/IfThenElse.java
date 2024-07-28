package analysis.common.controlstructure;

import abstractdomain.DomainValue;
import analysis.common.Analysis;
import analysis.common.AnalysisContext;
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
        AnalysisContext<ElementT, VariableT> context) implements Analysis<ElementT, VariableT> {

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
          AnalysisContext<ElementT, VariableT> context) {
    this(condition, ifAnalysis, new Block<>(elseAnalyses), context);
  }

  public IfThenElse(
          Condition<ElementT, VariableT> condition,
          List<Analysis<ElementT, VariableT>> ifAnalyses,
          Analysis<ElementT, VariableT> elseAnalysis,
          AnalysisContext<ElementT, VariableT> context) {
    this(condition, new Block<>(ifAnalyses), elseAnalysis, context);
  }

  public IfThenElse(
          Condition<ElementT, VariableT> condition,
          List<Analysis<ElementT, VariableT>> ifAnalyses,
          List<Analysis<ElementT, VariableT>> elseAnalyses,
          AnalysisContext<ElementT, VariableT> context) {
    this(condition, new Block<>(ifAnalyses), new Block<>(elseAnalyses), context);
  }

  @Override
  public AnalysisResult<ElementT, VariableT> run(EnvState<ElementT, VariableT> startingState) {
    var satisfiedState = condition.satisfy(startingState);
    var resultIf = ifAnalysis.run(satisfiedState);
    var complementSatisfiedState = condition.satisfyComplement(startingState);
    var resultElse = elseAnalysis.run(complementSatisfiedState);
    var joinedState = resultIf.resultState().join(resultElse.resultState(), context.getElementDomain().getUnreachable());

    var protocol = PROTOCOL_TEMPLATE.formatted(condition,
            satisfiedState.toString().indent(INDENTATION),
            resultIf.protocol().indent(INDENTATION),
            complementSatisfiedState.toString().indent(INDENTATION),
            resultElse.protocol().indent(INDENTATION),
            joinedState.toString());

    return new AnalysisResult<>(joinedState, protocol);
  }
}