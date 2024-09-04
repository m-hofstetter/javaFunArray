package benchmarks.sv;

import java.util.List;
import java.util.Set;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;

public class standard_partition_original_ground implements Benchmark {
  public <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program) {
    return
            program.block(
                    List.of(
                            program.assign(
                                    "a",
                                    program.constant(0)),
                            program.assign(
                                    "b",
                                    program.constant(0)),
                            program.assign(
                                    "c",
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
                                                                    "aa",
                                                                    program.variable("i")),
                                                            program.variable("__VERIFIER_nondet_int₁")),
                                                    program.assign(
                                                            "i",
                                                            program.addition(
                                                                    Set.of(
                                                                            program.variable("i"),
                                                                            program.constant(1))))))),
                            program.while_(
                                    program.lessThan(
                                            program.variable("a"),
                                            program.constant(100000)),
                                    program.block(
                                            List.of(
                                                    program.if_(
                                                            program.lessEqualThan(
                                                                    program.constant(0),
                                                                    program.arrayElement(
                                                                            "aa",
                                                                            program.variable("a"))),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    program.arrayElement(
                                                                                            "bb",
                                                                                            program.variable("b")),
                                                                                    program.arrayElement(
                                                                                            "aa",
                                                                                            program.variable("a"))),
                                                                            program.assign(
                                                                                    "b",
                                                                                    program.addition(
                                                                                            Set.of(
                                                                                                    program.variable("b"),
                                                                                                    program.constant(1)))))),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    program.arrayElement(
                                                                                            "cc",
                                                                                            program.variable("c")),
                                                                                    program.arrayElement(
                                                                                            "aa",
                                                                                            program.variable("a"))),
                                                                            program.assign(
                                                                                    "c",
                                                                                    program.addition(
                                                                                            Set.of(
                                                                                                    program.variable("c"),
                                                                                                    program.constant(1))))))),
                                                    program.assign(
                                                            "a",
                                                            program.addition(
                                                                    Set.of(
                                                                            program.variable("a"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    "x",
                                    program.constant(0)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("x"),
                                            program.variable("b")),
                                    program.block(
                                            List.of(
                                                    program.assert_(
                                                            program.lessEqualThan(
                                                                    program.constant(0),
                                                                    program.arrayElement(
                                                                            "bb",
                                                                            program.variable("x")))),
                                                    program.assign(
                                                            "x",
                                                            program.addition(
                                                                    Set.of(
                                                                            program.variable("x"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    "x",
                                    program.constant(0)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("x"),
                                            program.variable("c")),
                                    program.block(
                                            List.of(
                                                    program.assert_(
                                                            program.lessThan(
                                                                    program.arrayElement(
                                                                            "cc",
                                                                            program.variable("x")),
                                                                    program.constant(0))),
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
    return List.of("c", "__VERIFIER_nondet_int₁", "i", "b", "a", "x");
  }

  public List<String> arrayVariables() {
    return List.of("aa", "bb", "cc");
  }

}
