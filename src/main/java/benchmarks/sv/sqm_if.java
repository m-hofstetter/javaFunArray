package benchmarks.sv;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;
import java.util.List;

public class sqm_if implements Benchmark {
  public List<String> integerVariables() {
    return List.of("N", "i");
  }

  public List<String> arrayVariables() {
    return List.of("a", "b");
  }

  public boolean allAssertionsShouldHold() {
    return true;
  }

  public <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program) {
    return
            program.block(
                    List.of(
                            program.assign(
                                    "N",
                                    program.constant(0)),
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
                            program.arrayInit(
                                    "a",
                                    program.variable("N")),
                            program.arrayInit(
                                    "b",
                                    program.variable("N")),
                            program.assign(
                                    "i",
                                    program.constant(0)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("i"),
                                            program.variable("N")),
                                    program.block(
                                            List.of(
                                                    program.if_(
                                                            program.equalTo(
                                                                    program.variable("i"),
                                                                    program.constant(0)),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    program.arrayElement(
                                                                                            "b",
                                                                                            program.constant(0)),
                                                                                    program.constant(1)))),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    program.arrayElement(
                                                                                            "b",
                                                                                            program.variable("i")),
                                                                                    program.addition(
                                                                                            List.of(
                                                                                                    program.arrayElement(
                                                                                                            "b",
                                                                                                            program.subtraction(
                                                                                                                    program.variable("i"),
                                                                                                                    program.constant(1))),
                                                                                                    program.constant(2))))))),
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
                                            program.variable("N")),
                                    program.block(
                                            List.of(
                                                    program.if_(
                                                            program.equalTo(
                                                                    program.variable("i"),
                                                                    program.constant(0)),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    program.arrayElement(
                                                                                            "a",
                                                                                            program.constant(0)),
                                                                                    program.constant(1)))),
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
                                                                                                    program.arrayElement(
                                                                                                            "b",
                                                                                                            program.subtraction(
                                                                                                                    program.variable("i"),
                                                                                                                    program.constant(1))),
                                                                                                    program.constant(2))))))),
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
                                            program.variable("N")),
                                    program.block(
                                            List.of(
                                                    program.assert_(
                                                            program.equalTo(
                                                                    program.arrayElement(
                                                                            "a",
                                                                            program.variable("i")),
                                                                    program.multiplication(
                                                                            List.of(
                                                                                    program.addition(
                                                                                            List.of(
                                                                                                    program.variable("i"),
                                                                                                    program.constant(1))),
                                                                                    program.addition(
                                                                                            List.of(
                                                                                                    program.variable("i"),
                                                                                                    program.constant(1))))))),
                                                    program.assign(
                                                            "i",
                                                            program.addition(
                                                                    List.of(
                                                                            program.variable("i"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    "c#result",
                                    program.constant(1)),
                            program.stop()));
  }

}
