package benchmarks.sv;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;
import java.util.List;
import java.util.Set;

public class array_shadowinit implements Benchmark {
  public <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program) {
    return
            program.block(
                    List.of(
                            program.havoc(
                                    program.variable("N")),
                            program.if_(
                                    program.lessThan(
                                            program.constant(0),
                                            program.variable("N")),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            "i",
                                                            program.constant(0)),
                                                    program.assign(
                                                            "k",
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
                                                                                            program.variable("k")),
                                                                                    program.variable("k")),
                                                                            program.assign(
                                                                                    "i",
                                                                                    program.addition(
                                                                                            Set.of(
                                                                                                    program.variable("i"),
                                                                                                    program.constant(1)))),
                                                                            program.assign(
                                                                                    "k",
                                                                                    program.addition(
                                                                                            Set.of(
                                                                                                    program.variable("k"),
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
                                                                            program.assert_(
                                                                                    program.equalTo(
                                                                                            program.arrayElement(
                                                                                                    "a",
                                                                                                    program.variable("i")),
                                                                                            program.variable("i"))),
                                                                            program.assign(
                                                                                    "i",
                                                                                    program.addition(
                                                                                            Set.of(
                                                                                                    program.variable("i"),
                                                                                                    program.constant(1))))))))),
                                    program.block(
                                            List.of())),
                            program.assign(
                                    "c#result",
                                    program.constant(0)),
                            program.stop()));
  }

  public List<String> integerVariables() {
    return List.of("N", "i", "k");
  }

  public List<String> arrayVariables() {
    return List.of("a");
  }

}
