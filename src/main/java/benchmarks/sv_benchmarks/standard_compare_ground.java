package benchmarks.sv_benchmarks;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;
import java.util.List;
import java.util.Set;

public class standard_compare_ground implements Benchmark {
  @Override
  public <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program) {
    return
            program.block(
                    List.of(
                            program.assign(
                                    program.variable("j"),
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
                                                            program.arrayElement(
                                                                    "b",
                                                                    program.variable("j")),
                                                            program.variable("__VERIFIER_nondet_int₂")),
                                                    program.assign(
                                                            program.variable("j"),
                                                            program.addition(
                                                                    Set.of(
                                                                            program.variable("j"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    program.variable("i"),
                                    program.constant(0)),
                            program.assign(
                                    program.variable("rv"),
                                    program.constant(1)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("i"),
                                            program.constant(100000)),
                                    program.block(
                                            List.of(
                                                    program.if_(
                                                            program.unequalTo(
                                                                    program.arrayElement(
                                                                            "a",
                                                                            program.variable("i")),
                                                                    program.arrayElement(
                                                                            "b",
                                                                            program.variable("i"))),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    program.variable("rv"),
                                                                                    program.constant(0)))),
                                                            program.block(
                                                                    List.of())),
                                                    program.assign(
                                                            program.variable("i"),
                                                            program.addition(
                                                                    Set.of(
                                                                            program.variable("i"),
                                                                            program.constant(1))))))),
                            program.if_(
                                    program.unequalTo(
                                            program.variable("rv"),
                                            program.constant(0)),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            program.variable("x"),
                                                            program.constant(0)),
                                                    program.while_(
                                                            program.lessThan(
                                                                    program.variable("x"),
                                                                    program.constant(100000)),
                                                            program.block(
                                                                    List.of(
//                                                                            program.assert_(
//                                                                                    program.equalTo(
//                                                                                            program.arrayElement(
//                                                                                                    "a",
//                                                                                                    program.variable("x")),
//                                                                                            program.arrayElement(
//                                                                                                    "b",
//                                                                                                    program.variable("x")))),
                                                                            program.assign(
                                                                                    program.variable("x"),
                                                                                    program.addition(
                                                                                            Set.of(
                                                                                                    program.variable("x"),
                                                                                                    program.constant(1))))))))),
                                    program.block(
                                            List.of())),
                            program.assign(
                                    program.variable("c#result"),
                                    program.constant(0))));
  }

  @Override
  public List<String> integerVariables() {
    return List.of("__VERIFIER_nondet_int₂", "j", "__VERIFIER_nondet_int₁", "x", "rv", "i");
  }

  @Override
  public List<String> arrayVariables() {
    return List.of("a", "b");
  }

}
