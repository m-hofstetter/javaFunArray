package analysis.common.controlstructure;

import abstractdomain.DomainValue;
import analysis.common.Analysis;
import analysis.common.AnalysisContext;
import analysis.common.AnalysisResult;
import analysis.common.condition.Condition;
import funarray.state.State;
import java.util.List;

/**
 * A control structure that executes a statement as long as a given condition is satisfied.
 *
 * @param condition    the condition.
 * @param bodyAnalysis the statement to execute while the condition is true.
 * @param <ElementT>   the domain to abstract array element values with.
 * @param <VariableT>  the domain to abstract variable values with.
 */
public record While<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        Condition<ElementT, VariableT> condition,
        Analysis<ElementT, VariableT> bodyAnalysis,
        AnalysisContext<ElementT, VariableT> context) implements Analysis<ElementT, VariableT> {

  public static final int WIDENING_LOOP_HARD_LIMIT = 1000;
  public static final String PROTOCOL_TEMPLATE = """
          \033[1mWHILE\033[22m %s \033[1mDO:\033[22m
          \033[0;36m%s\033[0m%s\033[1mEND WHILE\033[22m
          \033[0;36m%s\033[0m""";
  public static final int INDENTATION = 6;

  public While(Condition<ElementT, VariableT> condition,
               List<Analysis<ElementT, VariableT>> bodyAnalysis,
               AnalysisContext<ElementT, VariableT> context) {
    this(condition, new Block<>(bodyAnalysis), context);
  }

  @Override
  public AnalysisResult<ElementT, VariableT> run(State<ElementT, VariableT> startingState) {
    var state = startingState;
    for (int i = 0; i < WIDENING_LOOP_HARD_LIMIT; i++) {
      var satisfiedState = condition.satisfy(state);
      var result = bodyAnalysis.run(satisfiedState);
      var nextState = state.widen(result.resultState());
      if (state.equals(nextState)) {
        // fixpoint has been reached
        var resultState = condition.satisfyComplement(state);
        return new AnalysisResult<>(
                resultState,
                result.exitStates(),
                PROTOCOL_TEMPLATE.formatted(
                        condition,
                        satisfiedState.toString().indent(INDENTATION),
                        result.protocol().indent(INDENTATION),
                        resultState
                ),
                result.assertions()
        );
      }
      state = nextState;
    }
    throw new RuntimeException(
            "Could not find fixpoint after %d widenings.".formatted(WIDENING_LOOP_HARD_LIMIT)
    );
  }
}
