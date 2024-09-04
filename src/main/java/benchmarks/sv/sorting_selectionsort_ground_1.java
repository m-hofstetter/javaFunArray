package benchmarks.sv;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;
import java.util.List;
import java.util.Set;

public class sorting_selectionsort_ground_1 implements Benchmark {
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
                                                                    "a",
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
                                            program.constant(100000)),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            "k",
                                                            program.addition(
                                                                    Set.of(
                                                                            program.variable("i"),
                                                                            program.constant(1)))),
                                                    program.assign(
                                                            "s",
                                                            program.variable("i")),
                                                    program.while_(
                                                            program.lessThan(
                                                                    program.variable("k"),
                                                                    program.constant(100000)),
                                                            program.block(
                                                                    List.of(
                                                                            program.if_(
                                                                                    program.lessThan(
                                                                                            program.arrayElement(
                                                                                                    "a",
                                                                                                    program.variable("k")),
                                                                                            program.arrayElement(
                                                                                                    "a",
                                                                                                    program.variable("s"))),
                                                                                    program.block(
                                                                                            List.of(
                                                                                                    program.assign(
                                                                                                            "s",
                                                                                                            program.variable("k")))),
                                                                                    program.block(
                                                                                            List.of())),
                                                                            program.assign(
                                                                                    "k",
                                                                                    program.addition(
                                                                                            Set.of(
                                                                                                    program.variable("k"),
                                                                                                    program.constant(1))))))),
                                                    program.if_(
                                                            program.unequalTo(
                                                                    program.variable("s"),
                                                                    program.variable("i")),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    "tmp",
                                                                                    program.arrayElement(
                                                                                            "a",
                                                                                            program.variable("s"))),
                                                                            program.assign(
                                                                                    program.arrayElement(
                                                                                            "a",
                                                                                            program.variable("s")),
                                                                                    program.arrayElement(
                                                                                            "a",
                                                                                            program.variable("i"))),
                                                                            program.assign(
                                                                                    program.arrayElement(
                                                                                            "a",
                                                                                            program.variable("i")),
                                                                                    program.variable("tmp")))),
                                                            program.block(
                                                                    List.of())),
                                                    program.assign(
                                                            "x",
                                                            program.constant(0)),
                                                    program.while_(
                                                            program.lessThan(
                                                                    program.variable("x"),
                                                                    program.variable("i")),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    "y",
                                                                                    program.addition(
                                                                                            Set.of(
                                                                                                    program.variable("x"),
                                                                                                    program.constant(1)))),
                                                                            program.while_(
                                                                                    program.lessThan(
                                                                                            program.variable("y"),
                                                                                            program.variable("i")),
                                                                                    program.block(
                                                                                            List.of(
                                                                                                    program.assert_(
                                                                                                            program.lessEqualThan(
                                                                                                                    program.arrayElement(
                                                                                                                            "a",
                                                                                                                            program.variable("x")),
                                                                                                                    program.arrayElement(
                                                                                                                            "a",
                                                                                                                            program.variable("y")))),
                                                                                                    program.assign(
                                                                                                            "y",
                                                                                                            program.addition(
                                                                                                                    Set.of(
                                                                                                                            program.variable("y"),
                                                                                                                            program.constant(1))))))),
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
                                                                    program.constant(100000)),
                                                            program.block(
                                                                    List.of(
                                                                            program.assert_(
                                                                                    program.lessEqualThan(
                                                                                            program.arrayElement(
                                                                                                    "a",
                                                                                                    program.variable("i")),
                                                                                            program.arrayElement(
                                                                                                    "a",
                                                                                                    program.variable("x")))),
                                                                            program.assign(
                                                                                    "x",
                                                                                    program.addition(
                                                                                            Set.of(
                                                                                                    program.variable("x"),
                                                                                                    program.constant(1))))))),
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
                                            program.constant(100000)),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            "y",
                                                            program.addition(
                                                                    Set.of(
                                                                            program.variable("x"),
                                                                            program.constant(1)))),
                                                    program.while_(
                                                            program.lessThan(
                                                                    program.variable("y"),
                                                                    program.constant(100000)),
                                                            program.block(
                                                                    List.of(
                                                                            program.assert_(
                                                                                    program.lessEqualThan(
                                                                                            program.arrayElement(
                                                                                                    "a",
                                                                                                    program.variable("x")),
                                                                                            program.arrayElement(
                                                                                                    "a",
                                                                                                    program.variable("y")))),
                                                                            program.assign(
                                                                                    "y",
                                                                                    program.addition(
                                                                                            Set.of(
                                                                                                    program.variable("y"),
                                                                                                    program.constant(1))))))),
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
    return List.of("y", "tmp", "s", "__VERIFIER_nondet_int₁", "i", "k", "x");
  }

  public List<String> arrayVariables() {
    return List.of("a");
  }

}
