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
          \033[1mWHILE\033[22m %s \033[1mDO:\033[22m
          \033[0;36m%s\033[0m%s\033[1mEND WHILE\033[22m
          \033[0;36m%s\033[0m""";
  public static final int INDENTATION = 6;

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
        return new AnalysisResult<>(resultState, PROTOCOL_TEMPLATE.formatted(condition, satisfiedState.toString().indent(INDENTATION), result.protocol().indent(INDENTATION), resultState));
      }
      state = nextState;
    }
    throw new RuntimeException("Could not find fixpoint after %d widenings.".formatted(WIDENING_LOOP_HARD_LIMIT));
  }
}
