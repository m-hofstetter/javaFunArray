package benchmarks.sv;

import java.util.List;
import java.util.Set;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;

public class zero_sum_m2 implements Benchmark {
  public <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program) {
    return
            program.block(
                    List.of(
                            program.havoc(
                                    program.variable("SIZE")),
                            program.if_(
                                    program.lessThan(
                                            program.constant(1),
                                            program.variable("SIZE")),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            "sum",
                                                            program.constant(0)),
                                                    program.assign(
                                                            "i",
                                                            program.constant(0)),
                                                    program.while_(
                                                            program.lessThan(
                                                                    program.variable("i"),
                                                                    program.variable("SIZE")),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    program.arrayElement(
                                                                                            "a",
                                                                                            program.variable("i")),
                                                                                    program.variable("__VERIFIER_nondet_short₁")),
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
                                                                    program.variable("SIZE")),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    "sum",
                                                                                    program.addition(
                                                                                            Set.of(
                                                                                                    program.variable("sum"),
                                                                                                    program.arrayElement(
                                                                                                            "a",
                                                                                                            program.variable("i"))))),
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
                                                                    program.variable("SIZE")),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    "sum",
                                                                                    program.addition(
                                                                                            Set.of(
                                                                                                    program.variable("sum"),
                                                                                                    program.arrayElement(
                                                                                                            "a",
                                                                                                            program.variable("i"))))),
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
                                                                    program.variable("SIZE")),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    "sum",
                                                                                    program.subtraction(
                                                                                            program.variable("sum"),
                                                                                            program.arrayElement(
                                                                                                    "a",
                                                                                                    program.variable("i")))),
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
                                                                    program.variable("SIZE")),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    "sum",
                                                                                    program.subtraction(
                                                                                            program.variable("sum"),
                                                                                            program.arrayElement(
                                                                                                    "a",
                                                                                                    program.variable("i")))),
                                                                            program.assign(
                                                                                    "i",
                                                                                    program.addition(
                                                                                            Set.of(
                                                                                                    program.variable("i"),
                                                                                                    program.constant(1))))))),
                                                    program.assert_(
                                                            program.equalTo(
                                                                    program.variable("sum"),
                                                                    program.constant(0))))),
                                    program.block(
                                            List.of())),
                            program.assign(
                                    "c#result",
                                    program.constant(1)),
                            program.stop()));
  }

  public List<String> integerVariables() {
    return List.of("SIZE", "i", "sum", "__VERIFIER_nondet_short₁");
  }

  public List<String> arrayVariables() {
    return List.of("a");
  }

}
