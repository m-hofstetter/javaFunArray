package benchmarks;

import abstractdomain.interval.value.Interval;
import analysis.common.AnalysisContext;
import analysis.interval.IntervalAnalysisContext;
import benchmarks.sv.condm;
import benchmarks.sv.copysome2_1;
import benchmarks.sv.indp5;
import benchmarks.sv.sina4f;
import benchmarks.sv.standard_compare_ground;
import benchmarks.sv.standard_password_ground;
import funarray.state.ReachableState;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class BenchmarkTest {

  private static final AnalysisContext<Interval, Interval> CONTEXT = IntervalAnalysisContext.INSTANCE;

  static Stream<Arguments> provideArguments() {
    return Stream.of(
            Arguments.of(new standard_compare_ground()),
            Arguments.of(new condm()),
            Arguments.of(new copysome2_1()),
            Arguments.of(new indp5()),
            Arguments.of(new sina4f()),
            Arguments.of(new standard_password_ground())

    );
  }

  @ParameterizedTest
  @MethodSource("provideArguments")
  void test(Benchmark benchmark) {
    var builder = new IntervalAnalysisBenchmarkBuilder();

    var analysis = benchmark.statement(builder);

    var startingState = new ReachableState<>(
            benchmark.integerVariables(),
            benchmark.arrayVariables(),
            CONTEXT
    );

    var result = analysis.run(startingState);
    System.out.println(result);
  }
}
