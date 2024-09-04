package benchmarks.sv;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;
import java.util.List;
import java.util.Set;

public class standard_palindrome_ground implements Benchmark {
  public <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program) {
    return
            program.block(
                    List.of(
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
                                                                    "A",
                                                                    program.variable("i")),
                                                            program.variable("__VERIFIER_nondet_int₁")),
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
                                            program.division(
                                                    program.constant(100000),
                                                    program.constant(2))),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "A",
                                                                    program.variable("i")),
                                                            program.arrayElement(
                                                                    "A",
                                                                    program.subtraction(
                                                                            program.subtraction(
                                                                                    program.constant(100000),
                                                                                    program.variable("i")),
                                                                            program.constant(1)))),
                                                    program.assign(
                                                            "i",
                                                            program.addition(
                                                                    Set.of(
                                                                            program.variable("i"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    "x",
                                    program.constant(0)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("x"),
                                            program.division(
                                                    program.constant(100000),
                                                    program.constant(2))),
                                    program.block(
                                            List.of(
                                                    program.assert_(
                                                            program.equalTo(
                                                                    program.arrayElement(
                                                                            "A",
                                                                            program.variable("x")),
                                                                    program.arrayElement(
                                                                            "A",
                                                                            program.subtraction(
                                                                                    program.subtraction(
                                                                                            program.constant(100000),
                                                                                            program.variable("x")),
                                                                                    program.constant(1))))),
                                                    program.assign(
                                                            "x",
                                                            program.addition(
                                                                    Set.of(
                                                                            program.variable("x"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    "c#result",
                                    program.constant(0)),
                            program.stop()));
  }

  public List<String> integerVariables() {
    return List.of("i", "__VERIFIER_nondet_int₁", "x");
  }

  public List<String> arrayVariables() {
    return List.of("A");
  }

}
