package benchmarks.sv_benchmarks;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;
import java.util.List;
import java.util.Set;

public class condm implements Benchmark {
  public <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program) {
    return
            program.block(
                    List.of(
                            program.havoc(
                                    program.variable("N")),
                            program.if_(
                                    program.lessEqualThan(
                                            program.variable("N"),
                                            program.constant(0)),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            program.variable("c#result"),
                                                            program.constant(1)))),
                                    program.block(
                                            List.of())),
                            program.assume(
                                    program.lessEqualThan(
                                            program.variable("N"),
                                            program.division(
                                                    program.constant(2147483647),
                                                    program.constant(4)))),
                            program.assign(
                                    program.variable("i"),
                                    program.constant(0)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("i"),
                                            program.variable("N")),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "a",
                                                                    program.variable("i")),
                                                            program.constant(0)),
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
                                            program.variable("N")),
                                    program.block(
                                            List.of(
                                                    program.if_(
                                                            program.equalTo(
                                                                    program.modulo(
                                                                            program.variable("N"),
                                                                            program.constant(2)),
                                                                    program.constant(0)),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    program.arrayElement(
                                                                                            "a",
                                                                                            program.variable("i")),
                                                                                    program.addition(
                                                                                            Set.of(
                                                                                                    program.arrayElement(
                                                                                                            "a",
                                                                                                            program.variable("i")),
                                                                                                    program.constant(2)))))),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    program.arrayElement(
                                                                                            "a",
                                                                                            program.variable("i")),
                                                                                    program.addition(
                                                                                            Set.of(
                                                                                                    program.arrayElement(
                                                                                                            "a",
                                                                                                            program.variable("i")),
                                                                                                    program.constant(1))))))),
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
                                            program.variable("N")),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            program.variable("i"),
                                                            program.addition(
                                                                    Set.of(
                                                                            program.variable("i"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    program.variable("c#result"),
                                    program.constant(1))));
  }

  public List<String> integerVariables() {
    return List.of("N", "i");
  }

  public List<String> arrayVariables() {
    return List.of("a");
  }

}
