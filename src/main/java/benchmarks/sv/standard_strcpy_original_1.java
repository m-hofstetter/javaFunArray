package benchmarks.sv;

import java.util.List;
import java.util.Set;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;

public class standard_strcpy_original_1 implements Benchmark {
  public <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program) {
    return
            program.block(
                    List.of(
                            program.assign(
                                    "i",
                                    program.constant(0)),
                            program.assign(
                                    "i",
                                    program.constant(0)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("i"),
                                            program.constant(100000)),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "src",
                                                                    program.variable("i")),
                                                            program.variable("__VERIFIER_nondet_int₁")),
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
                                    program.unequalTo(
                                            program.arrayElement(
                                                    "src",
                                                    program.variable("i")),
                                            program.constant(0)),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "dst",
                                                                    program.variable("i")),
                                                            program.arrayElement(
                                                                    "src",
                                                                    program.variable("i"))),
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
                                    program.unequalTo(
                                            program.arrayElement(
                                                    "src",
                                                    program.variable("i")),
                                            program.constant(0)),
                                    program.block(
                                            List.of(
                                                    program.assert_(
                                                            program.equalTo(
                                                                    program.arrayElement(
                                                                            "dst",
                                                                            program.variable("i")),
                                                                    program.arrayElement(
                                                                            "src",
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
    return List.of("i", "__VERIFIER_nondet_int₁");
  }

  public List<String> arrayVariables() {
    return List.of("src", "dst");
  }

}
