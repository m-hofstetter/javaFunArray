package benchmarks.sv;

import java.util.List;
import java.util.Set;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;

public class standard_seq_init_ground implements Benchmark {
  public <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program) {
    return
            program.block(
                    List.of(
                            program.assign(
                                    "i",
                                    program.constant(1)),
                            program.assign(
                                    program.arrayElement(
                                            "a",
                                            program.constant(0)),
                                    program.constant(7)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("i"),
                                            program.constant(100000)),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "a",
                                                                    program.variable("i")),
                                                            program.addition(
                                                                    List.of(
                                                                            program.arrayElement(
                                                                                    "a",
                                                                                    program.subtraction(
                                                                                            program.variable("i"),
                                                                                            program.constant(1))),
                                                                            program.constant(1)))),
                                                    program.assign(
                                                            "i",
                                                            program.addition(
                                                                    List.of(
                                                                            program.variable("i"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    "x",
                                    program.constant(1)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("x"),
                                            program.constant(100000)),
                                    program.block(
                                            List.of(
                                                    program.assert_(
                                                            program.lessEqualThan(
                                                                    program.arrayElement(
                                                                            "a",
                                                                            program.subtraction(
                                                                                    program.variable("x"),
                                                                                    program.constant(1))),
                                                                    program.arrayElement(
                                                                            "a",
                                                                            program.variable("x")))),
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
    return List.of("i", "x");
  }

  public List<String> arrayVariables() {
    return List.of("a");
  }

}
