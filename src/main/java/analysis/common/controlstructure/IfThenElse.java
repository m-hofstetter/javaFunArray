package analysis.common.controlstructure;

import analysis.common.Analysis;
import analysis.common.AnalysisResult;
import analysis.common.condition.Condition;
import base.DomainValue;
import funarray.EnvState;
import java.util.List;

public record IfThenElse<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        Condition<ELEMENT, VARIABLE> condition, Analysis<ELEMENT, VARIABLE> ifAnalysis,
        Analysis<ELEMENT, VARIABLE> elseAnalysis,
        ELEMENT unreachable) implements Analysis<ELEMENT, VARIABLE> {

  public static final String PROTOCOL_TEMPLATE = """
          \033[1mIF\033[22m %s \033[1mTHEN DO:\033[22m
          \033[0;36m%s\033[0m%s\033[1mELSE DO:\033[22m
          \033[0;36m%s\033[0m%s\033[1mEND IF-THEN-ELSE\033[22m
          \033[0;36m%s\033[0m""";
  public static final int INDENTATION = 6;

  public IfThenElse(
          Condition<ELEMENT, VARIABLE> condition,
          Analysis<ELEMENT, VARIABLE> ifAnalysis,
          List<Analysis<ELEMENT, VARIABLE>> elseAnalyses,
          ELEMENT unreachable) {
    this(condition, ifAnalysis, new Block<>(elseAnalyses), unreachable);
  }

  public IfThenElse(
          Condition<ELEMENT, VARIABLE> condition,
          List<Analysis<ELEMENT, VARIABLE>> ifAnalyses,
          Analysis<ELEMENT, VARIABLE> elseAnalysis,
          ELEMENT unreachable) {
    this(condition, new Block<>(ifAnalyses), elseAnalysis, unreachable);
  }

  public IfThenElse(
          Condition<ELEMENT, VARIABLE> condition,
          List<Analysis<ELEMENT, VARIABLE>> ifAnalyses,
          List<Analysis<ELEMENT, VARIABLE>> elseAnalyses,
          ELEMENT unreachable) {
    this(condition, new Block<>(ifAnalyses), new Block<>(elseAnalyses), unreachable);
  }

  @Override
  public AnalysisResult<ELEMENT, VARIABLE> run(EnvState<ELEMENT, VARIABLE> startingState) {
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