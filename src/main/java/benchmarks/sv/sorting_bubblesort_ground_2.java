package benchmarks.sv;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;
import java.util.List;

public class sorting_bubblesort_ground_2 implements Benchmark {
  public <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program) {
    return
            program.block(
                    List.of(
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
                                                            program.variable("__VERIFIER_nondet_int₁")),
                                                    program.assign(
                                                            "j",
                                                            program.addition(
                                                                    List.of(
                                                                            program.variable("j"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    "swapped",
                                    program.constant(1)),
                            program.while_(
                                    program.unequalTo(
                                            program.variable("swapped"),
                                            program.constant(0)),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            "swapped",
                                                            program.constant(0)),
                                                    program.assign(
                                                            "i",
                                                            program.constant(1)),
                                                    program.while_(
                                                            program.lessThan(
                                                                    program.variable("i"),
                                                                    program.constant(100000)),
                                                            program.block(
                                                                    List.of(
                                                                            program.if_(
                                                                                    program.lessThan(
                                                                                            program.arrayElement(
                                                                                                    "a",
                                                                                                    program.subtraction(
                                                                                                            program.variable("i"),
                                                                                                            program.constant(1))),
                                                                                            program.arrayElement(
                                                                                                    "a",
                                                                                                    program.variable("i"))),
                                                                                    program.block(
                                                                                            List.of(
                                                                                                    program.assign(
                                                                                                            "t",
                                                                                                            program.arrayElement(
                                                                                                                    "a",
                                                                                                                    program.variable("i"))),
                                                                                                    program.assign(
                                                                                                            program.arrayElement(
                                                                                                                    "a",
                                                                                                                    program.variable("i")),
                                                                                                            program.arrayElement(
                                                                                                                    "a",
                                                                                                                    program.subtraction(
                                                                                                                            program.variable("i"),
                                                                                                                            program.constant(1)))),
                                                                                                    program.assign(
                                                                                                            program.arrayElement(
                                                                                                                    "a",
                                                                                                                    program.subtraction(
                                                                                                                            program.variable("i"),
                                                                                                                            program.constant(1))),
                                                                                                            program.variable("t")),
                                                                                                    program.assign(
                                                                                                            "swapped",
                                                                                                            program.constant(1)))),
                                                                                    program.block(
                                                                                            List.of())),
                                                                            program.assign(
                                                                                    "i",
                                                                                    program.addition(
                                                                                            List.of(
                                                                                                    program.variable("i"),
                                                                                                    program.constant(1)))))))))),
                            program.assign(
                                    "x",
                                    program.constant(0)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("x"),
                                            program.constant(100000)),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            "y",
                                                            program.addition(
                                                                    List.of(
                                                                            program.variable("x"),
                                                                            program.constant(1)))),
                                                    program.while_(
                                                            program.lessThan(
                                                                    program.variable("y"),
                                                                    program.constant(100000)),
                                                            program.block(
                                                                    List.of(
                                                                            program.assert_(
                                                                                    program.lessEqualThan(
                                                                                            program.arrayElement(
                                                                                                    "a",
                                                                                                    program.variable("x")),
                                                                                            program.arrayElement(
                                                                                                    "a",
                                                                                                    program.variable("y")))),
                                                                            program.assign(
                                                                                    "y",
                                                                                    program.addition(
                                                                                            List.of(
                                                                                                    program.variable("y"),
                                                                                                    program.constant(1))))))),
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

  public List<String> integerVariables() {
    return List.of("t", "y", "j", "__VERIFIER_nondet_int₁", "i", "swapped", "x");
  }

  public List<String> arrayVariables() {
    return List.of("a");
  }

}
