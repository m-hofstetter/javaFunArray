package condition;

import static org.assertj.core.api.Assertions.assertThat;

import abstractdomain.interval.value.Interval;
import analysis.common.condition.LessThan;
import analysis.common.expression.atom.Variable;
import analysis.interval.IntervalAnalysisContext;
import base.infint.InfInt;
import funarray.state.ReachableState;
import funarray.varref.Reference;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class ConditionTest {

  @Test
  void lessThanTest() {
    final var context = IntervalAnalysisContext.INSTANCE;
    var state = new ReachableState<>(Map.of(), Map.of(
            Reference.of("a"), Interval.of(0),
            Reference.of("b"), Interval.of(InfInt.negInf(), InfInt.posInf())
    ), context);

    var condition = new LessThan<>(
            new Variable<>("a", context),
            new Variable<>("b", context),
            context
    );

    var satisifed = condition.satisfy(state);

    assertThat(satisifed.variables().get(Reference.of("a"))).isEqualTo(Interval.of(0));
    assertThat(satisifed.variables().get(Reference.of("b"))).isEqualTo(Interval.of(1, InfInt.posInf()));
  }
}
