package benchmarks.sv;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;
import java.util.List;

public class standard_maxInArray_ground implements Benchmark {
  public List<String> integerVariables() {
    return List.of("max", "nondet_int₁", "j", "i", "x");
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
                            program.arrayInit(
                                    "a",
                                    program.constant(100000)),
                            program.assign(
                                    "max",
                                    program.constant(0)),
                            program.assign(
                                    "j",
                                    program.constant(0)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("j"),
                                            program.constant(100000)),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "a",
                                                                    program.variable("j")),
                                                            program.variable("nondet_int₁")),
                                                    program.assign(
                                                            "j",
                                                            program.addition(
                                                                    List.of(
                                                                            program.variable("j"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    "i",
                                    program.constant(0)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("i"),
                                            program.constant(100000)),
                                    program.block(
                                            List.of(
                                                    program.if_(
                                                            program.lessThan(
                                                                    program.variable("max"),
                                                                    program.arrayElement(
                                                                            "a",
                                                                            program.variable("i"))),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    "max",
                                                                                    program.arrayElement(
                                                                                            "a",
                                                                                            program.variable("i"))))),
                                                            program.block(
                                                                    List.of())),
                                                    program.assign(
                                                            "i",
                                                            program.addition(
                                                                    List.of(
                                                                            program.variable("i"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    "x",
                                    program.constant(0)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("x"),
                                            program.constant(100000)),
                                    program.block(
                                            List.of(
                                                    program.assert_(
                                                            program.lessEqualThan(
                                                                    program.arrayElement(
                                                                            "a",
                                                                            program.variable("x")),
                                                                    program.variable("max"))),
                                                    program.assign(
                                                            "x",
                                                            program.addition(
                                                                    List.of(
                                                                            program.variable("x"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    "c#result",
                                    program.constant(0)),
                            program.stop()));
  }

}
