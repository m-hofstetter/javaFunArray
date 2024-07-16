package analysis.common.controlstructure;

import analysis.common.AnalysisResult;
import analysis.common.Program;
import analysis.common.condition.Condition;
import base.DomainValue;
import funarray.Environment;
import java.util.List;

public record IfThenElse<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        Condition<ELEMENT, VARIABLE> condition, Program<ELEMENT, VARIABLE> ifProgram,
        Program<ELEMENT, VARIABLE> elseProgram,
        ELEMENT unreachable) implements Program<ELEMENT, VARIABLE> {

  public static final String PROTOCOL_TEMPLATE = """
          \033[1mIF\033[22m %s \033[1mTHEN:\033[22m
          \033[0;36m%s\033[0m%s\033[1mELSE:\033[22m
          \033[0;36m%s\033[0m%s\033[1mEND IF-ELSE\033[22m
          \033[0;36m%s\033[0m""";
  public static final int INDENTATION = 6;

  public IfThenElse(
          Condition<ELEMENT, VARIABLE> condition,
          Program<ELEMENT, VARIABLE> ifProgram,
          List<Program<ELEMENT, VARIABLE>> elsePrograms,
          ELEMENT unreachable) {
    this(condition, ifProgram, new Block<>(elsePrograms), unreachable);
  }

  public IfThenElse(
          Condition<ELEMENT, VARIABLE> condition,
          List<Program<ELEMENT, VARIABLE>> ifPrograms,
          Program<ELEMENT, VARIABLE> elseProgram,
          ELEMENT unreachable) {
    this(condition, new Block<>(ifPrograms), elseProgram, unreachable);
  }

  public IfThenElse(
          Condition<ELEMENT, VARIABLE> condition,
          List<Program<ELEMENT, VARIABLE>> ifPrograms,
          List<Program<ELEMENT, VARIABLE>> elsePrograms,
          ELEMENT unreachable) {
    this(condition, new Block<>(ifPrograms), new Block<>(elsePrograms), unreachable);
  }

  @Override
  public AnalysisResult<ELEMENT, VARIABLE> run(Environment<ELEMENT, VARIABLE> startingState) {
    var satisfiedState = condition.satisfy(startingState);
    var resultIf = ifProgram.run(satisfiedState);
    var complementSatisfiedState = condition.satisfyComplement(startingState);
    var resultElse = elseProgram.run(complementSatisfiedState);
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