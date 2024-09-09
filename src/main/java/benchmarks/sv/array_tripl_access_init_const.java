package benchmarks.sv;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;
import java.util.List;

public class array_tripl_access_init_const implements Benchmark {
  public <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program) {
    return
            program.block(
                    List.of(
                            program.assign(
                                    "N",
                                    program.constant(100000)),
                            program.assign(
                                    "i",
                                    program.constant(0)),
                            program.while_(
                                    program.lessEqualThan(
                                            program.variable("i"),
                                            program.variable("N")),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "a",
                                                                    program.multiplication(
                                                                            List.of(
                                                                                    program.constant(3),
                                                                                    program.variable("i")))),
                                                            program.constant(0)),
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "a",
                                                                    program.addition(
                                                                            List.of(
                                                                                    program.multiplication(
                                                                                            List.of(
                                                                                                    program.constant(3),
                                                                                                    program.variable("i"))),
                                                                                    program.constant(1)))),
                                                            program.constant(0)),
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "a",
                                                                    program.addition(
                                                                            List.of(
                                                                                    program.multiplication(
                                                                                            List.of(
                                                                                                    program.constant(3),
                                                                                                    program.variable("i"))),
                                                                                    program.constant(2)))),
                                                            program.constant(0)),
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
                                    program.lessEqualThan(
                                            program.variable("i"),
                                            program.multiplication(
                                                    List.of(
                                                            program.constant(3),
                                                            program.variable("N")))),
                                    program.block(
                                            List.of(
                                                    program.assert_(
                                                            program.lessEqualThan(
                                                                    program.constant(0),
                                                                    program.arrayElement(
                                                                            "a",
                                                                            program.variable("i")))),
                                                    program.assign(
                                                            "i",
                                                            program.addition(
                                                                    List.of(
                                                                            program.variable("i"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    "c#result",
                                    program.constant(0)),
                            program.stop()));
  }

  public List<String> integerVariables() {
    return List.of("i", "N");
  }

  public List<String> arrayVariables() {
    return List.of("a");
  }

}
