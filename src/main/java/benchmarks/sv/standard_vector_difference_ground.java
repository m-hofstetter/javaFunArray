package benchmarks.sv;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;
import java.util.List;

public class standard_vector_difference_ground implements Benchmark {
  public List<String> integerVariables() {
    return List.of("i", "nondet_int₁", "nondet_int₂", "x");
  }

  public List<String> arrayVariables() {
    return List.of("a", "b", "c");
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
                            program.arrayInit(
                                    "b",
                                    program.constant(100000)),
                            program.arrayInit(
                                    "c",
                                    program.constant(100000)),
                            program.assign(
                                    "i",
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
                                                                    "a",
                                                                    program.variable("i")),
                                                            program.variable("nondet_int₁")),
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "b",
                                                                    program.variable("i")),
                                                            program.variable("nondet_int₂")),
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
                                            program.constant(100000)),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "c",
                                                                    program.variable("i")),
                                                            program.subtraction(
                                                                    program.arrayElement(
                                                                            "a",
                                                                            program.variable("i")),
                                                                    program.arrayElement(
                                                                            "b",
                                                                            program.variable("i")))),
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
                                                            program.equalTo(
                                                                    program.arrayElement(
                                                                            "c",
                                                                            program.variable("x")),
                                                                    program.subtraction(
                                                                            program.arrayElement(
                                                                                    "a",
                                                                                    program.variable("x")),
                                                                            program.arrayElement(
                                                                                    "b",
                                                                                    program.variable("x"))))),
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
