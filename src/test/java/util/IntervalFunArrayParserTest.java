package util;

import static org.assertj.core.api.Assertions.assertThat;

import base.interval.Interval;
import funarray.Bound;
import funarray.Expression;
import funarray.FunArray;
import funarray.VariableReference;
import org.junit.jupiter.api.Test;
import java.util.List;

public class IntervalFunArrayParserTest {

  @Test
  void parseTest() {
    var f = IntervalFunArrayParser.parse("{a} [0, 0] {b} ⊥ {c d};");
    var comparand = new FunArray<Interval>(
            List.of(
                    new Bound(new Expression(new VariableReference("a"))),
                    new Bound(new Expression(new VariableReference("b"))),
                    new Bound(new Expression(new VariableReference("c")), new Expression(new VariableReference("d")))
            ), List.of(
            Interval.of(0), Interval.unreachable()
    ), List.of(
            false, false
    )
    );
    assertThat(f).isEqualTo(comparand);
  }

  @Test
  void parseMissingWhitespaceTest() {
    var f = IntervalFunArrayParser.parse("{a}[0, 0] {b} ⊥{c d};");
    var comparand = new FunArray<Interval>(
            List.of(
                    new Bound(new Expression(new VariableReference("a"))),
                    new Bound(new Expression(new VariableReference("b"))),
                    new Bound(new Expression(new VariableReference("c")), new Expression(new VariableReference("d")))
            ), List.of(
            Interval.of(0), Interval.unreachable()
    ), List.of(
            false, false
    )
    );
    assertThat(f).isEqualTo(comparand);
  }

  @Test
  void parseConstantsInExpressionsTest() {
    var f = IntervalFunArrayParser.parse("{0} [0, 0] {b+1} ⊥ {c};");
    var comparand = new FunArray<Interval>(
            List.of(
                    new Bound(new Expression(new VariableReference("0"))),
                    new Bound(new Expression(new VariableReference("b"), 1)),
                    new Bound(new Expression(new VariableReference("c")))
            ), List.of(
            Interval.of(0), Interval.unreachable()
    ), List.of(
            false, false
    )
    );
    assertThat(f).isEqualTo(comparand);
  }
}
