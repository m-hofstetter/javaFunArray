package benchmarks.sv;

import benchmarks.Benchmark;
import benchmarks.BenchmarkProgram;
import java.util.List;

public class array_tiling_tcpy implements Benchmark {
    public List<String> integerVariables() {
        return List.of("S", "i");
    }

    public List<String> arrayVariables() {
        return List.of("a", "acopy");
    }

    public boolean allAssertionsShouldHold() {
        return true;
    }

    public <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program) {
        return
            program.block(
                    List.of(
                            program.assume(
                                    program.lessThan(
                                            program.constant(1),
                                            program.variable("S"))),
                            program.assume(
                                    program.lessThan(
                                            program.variable("S"),
                                            program.constant(1073741823))),
                            program.arrayInit(
                                    "a",
                                    program.multiplication(
                                            List.of(
                                                    program.constant(2),
                                                    program.variable("S")))),
                            program.arrayInit(
                                    "acopy",
                                    program.multiplication(
                                            List.of(
                                                    program.constant(2),
                                                    program.variable("S")))),
                            program.assign(
                                    "i",
                                    program.constant(0)),
                            program.while_(
                                    program.lessThan(
                                            program.variable("i"),
                                            program.variable("S")),
                                    program.block(
                                            List.of(
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "acopy",
                                                                    program.subtraction(
                                                                            program.multiplication(
                                                                                    List.of(
                                                                                            program.constant(2),
                                                                                            program.variable("S"))),
                                                                            program.addition(
                                                                                    List.of(
                                                                                            program.variable("i"),
                                                                                            program.constant(1))))),
                                                            program.arrayElement(
                                                                    "a",
                                                                    program.subtraction(
                                                                            program.multiplication(
                                                                                    List.of(
                                                                                            program.constant(2),
                                                                                            program.variable("S"))),
                                                                            program.addition(
                                                                                    List.of(
                                                                                            program.variable("i"),
                                                                                            program.constant(1)))))),
                                                    program.assign(
                                                            program.arrayElement(
                                                                    "acopy",
                                                                    program.variable("i")),
                                                            program.arrayElement(
                                                                    "a",
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
                                    program.lessThan(
                                            program.variable("i"),
                                            program.multiplication(
                                                    List.of(
                                                            program.constant(2),
                                                            program.variable("S")))),
                                    program.block(
                                            List.of(
                                                    program.assert_(
                                                            program.equalTo(
                                                                    program.arrayElement(
                                                                            "acopy",
                                                                            program.variable("i")),
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
                                    "c#result",
                                    program.constant(0)),
                            program.stop()));
    }

}
