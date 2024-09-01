package benchmarks;

import abstractdomain.interval.value.Interval;
import analysis.common.AnalysisContext;
import analysis.interval.IntervalAnalysisContext;
import benchmarks.sv_benchmarks.standard_compare_ground;
import funarray.EnvState;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class BenchmarkTest {

  private static final AnalysisContext<Interval, Interval> CONTEXT = IntervalAnalysisContext.INSTANCE;

  static Stream<Arguments> provideArguments() {
    return Stream.of(Arguments.of(new standard_compare_ground()));
  }

  @ParameterizedTest
  @MethodSource("provideArguments")
  void test(Benchmark benchmark) {
    var builder = new IntervalAnalysisBenchmarkBuilder();

    var analysis = benchmark.statement(builder);

    var startingState = new EnvState<>(
            benchmark.integerVariables(), CONTEXT.getVariableDomain().getUnknown(),
            benchmark.arrayVariables(), CONTEXT.getElementDomain().getUnknown()
    );

    var result = analysis.run(startingState);
    System.out.println(result.protocol());
  }
}
