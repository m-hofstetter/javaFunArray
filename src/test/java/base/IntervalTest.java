package base;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import base.infint.InfInt;
import base.interval.Interval;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

public class IntervalTest {

  static final Interval ZERO = Interval.of(0, 0);
  static final Interval ONE = Interval.of(1, 1);
  static final Interval NEG_ONE = Interval.of(-1, -1);
  static final Interval ZERO_TO_ONE = Interval.of(0, 1);
  static final Interval ZERO_TO_TWO = Interval.of(0, 2);
  static final Interval NEG_ONE_TO_ONE = Interval.of(-1, 1);
  static final Interval ONE_TO_THREE = Interval.of(1, 3);
  static final Interval ZERO_TO_THREE = Interval.of(0, 3);
  static final Interval ONE_TO_TWO = Interval.of(1, 2);
  static final Interval NEG_INF_TO_POS_INF = Interval.of(InfInt.negInf(), InfInt.posInf());
  static final Interval ZERO_TO_POS_INF = Interval.of(0, InfInt.posInf());
  static final Interval NEG_INF_TO_ZERO = Interval.of(InfInt.negInf(), 0);
  static final Interval POS_ONE_TO_POS_INF = Interval.of(1, InfInt.posInf());
  static final Interval NEG_INF_TO_NEG_ONE = Interval.of(InfInt.negInf(), -1);


  static Stream<Arguments> provideArgumentsForToStringTest() {
    return Stream.of(
            Arguments.of(NEG_INF_TO_POS_INF, "[-∞, ∞]"),
            Arguments.of(ZERO_TO_POS_INF, "[0, ∞]"),
            Arguments.of(NEG_INF_TO_ZERO, "[-∞, 0]"),
            Arguments.of(POS_ONE_TO_POS_INF, "[1, ∞]"),
            Arguments.of(NEG_INF_TO_NEG_ONE, "[-∞, -1]")
    );
  }

  static Stream<Arguments> provideArgumentsForJoinTest() {
    return Stream.of(
            Arguments.of(ZERO, ZERO, ZERO),
            Arguments.of(ZERO, ONE, ZERO_TO_ONE),
            Arguments.of(ZERO_TO_TWO, ONE_TO_THREE, ZERO_TO_THREE),
            Arguments.of(NEG_INF_TO_ZERO, ZERO, NEG_INF_TO_ZERO),
            Arguments.of(NEG_INF_TO_POS_INF, ZERO, NEG_INF_TO_POS_INF)
    );
  }

  static Stream<Arguments> provideArgumentsForMeetTest() {
    return Stream.of(
            Arguments.of(ZERO, ZERO, ZERO),
            Arguments.of(ZERO, ONE, Interval.unreachable()),
            Arguments.of(ZERO_TO_TWO, ONE_TO_THREE, ONE_TO_TWO),
            Arguments.of(NEG_INF_TO_POS_INF, ZERO, ZERO)
    );
  }

  static Stream<Arguments> provideArgumentsForWidenTest() {
    return Stream.of(
            Arguments.of(ZERO, ONE, ZERO_TO_POS_INF),
            Arguments.of(ZERO, NEG_ONE, NEG_INF_TO_ZERO),
            Arguments.of(ZERO, NEG_ONE_TO_ONE, NEG_INF_TO_POS_INF),
            Arguments.of(ZERO, ZERO, ZERO)
    );
  }

  static Stream<Arguments> provideArgumentsForNarrowTest() {
    return Stream.of(
            Arguments.of(ZERO, ONE, ZERO),
            Arguments.of(ZERO_TO_TWO, ONE_TO_THREE, ZERO_TO_TWO),
            Arguments.of(ZERO, ZERO, ZERO),
            Arguments.of(ZERO_TO_POS_INF, ZERO_TO_ONE, ZERO_TO_ONE),
            Arguments.of(ZERO_TO_POS_INF, ONE_TO_TWO, ZERO_TO_TWO)
    );
  }

  @ParameterizedTest(name = "{1}")
  @MethodSource("provideArgumentsForToStringTest")
  public void toStringTest(Interval interval, String expected) {
    assertEquals(expected, interval.toString());
  }

  @ParameterizedTest(name = "{0} ⊔ {1} = {2}")
  @MethodSource("provideArgumentsForJoinTest")
  public void joinTest(Interval intervalA, Interval intervalB, Interval expected) {
    var actual = intervalA.join(intervalB);
    assertEquals(expected, actual);
  }

  @ParameterizedTest(name = "{0} ⊓ {1} = {2}")
  @MethodSource("provideArgumentsForMeetTest")
  public void meetTest(Interval intervalA, Interval intervalB, Interval expected) {
    var actual = intervalA.meet(intervalB);
    assertEquals(expected, actual);
  }

  @ParameterizedTest(name = "{0} ▽ {1} = {2}")
  @MethodSource("provideArgumentsForWidenTest")
  public void widenTest(Interval intervalA, Interval intervalB, Interval expected) {
    var actual = intervalA.widen(intervalB);
    assertEquals(expected, actual);
  }

  @ParameterizedTest(name = "{0} △ {1} = {2}")
  @MethodSource("provideArgumentsForNarrowTest")
  public void narrowTest(Interval intervalA, Interval intervalB, Interval expected) {
    var actual = intervalA.narrow(intervalB);
    assertEquals(expected, actual);
  }

  @Test
  public void addTest() {
    assertThat(Interval.of(0).add(Interval.of(1))).isEqualTo(Interval.of(1));
    assertThat(Interval.of(0).add(Interval.of(0, 1))).isEqualTo(Interval.of(0, 1));
    assertThat(Interval.of(0).add(Interval.of(0, InfInt.posInf()))).isEqualTo(Interval.of(0, InfInt.posInf()));
  }

  @Test
  public void inverseTest() {
    assertThat(Interval.of(0).negate()).isEqualTo(Interval.of(0));
    assertThat(Interval.of(1, 2).negate()).isEqualTo(Interval.of(-2, -1));
    assertThat(Interval.of(0, InfInt.posInf()).negate()).isEqualTo(Interval.of(InfInt.negInf(), 0));
  }

  @Test
  public void inequalityTest() {
    assertThat(Interval.of(0, 10).satisfyLessEqualThan(Interval.of(0, 5))).isEqualTo(Interval.of(0, 5));
    assertThat(Interval.of(0, 10).satisfyLessEqualThan(Interval.of(-10, -5))).isEqualTo(Interval.unreachable());
    assertThat(Interval.of(0, 10).satisfyLessEqualThan(Interval.of(15, 20))).isEqualTo(Interval.of(0, 10));
    assertThat(Interval.of(0, 10).satisfyLessEqualThan(Interval.of(15, InfInt.posInf()))).isEqualTo(Interval.of(0, 10));
    assertThat(Interval.of(0, 10).satisfyLessEqualThan(Interval.unreachable())).isEqualTo(Interval.unreachable());

    assertThat(Interval.of(0, 10).satisfyGreaterEqualThan(Interval.of(5, 10))).isEqualTo(Interval.of(5, 10));
    assertThat(Interval.of(0, 10).satisfyGreaterEqualThan(Interval.of(15, 20))).isEqualTo(Interval.unreachable());
    assertThat(Interval.of(0, 10).satisfyGreaterEqualThan(Interval.of(-10, -5))).isEqualTo(Interval.of(0, 10));
    assertThat(Interval.of(0, 10).satisfyGreaterEqualThan(Interval.of(InfInt.negInf(), 5))).isEqualTo(Interval.of(0, 10));
    assertThat(Interval.of(0, 10).satisfyGreaterEqualThan(Interval.unreachable())).isEqualTo(Interval.unreachable());

    assertThat(Interval.of(0, 10).satisfyLessThan(Interval.of(5, 10))).isEqualTo(Interval.of(0, 4));
    assertThat(Interval.of(0, 10).satisfyLessThan(Interval.of(15, 20))).isEqualTo(Interval.of(0, 10));
    assertThat(Interval.of(0, 10).satisfyLessThan(Interval.of(0, 0))).isEqualTo(Interval.unreachable());
    assertThat(Interval.of(0, 10).satisfyLessThan(Interval.of(InfInt.negInf(), 10))).isEqualTo(Interval.unreachable());
    assertThat(Interval.of(0, 10).satisfyLessThan(Interval.unreachable())).isEqualTo(Interval.unreachable());

    assertThat(Interval.of(0, 10).satisfyGreaterThan(Interval.of(0, 5))).isEqualTo(Interval.of(6, 10));
    assertThat(Interval.of(0, 10).satisfyGreaterThan(Interval.of(-10, -5))).isEqualTo(Interval.of(0, 10));
    assertThat(Interval.of(0, 10).satisfyGreaterThan(Interval.of(10, 10))).isEqualTo(Interval.unreachable());
    assertThat(Interval.of(0, 10).satisfyGreaterThan(Interval.of(0, InfInt.posInf()))).isEqualTo(Interval.unreachable());
    assertThat(Interval.of(0, 10).satisfyGreaterThan(Interval.unreachable())).isEqualTo(Interval.unreachable());
  }

  @Test
  public void satisfyNotEqualTest() {
    assertThat(Interval.of(0, 3).satisfyNotEqual(Interval.of(1, 2))).isEqualTo(Interval.of(0, 3));
    assertThat(Interval.of(0, 2).satisfyNotEqual(Interval.of(1, 3))).isEqualTo(Interval.of(0, 0));
    assertThat(Interval.of(1, 3).satisfyNotEqual(Interval.of(0, 2))).isEqualTo(Interval.of(3, 3));
    assertThat(Interval.of(0, 1).satisfyNotEqual(Interval.of(1, 2))).isEqualTo(Interval.of(0, 0));
    assertThat(Interval.of(1, 2).satisfyNotEqual(Interval.of(0, 1))).isEqualTo(Interval.of(2, 2));
    assertThat(Interval.of(0, 1).satisfyNotEqual(Interval.of(2, 3))).isEqualTo(Interval.of(0, 1));
    assertThat(Interval.of(2, 3).satisfyNotEqual(Interval.of(0, 1))).isEqualTo(Interval.of(2, 3));
    assertThat(Interval.of(0, 100).satisfyNotEqual(Interval.of(0, 0))).isEqualTo(Interval.of(1, 100));
    assertThat(Interval.of(1, 2).satisfyNotEqual(Interval.of(0, 3))).isEqualTo(Interval.unreachable());
    assertThat(Interval.of(1, 2).satisfyNotEqual(Interval.of(1, 2))).isEqualTo(Interval.unreachable());
  }
}
