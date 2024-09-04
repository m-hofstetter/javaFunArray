package benchmarks.sv;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;
import java.util.List;
import java.util.Set;

public class res1of implements Benchmark {
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
                                                            "c#result",
                                                            program.constant(1)),
                                                    program.stop())),
                                    program.block(
                                            List.of())),
                            program.assume(
                                    program.lessEqualThan(
                                            program.variable("N"),
                                            program.division(
                                                    program.constant(2147483647),
                                                    program.constant(4)))),
                            program.assign(
                                    program.arrayElement(
                                            "sum",
                                            program.constant(0)),
                                    program.constant(0)),
                            program.assign(
                                    "i",
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
                                                            program.constant(1)),
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
                                            program.variable("N")),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "sum",
                                                                    program.constant(0)),
                                                            program.addition(
                                                                    Set.of(
                                                                            program.arrayElement(
                                                                                    "sum",
                                                                                    program.constant(0)),
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
                                            program.variable("N")),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "b",
                                                                    program.variable("i")),
                                                            program.constant(1)),
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
                                            program.variable("N")),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "sum",
                                                                    program.constant(0)),
                                                            program.addition(
                                                                    Set.of(
                                                                            program.arrayElement(
                                                                                    "sum",
                                                                                    program.constant(0)),
                                                                            program.arrayElement(
                                                                                    "b",
                                                                                    program.variable("i"))))),
                                                    program.assign(
                                                            "i",
                                                            program.addition(
                                                                    Set.of(
                                                                            program.variable("i"),
                                                                            program.constant(1))))))),
                            program.assert_(
                                    program.lessEqualThan(
                                            program.arrayElement(
                                                    "sum",
                                                    program.constant(0)),
                                            program.variable("N"))),
                            program.assign(
                                    "c#result",
                                    program.constant(1)),
                            program.stop()));
  }

  public List<String> integerVariables() {
    return List.of("N", "i");
  }

  public List<String> arrayVariables() {
    return List.of("sum", "a", "b");
  }

}
