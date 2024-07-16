package analysis.common.controlstructure;

import analysis.common.AnalysisResult;
import analysis.common.Program;
import analysis.common.condition.Condition;
import base.DomainValue;
import funarray.Environment;
import java.util.List;

public record While<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        Condition<ELEMENT, VARIABLE> condition, Program<ELEMENT, VARIABLE> program,
        ELEMENT unreachable) implements Program<ELEMENT, VARIABLE> {

  public static final int WIDENING_LOOP_HARD_LIMIT = 1000;
  public static final String PROTOCOL_TEMPLATE = """
          WHILE %s DO;
          %s
          %s
          END WHILE
          %s
          """;
  public static final int INDENTATION = 4;

  public While(Condition<ELEMENT, VARIABLE> condition,
               List<Program<ELEMENT, VARIABLE>> programs,
               ELEMENT unreachable) {
    this(condition, new Block<>(programs), unreachable);
  }

  @Override
  public AnalysisResult<ELEMENT, VARIABLE> run(Environment<ELEMENT, VARIABLE> startingState) {
    var state = startingState;
    for (int i = 0; i < WIDENING_LOOP_HARD_LIMIT; i++) {
      var satisfiedState = condition.satisfy(state);
      var result = program.run(satisfiedState);
      var nextState = state.widen(result.resultState(), unreachable);
      if (state.equals(nextState)) {
        // fixpoint has been reached
        var resultState = condition.satisfyComplement(state);
        return new AnalysisResult<>(resultState, PROTOCOL_TEMPLATE.formatted(condition, state.toString().indent(INDENTATION), result.protocol().indent(INDENTATION), resultState));
      }
      state = nextState;
    }
    throw new RuntimeException("Could not find fixpoint after %d widenings.".formatted(WIDENING_LOOP_HARD_LIMIT));
  }
}
