package benchmarks.sv;

import java.util.List;
import java.util.Set;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;

public class standard_password_ground implements Benchmark {
  public <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program) {
    return
            program.block(
                    List.of(
                            program.assign(
                                    "result",
                                    program.constant(1)),
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
                                                                    "password",
                                                                    program.variable("i")),
                                                            program.variable("__VERIFIER_nondet_int₁")),
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "guess",
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
                                                    program.if_(
                                                            program.unequalTo(
                                                                    program.arrayElement(
                                                                            "password",
                                                                            program.variable("i")),
                                                                    program.arrayElement(
                                                                            "guess",
                                                                            program.variable("i"))),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    "result",
                                                                                    program.constant(0)))),
                                                            program.block(
                                                                    List.of())),
                                                    program.assign(
                                                            "i",
                                                            program.addition(
                                                                    Set.of(
                                                                            program.variable("i"),
                                                                            program.constant(1))))))),
                            program.if_(
                                    program.unequalTo(
                                            program.variable("result"),
                                            program.constant(0)),
                                    program.block(
                                            List.of(
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
                                                                                                    "password",
                                                                                                    program.variable("x")),
                                                                                            program.arrayElement(
                                                                                                    "guess",
                                                                                                    program.variable("x")))),
                                                                            program.assign(
                                                                                    "x",
                                                                                    program.addition(
                                                                                            Set.of(
                                                                                                    program.variable("x"),
                                                                                                    program.constant(1))))))))),
                                    program.block(
                                            List.of())),
                            program.assign(
                                    "c#result",
                                    program.constant(0)),
                            program.stop()));
  }

  public List<String> integerVariables() {
    return List.of("__VERIFIER_nondet_int₂", "result", "__VERIFIER_nondet_int₁", "i", "x");
  }

  public List<String> arrayVariables() {
    return List.of("password", "guess");
  }

}
