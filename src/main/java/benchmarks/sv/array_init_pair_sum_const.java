package benchmarks.sv;

import java.util.List;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;

public class array_init_pair_sum_const implements Benchmark {
    public List<String> integerVariables() {
        return List.of("i", "N");
    }

    public List<String> arrayVariables() {
        return List.of("a", "b", "c");
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
                                    program.constant(100000)),
                            program.arrayInit(
                                    "a",
                                    program.variable("N")),
                            program.arrayInit(
                                    "b",
                                    program.variable("N")),
                            program.arrayInit(
                                    "c",
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
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "a",
                                                                    program.variable("i")),
                                                            program.constant(1)),
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "b",
                                                                    program.variable("i")),
                                                            program.constant(2)),
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
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "c",
                                                                    program.variable("i")),
                                                            program.addition(
                                                                    List.of(
                                                                            program.arrayElement(
                                                                                    "a",
                                                                                    program.variable("i")),
                                                                            program.arrayElement(
                                                                                    "b",
                                                                                    program.variable("i"))))),
                                                    program.assign(
                                                            "i",
                                                            program.addition(
                                                                    List.of(
                                                                            program.variable("i"),
                                                                            program.constant(1))))))),
                            program.assign(
                                    "i",
                                    program.constant(1)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("i"),
                                            program.variable("N")),
                                    program.block(
                                            List.of(
                                                    program.assert_(
                                                            program.lessEqualThan(
                                                                    program.constant(3),
                                                                    program.arrayElement(
                                                                            "c",
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

}
