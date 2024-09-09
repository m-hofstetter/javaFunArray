package benchmarks.sv;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;
import java.util.List;

public class copysome1_1 implements Benchmark {
  public <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program) {
    return
            program.block(
                    List.of(
                            program.assign(
                                    "z",
                                    program.constant(150000)),
                            program.assign(
                                    "i",
                                    program.constant(0)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("i"),
                                            program.constant(200000)),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "a1",
                                                                    program.variable("i")),
                                                            program.variable("__VERIFIER_nondet_int₁")),
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "a2",
                                                                    program.variable("i")),
                                                            program.variable("__VERIFIER_nondet_int₂")),
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
                                            program.constant(200000)),
                                    program.block(
                                            List.of(
                                                    program.if_(
                                                            program.unequalTo(
                                                                    program.variable("i"),
                                                                    program.variable("z")),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    program.arrayElement(
                                                                                            "a2",
                                                                                            program.variable("i")),
                                                                                    program.arrayElement(
                                                                                            "a1",
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
                                            program.constant(200000)),
                                    program.block(
                                            List.of(
                                                    program.if_(
                                                            program.unequalTo(
                                                                    program.variable("x"),
                                                                    program.variable("z")),
                                                            program.block(
                                                                    List.of(
                                                                            program.assert_(
                                                                                    program.equalTo(
                                                                                            program.arrayElement(
                                                                                                    "a1",
                                                                                                    program.variable("x")),
                                                                                            program.arrayElement(
                                                                                                    "a2",
                                                                                                    program.variable("x")))))),
                                                            program.block(
                                                                    List.of())),
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
    return List.of("z", "__VERIFIER_nondet_int₂", "__VERIFIER_nondet_int₁", "i", "x");
  }

  public List<String> arrayVariables() {
    return List.of("a1", "a2");
  }

}
