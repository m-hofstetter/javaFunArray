package benchmarks.sv;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;
import java.util.List;

public class copysome2_1 implements Benchmark {
  public List<String> integerVariables() {
    return List.of("z", "i", "nondet_int₃", "x", "nondet_int₁", "nondet_int₂");
  }

  public List<String> arrayVariables() {
    return List.of("a1", "a2", "a3");
  }

  public boolean allAssertionsShouldHold() {
    return true;
  }

  public <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program) {
    return
            program.block(
                    List.of(
                            program.arrayInit(
                                    "a1",
                                    program.constant(200000)),
                            program.arrayInit(
                                    "a2",
                                    program.constant(200000)),
                            program.arrayInit(
                                    "a3",
                                    program.constant(200000)),
                            program.assign(
                                    "z",
                                    program.constant(150000)),
                            program.assign(
                                    "i",
                                    program.constant(0)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("i"),
                                            program.constant(200000)),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "a1",
                                                                    program.variable("i")),
                                                            program.variable("nondet_int₁")),
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "a2",
                                                                    program.variable("i")),
                                                            program.variable("nondet_int₂")),
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "a3",
                                                                    program.variable("i")),
                                                            program.variable("nondet_int₃")),
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
                                            program.constant(200000)),
                                    program.block(
                                            List.of(
                                                    program.if_(
                                                            program.unequalTo(
                                                                    program.variable("i"),
                                                                    program.variable("z")),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    program.arrayElement(
                                                                                            "a2",
                                                                                            program.variable("i")),
                                                                                    program.arrayElement(
                                                                                            "a1",
                                                                                            program.variable("i"))))),
                                                            program.block(
                                                                    List.of())),
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
                                            program.constant(200000)),
                                    program.block(
                                            List.of(
                                                    program.if_(
                                                            program.unequalTo(
                                                                    program.variable("i"),
                                                                    program.variable("z")),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    program.arrayElement(
                                                                                            "a3",
                                                                                            program.variable("i")),
                                                                                    program.arrayElement(
                                                                                            "a2",
                                                                                            program.variable("i"))))),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    program.arrayElement(
                                                                                            "a3",
                                                                                            program.variable("i")),
                                                                                    program.arrayElement(
                                                                                            "a1",
                                                                                            program.variable("i")))))),
                                                    program.assign(
                                                            "i",
                                                            program.addition(
                                                                    List.of(
                                                                            program.variable("i"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    "x",
                                    program.constant(0)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("x"),
                                            program.constant(200000)),
                                    program.block(
                                            List.of(
                                                    program.assert_(
                                                            program.equalTo(
                                                                    program.arrayElement(
                                                                            "a1",
                                                                            program.variable("x")),
                                                                    program.arrayElement(
                                                                            "a3",
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

}
