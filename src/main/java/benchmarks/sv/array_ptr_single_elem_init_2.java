package benchmarks.sv;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;
import java.util.List;
import java.util.Set;

public class array_ptr_single_elem_init_2 implements Benchmark {
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
                                                            "q",
                                                            program.variable("__VERIFIER_nondet_int₁")),
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "a",
                                                                    program.variable("i")),
                                                            program.constant(0)),
                                                    program.if_(
                                                            program.equalTo(
                                                                    program.variable("q"),
                                                                    program.constant(0)),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    program.arrayElement(
                                                                                            "a",
                                                                                            program.variable("i")),
                                                                                    program.constant(1)),
                                                                            program.assign(
                                                                                    program.arrayElement(
                                                                                            "b",
                                                                                            program.variable("i")),
                                                                                    program.modulo(
                                                                                            program.variable("i"),
                                                                                            program.constant(2))))),
                                                            program.block(
                                                                    List.of())),
                                                    program.if_(
                                                            program.unequalTo(
                                                                    program.arrayElement(
                                                                            "a",
                                                                            program.variable("i")),
                                                                    program.constant(0)),
                                                            program.block(
                                                                    List.of(
                                                                            program.if_(
                                                                                    program.equalTo(
                                                                                            program.arrayElement(
                                                                                                    "b",
                                                                                                    program.variable("i")),
                                                                                            program.constant(0)),
                                                                                    program.block(
                                                                                            List.of(
                                                                                                    program.assign(
                                                                                                            program.arrayElement(
                                                                                                                    "c",
                                                                                                                    program.variable("i")),
                                                                                                            program.constant(0)))),
                                                                                    program.block(
                                                                                            List.of(
                                                                                                    program.assign(
                                                                                                            program.arrayElement(
                                                                                                                    "c",
                                                                                                                    program.variable("i")),
                                                                                                            program.constant(1))))))),
                                                            program.block(
                                                                    List.of())),
                                                    program.assign(
                                                            "i",
                                                            program.addition(
                                                                    Set.of(
                                                                            program.variable("i"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    program.arrayElement(
                                            "a",
                                            program.constant(15000)),
                                    program.constant(1)),
                            program.assign(
                                    "i",
                                    program.constant(0)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("i"),
                                            program.constant(100000)),
                                    program.block(
                                            List.of(
                                                    program.if_(
                                                            program.equalTo(
                                                                    program.variable("i"),
                                                                    program.constant(15000)),
                                                            program.block(
                                                                    List.of(
                                                                            program.assert_(
                                                                                    program.unequalTo(
                                                                                            program.arrayElement(
                                                                                                    "a",
                                                                                                    program.variable("i")),
                                                                                            program.constant(1))))),
                                                            program.block(
                                                                    List.of())),
                                                    program.assign(
                                                            "i",
                                                            program.addition(
                                                                    Set.of(
                                                                            program.variable("i"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    "c#result",
                                    program.constant(0)),
                            program.stop()));
  }

  public List<String> integerVariables() {
    return List.of("i", "__VERIFIER_nondet_int₁", "q");
  }

  public List<String> arrayVariables() {
    return List.of("a", "b", "c");
  }

}
