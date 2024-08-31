package benchmarks;

import abstractdomain.interval.value.Interval;
import analysis.common.AnalysisContext;
import analysis.interval.IntervalAnalysisContext;
import benchmarks.sv_benchmarks.standard_compare_ground;
import funarray.Bound;
import funarray.EnvState;
import funarray.FunArray;
import funarray.NormalExpression;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class BenchmarkTest {

  private static final AnalysisContext<Interval, Interval> CONTEXT = IntervalAnalysisContext.INSTANCE;

  @Test
  void test() {
    var benchmark = new standard_compare_ground();
    var builder = new IntervalAnalysisBenchmarkBuilder();

    var analysis = benchmark.statement(builder);

    var startingState = new EnvState<>(
            Map.of(
                    "a", unknownFunArray("a.length"),
                    "b", unknownFunArray("b.length")
            ),
            Map.of(
                    "__VERIFIER_nondet_int₂", CONTEXT.getVariableDomain().getUnknown(),
                    "j", CONTEXT.getVariableDomain().getUnknown(),
                    "__VERIFIER_nondet_int₁", CONTEXT.getVariableDomain().getUnknown(),
                    "x", CONTEXT.getVariableDomain().getUnknown(),
                    "rv", CONTEXT.getVariableDomain().getUnknown(),
                    "i", CONTEXT.getVariableDomain().getUnknown()
            )
    );

    var result = analysis.run(startingState);
    System.out.println(result.protocol());


  }

  private static FunArray<Interval> unknownFunArray(String lenghtVariable) {
    return new FunArray<>(
            List.of(new Bound(new NormalExpression("0")), new Bound(new NormalExpression(lenghtVariable))),
            List.of(CONTEXT.getElementDomain().getUnknown()),
            List.of(true)
    );
  }
}
