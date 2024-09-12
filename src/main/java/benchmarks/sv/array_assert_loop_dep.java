package benchmarks.sv;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;
import java.util.List;

public class array_assert_loop_dep implements Benchmark {
    public List<String> integerVariables() {
        return List.of("i");
    }

    public List<String> arrayVariables() {
        return List.of("a");
    }

    public boolean allAssertionsShouldHold() {
        return false;
    }

    public <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program) {
        return
            program.block(
                    List.of(
                            program.arrayInit(
                                    "a",
                                    program.constant(100000)),
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
                                                            program.constant(10)),
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
                                            program.constant(100000)),
                                    program.block(
                                            List.of(
                                                    program.assert_(
                                                            program.equalTo(
                                                                    program.arrayElement(
                                                                            "a",
                                                                            program.variable("i")),
                                                                    program.constant(10))),
                                                    program.if_(
                                                            program.unequalTo(
                                                                    program.addition(
                                                                            List.of(
                                                                                    program.variable("i"),
                                                                                    program.constant(1))),
                                                                    program.constant(15000)),
                                                            program.block(
                                                                    List.of(
                                                                            program.assign(
                                                                                    program.arrayElement(
                                                                                            "a",
                                                                                            program.addition(
                                                                                                    List.of(
                                                                                                            program.variable("i"),
                                                                                                            program.constant(1)))),
                                                                                    program.constant(20)))),
                                                            program.block(
                                                                    List.of())),
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
