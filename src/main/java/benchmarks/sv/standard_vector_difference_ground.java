package benchmarks.sv;

import java.util.List;
import java.util.Set;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;

public class standard_vector_difference_ground implements Benchmark {
  public <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program) {
    return
            program.block(
                    List.of(
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
                                                            program.variable("__VERIFIER_nondet_int₁")),
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "b",
                                                                    program.variable("i")),
                                                            program.variable("__VERIFIER_nondet_int₂")),
                                                    program.assign(
                                                            "i",
                                                            program.addition(
                                                                    Set.of(
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
                                                                    Set.of(
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
                                                                    Set.of(
                                                                            program.variable("x"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    "c#result",
                                    program.constant(0)),
                            program.stop()));
  }

  public List<String> integerVariables() {
    return List.of("i", "__VERIFIER_nondet_int₁", "__VERIFIER_nondet_int₂", "x");
  }

  public List<String> arrayVariables() {
    return List.of("a", "b", "c");
  }

}
