package benchmarks.sv;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;
import java.util.List;
import java.util.Set;

public class standard_copy3_ground_2 implements Benchmark {
  public <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program) {
    return
            program.block(
                    List.of(
                            program.assign(
                                    "a",
                                    program.constant(0)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("a"),
                                            program.constant(100000)),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "a1",
                                                                    program.variable("a")),
                                                            program.variable("__VERIFIER_nondet_int₁")),
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "a3",
                                                                    program.variable("a")),
                                                            program.variable("__VERIFIER_nondet_int₂")),
                                                    program.assign(
                                                            "a",
                                                            program.addition(
                                                                    Set.of(
                                                                            program.variable("a"),
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
                                                                    "a2",
                                                                    program.variable("i")),
                                                            program.arrayElement(
                                                                    "a1",
                                                                    program.variable("i"))),
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
                                                                    "a4",
                                                                    program.variable("i")),
                                                            program.arrayElement(
                                                                    "a2",
                                                                    program.variable("i"))),
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
                                                                    "a4",
                                                                    program.variable("i")),
                                                            program.arrayElement(
                                                                    "a3",
                                                                    program.variable("i"))),
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
                                                                            "a1",
                                                                            program.variable("x")),
                                                                    program.arrayElement(
                                                                            "a4",
                                                                            program.variable("x")))),
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
    return List.of("__VERIFIER_nondet_int₂", "__VERIFIER_nondet_int₁", "i", "a", "x");
  }

  public List<String> arrayVariables() {
    return List.of("a1", "a2", "a3", "a4");
  }

}
