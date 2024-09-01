package benchmarks.sv_benchmarks;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;
import java.util.List;
import java.util.Set;

public class copysome2_1 implements Benchmark {
  public <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program) {
    return
            program.block(
                    List.of(
                            program.assign(
                                    program.variable("z"),
                                    program.constant(150000)),
                            program.assign(
                                    program.variable("i"),
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
                                                            program.arrayElement(
                                                                    "a3",
                                                                    program.variable("i")),
                                                            program.variable("__VERIFIER_nondet_int₃")),
                                                    program.assign(
                                                            program.variable("i"),
                                                            program.addition(
                                                                    Set.of(
                                                                            program.variable("i"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    program.variable("i"),
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
                                                            program.variable("i"),
                                                            program.addition(
                                                                    Set.of(
                                                                            program.variable("i"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    program.variable("i"),
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
                                                                                            "a3",
                                                                                            program.variable("i")),
                                                                                    program.arrayElement(
                                                                                            "a2",
                                                                                            program.variable("i"))))),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    program.arrayElement(
                                                                                            "a3",
                                                                                            program.variable("i")),
                                                                                    program.arrayElement(
                                                                                            "a1",
                                                                                            program.variable("i")))))),
                                                    program.assign(
                                                            program.variable("i"),
                                                            program.addition(
                                                                    Set.of(
                                                                            program.variable("i"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    program.variable("x"),
                                    program.constant(0)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("x"),
                                            program.constant(200000)),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            program.variable("x"),
                                                            program.addition(
                                                                    Set.of(
                                                                            program.variable("x"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    program.variable("c#result"),
                                    program.constant(0))));
  }

  public List<String> integerVariables() {
    return List.of("z", "__VERIFIER_nondet_int₃", "__VERIFIER_nondet_int₂", "__VERIFIER_nondet_int₁", "i", "x");
  }

  public List<String> arrayVariables() {
    return List.of("a1", "a2", "a3");
  }

}
