package base;

import static base.Interval.UNREACHABLE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import base.infint.InfInt;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

public class IntervalTest {

  static Interval ZERO = new Interval(0, 0);
  static Interval ONE = new Interval(1, 1);
  static Interval NEG_ONE = new Interval(-1, -1);
  static Interval ZERO_TO_ONE = new Interval(0, 1);
  static Interval ZERO_TO_TWO = new Interval(0, 2);
  static Interval NEG_ONE_TO_ONE = new Interval(-1, 1);
  static Interval ONE_TO_THREE = new Interval(1, 3);
  static Interval ZERO_TO_THREE = new Interval(0, 3);
  static Interval ONE_TO_TWO = new Interval(1, 2);
  static Interval NEG_INF_TO_POS_INF = new Interval(InfInt.negInf(), InfInt.posInf());
  static Interval ZERO_TO_POS_INF = new Interval(0, InfInt.posInf());
  static Interval NEG_INF_TO_ZERO = new Interval(InfInt.negInf(), 0);
  static Interval POS_ONE_TO_POS_INF = new Interval(1, InfInt.posInf());
  static Interval NEG_INF_TO_NEG_ONE = new Interval(InfInt.negInf(), -1);


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
            Arguments.of(ZERO, ONE, UNREACHABLE),
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

}
