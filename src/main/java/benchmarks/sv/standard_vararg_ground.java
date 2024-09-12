package benchmarks.sv;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;
import java.util.List;

public class standard_vararg_ground implements Benchmark {
  public List<String> integerVariables() {
    return List.of("a", "i", "nondet_int₁", "x");
  }

  public List<String> arrayVariables() {
    return List.of("aa");
  }

  public boolean allAssertionsShouldHold() {
    return true;
  }

  public <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program) {
    return
            program.block(
                    List.of(
                            program.arrayInit(
                                    "aa",
                                    program.constant(100000)),
                            program.assign(
                                    "a",
                                    program.constant(0)),
                            program.assign(
                                    "i",
                                    program.constant(0)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("i"),
                                            program.constant(100000)),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "aa",
                                                                    program.variable("i")),
                                                            program.variable("nondet_int₁")),
                                                    program.assign(
                                                            "i",
                                                            program.addition(
                                                                    List.of(
                                                                            program.variable("i"),
                                                                            program.constant(1))))))),
                            program.while_(
                                    program.lessEqualThan(
                                            program.constant(0),
                                            program.arrayElement(
                                                    "aa",
                                                    program.variable("a"))),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            "a",
                                                            program.addition(
                                                                    List.of(
                                                                            program.variable("a"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    "x",
                                    program.constant(0)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("x"),
                                            program.variable("a")),
                                    program.block(
                                            List.of(
                                                    program.assert_(
                                                            program.lessEqualThan(
                                                                    program.constant(0),
                                                                    program.arrayElement(
                                                                            "aa",
                                                                            program.variable("x")))),
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
