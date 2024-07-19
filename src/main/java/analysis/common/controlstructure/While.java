package analysis.common.controlstructure;

import analysis.common.Analysis;
import analysis.common.AnalysisResult;
import analysis.common.condition.Condition;
import base.DomainValue;
import funarray.EnvState;
import java.util.List;

public record While<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        Condition<ElementT, VariableT> condition,
        Analysis<ElementT, VariableT> bodyAnalysis,
        ElementT unreachable) implements Analysis<ElementT, VariableT> {

  public static final int WIDENING_LOOP_HARD_LIMIT = 1000;
  public static final String PROTOCOL_TEMPLATE = """
          \033[1mWHILE\033[22m %s \033[1mDO:\033[22m
          \033[0;36m%s\033[0m%s\033[1mEND WHILE\033[22m
          \033[0;36m%s\033[0m""";
  public static final int INDENTATION = 6;

  public While(Condition<ElementT, VariableT> condition,
               List<Analysis<ElementT, VariableT>> bodyAnalyses,
               ElementT unreachable) {
    this(condition, new Block<>(bodyAnalyses), unreachable);
  }

  @Override
  public AnalysisResult<ElementT, VariableT> run(EnvState<ElementT, VariableT> startingState) {
    var state = startingState;
    for (int i = 0; i < WIDENING_LOOP_HARD_LIMIT; i++) {
      var satisfiedState = condition.satisfy(state);
      var result = bodyAnalysis.run(satisfiedState);
      var nextState = state.widen(result.resultState(), unreachable);
      if (state.equals(nextState)) {
        // fixpoint has been reached
        var resultState = condition.satisfyComplement(state);
        return new AnalysisResult<>(
                resultState,
                PROTOCOL_TEMPLATE.formatted(
                        condition,
                        satisfiedState.toString().indent(INDENTATION),
                        result.protocol().indent(INDENTATION),
                        resultState
                )
        );
      }
      state = nextState;
    }
    throw new RuntimeException(
            "Could not find fixpoint after %d widenings.".formatted(WIDENING_LOOP_HARD_LIMIT)
    );
  }
}
