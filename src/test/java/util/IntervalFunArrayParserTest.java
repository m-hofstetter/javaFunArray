package util;

import static base.sign.Sign.SignElement.NEGATIVE;
import static base.sign.Sign.SignElement.POSITIVE;
import static base.sign.Sign.SignElement.ZERO;
import static org.assertj.core.api.Assertions.assertThat;

import base.interval.Interval;
import base.sign.Sign;
import funarray.Bound;
import funarray.Expression;
import funarray.FunArray;
import funarray.VariableReference;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class IntervalFunArrayParserTest {

  @Test
  void parseTest() {
    var f = IntervalFunArrayParser.parseIntervalFunArray("{a} [0, 0] {b} ⊥ {c d}");
    var comparand = new FunArray<>(
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
    var f = IntervalFunArrayParser.parseIntervalFunArray("{a}[0, 0] {b} ⊥{c d}");
    var comparand = new FunArray<>(
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
    var f = IntervalFunArrayParser.parseIntervalFunArray("{0} [0, 0] {b+1} ⊥ {c-1}");
    var comparand = new FunArray<>(
            List.of(
                    new Bound(new Expression(new VariableReference("0"))),
                    new Bound(new Expression(new VariableReference("b"), 1)),
                    new Bound(new Expression(new VariableReference("c"), -1))
            ), List.of(
            Interval.of(0), Interval.unreachable()
    ), List.of(
            false, false
    )
    );
    assertThat(f).isEqualTo(comparand);
  }

  @Test
  void parseSignTest() {
    var f = IntervalFunArrayParser.parseSignFunArray("{a} <0 {b} ⊤ {c}");
    var comparand = new FunArray<>(
            List.of(
                    new Bound(new Expression(new VariableReference("a"))),
                    new Bound(new Expression(new VariableReference("b"))),
                    new Bound(new Expression(new VariableReference("c")))
            ), List.of(
            new Sign(Set.of(NEGATIVE)), new Sign(Set.of(NEGATIVE, ZERO, POSITIVE))
    ), List.of(
            false, false
    )
    );
    assertThat(f).isEqualTo(comparand);
  }
}
