package benchmarks.sv;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;
import java.util.List;

public class partial_mod_count_5 implements Benchmark {
  public List<String> integerVariables() {
    return List.of("N", "i", "j", "R");
  }

  public List<String> arrayVariables() {
    return List.of("a");
  }

  public boolean allAssertionsShouldHold() {
    return true;
  }

  public <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program) {
    return
            program.block(
                    List.of(
                            program.assign(
                                    "N",
                                    program.constant(1000000)),
                            program.assign(
                                    "j",
                                    program.constant(0)),
                            program.arrayInit(
                                    "a",
                                    program.variable("N")),
                            program.assign(
                                    "R",
                                    program.constant(5)),
                            program.assign(
                                    "i",
                                    program.constant(0)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("i"),
                                            program.variable("N")),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "a",
                                                                    program.variable("i")),
                                                            program.addition(
                                                                    List.of(
                                                                            program.variable("i"),
                                                                            program.constant(1)))),
                                                    program.if_(
                                                            program.lessThan(
                                                                    program.division(
                                                                            program.variable("N"),
                                                                            program.constant(2)),
                                                                    program.variable("i")),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    program.arrayElement(
                                                                                            "a",
                                                                                            program.variable("i")),
                                                                                    program.modulo(
                                                                                            program.variable("i"),
                                                                                            program.variable("R"))))),
                                                            program.block(
                                                                    List.of())),
                                                    program.assign(
                                                            "i",
                                                            program.addition(
                                                                    List.of(
                                                                            program.variable("i"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    "i",
                                    program.constant(0)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("i"),
                                            program.variable("N")),
                                    program.block(
                                            List.of(
                                                    program.if_(
                                                            program.equalTo(
                                                                    program.arrayElement(
                                                                            "a",
                                                                            program.variable("i")),
                                                                    program.constant(0)),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    "j",
                                                                                    program.addition(
                                                                                            List.of(
                                                                                                    program.variable("j"),
                                                                                                    program.constant(1)))))),
                                                            program.block(
                                                                    List.of())),
                                                    program.assert_(
                                                            program.lessThan(
                                                                    program.variable("j"),
                                                                    program.division(
                                                                            program.variable("N"),
                                                                            program.constant(10)))),
                                                    program.assign(
                                                            "i",
                                                            program.addition(
                                                                    List.of(
                                                                            program.variable("i"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    "c#result",
                                    program.constant(0)),
                            program.stop()));
  }

}
