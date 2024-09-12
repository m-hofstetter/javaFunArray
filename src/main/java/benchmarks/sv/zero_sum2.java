package benchmarks.sv;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;
import java.util.List;

public class zero_sum2 implements Benchmark {
  public List<String> integerVariables() {
    return List.of("SIZE", "i", "sum", "nondet_short₁");
  }

  public List<String> arrayVariables() {
    return List.of("a");
  }

  public boolean allAssertionsShouldHold() {
    return true;
  }

  public <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program) {
    return
            program.block(
                    List.of(
                            program.assign(
                                    "SIZE",
                                    program.constant(0)),
                            program.havoc(
                                    program.variable("SIZE")),
                            program.if_(
                                    program.lessThan(
                                            program.constant(1),
                                            program.variable("SIZE")),
                                    program.block(
                                            List.of(
                                                    program.arrayInit(
                                                            "a",
                                                            program.variable("SIZE")),
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
                                                                                    program.variable("nondet_short₁")),
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
                                                            program.lessThan(
                                                                    program.variable("i"),
                                                                    program.variable("SIZE")),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    "sum",
                                                                                    program.addition(
                                                                                            List.of(
                                                                                                    program.variable("sum"),
                                                                                                    program.arrayElement(
                                                                                                            "a",
                                                                                                            program.variable("i"))))),
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
                                                                                            List.of(
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
                                                                                            List.of(
                                                                                                    program.variable("sum"),
                                                                                                    program.arrayElement(
                                                                                                            "a",
                                                                                                            program.variable("i"))))),
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
                                                                                            List.of(
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

}
