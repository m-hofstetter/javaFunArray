package util;

import static abstractdomain.sign.value.Sign.SignElement.NEGATIVE;
import static abstractdomain.sign.value.Sign.SignElement.POSITIVE;
import static abstractdomain.sign.value.Sign.SignElement.ZERO;
import static org.assertj.core.api.Assertions.assertThat;

import abstractdomain.interval.value.Interval;
import abstractdomain.sign.value.Sign;
import funarray.Bound;
import funarray.FunArray;
import funarray.NormalExpression;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class IntervalFunArrayParserTest {

  @Test
  void parseTest() {
    var f = IntervalFunArrayParser.parseIntervalFunArray("{a} [0, 0] {b} ⊥ {c d}");
    var comparand = new FunArray<>(
            List.of(
                    new Bound(new NormalExpression("a")),
                    new Bound(new NormalExpression("b")),
                    new Bound(Set.of(new NormalExpression("c"), new NormalExpression("d")))
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
                    new Bound(new NormalExpression("a")),
                    new Bound(new NormalExpression("b")),
                    new Bound(Set.of(new NormalExpression("c"), new NormalExpression("d")))
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
                    new Bound(new NormalExpression("0")),
                    new Bound(new NormalExpression("b", 1)),
                    new Bound(new NormalExpression("c", -1))
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
                    new Bound(new NormalExpression("a")),
                    new Bound(new NormalExpression("b")),
                    new Bound(new NormalExpression("c"))
            ), List.of(
            new Sign(Set.of(NEGATIVE)), new Sign(Set.of(NEGATIVE, ZERO, POSITIVE))
    ), List.of(
            false, false
    )
    );
    assertThat(f).isEqualTo(comparand);
  }
}
