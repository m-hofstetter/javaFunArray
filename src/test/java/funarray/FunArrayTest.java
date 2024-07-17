package funarray;

import static org.assertj.core.api.Assertions.assertThat;
import static util.IntervalFunArrayParser.parseIntervalFunArray;

import base.infint.InfInt;
import base.interval.Interval;
import org.junit.jupiter.api.Test;

public class FunArrayTest {

  @Test
  void addToVariableTest() {
    var funArray = parseIntervalFunArray("{a} [-∞, ∞] {b}");
    var modified = funArray.addToVariable(new VariableReference("b"), InfInt.of(3));
    assertThat(modified).isEqualTo(parseIntervalFunArray("{a} [-∞, ∞] {b-3}"));
  }

  @Test
  void insertTest() {

    var funArray = parseIntervalFunArray("{0} [-∞, ∞] {A.length}");

    funArray = funArray.insert(new Expression(new VariableReference("0")), Interval.of(0));
    assertThat(funArray).isEqualTo(
            parseIntervalFunArray("{0} [0, 0] {1} [-∞, ∞] {A.length}?")
    );

    funArray = funArray.insert(new Expression(new VariableReference("0"), 1), Interval.of(0, 0));
    assertThat(funArray).isEqualTo(
            parseIntervalFunArray("{0} [0, 0] {1} [0, 0] {2} [-∞, ∞] {A.length}?")
    );

    funArray = funArray.insert(new Expression(new VariableReference("A.length"), -1), Interval.of(0));
    assertThat(funArray).isEqualTo(
            parseIntervalFunArray("{0} [0, 0] {1} [-∞, ∞] {A.length-1}? [0, 0] {A.length}")
    );
  }

  /**
   * From Cousot, Cousot, Logozzo (POPL 2011), Example 8
   */
  @Test
  void unifyTest() {
    var arrayA = parseIntervalFunArray("{0 i} [-∞, ∞] {n}");
    var arrayB = parseIntervalFunArray("{0 i-1} [0, 0] {1 i} [-∞, ∞] {n}?");

    var expectedA = parseIntervalFunArray("{0} ⊥ {i}? [-∞, ∞] {n}");
    var expectedB = parseIntervalFunArray("{0} [0, 0] {i} [-∞, ∞] {n}?");

    var unifiedArrays = arrayA.unify(arrayB, Interval.unreachable(), Interval.unreachable());

    assertThat(unifiedArrays.resultThis()).isEqualTo(expectedA);
    assertThat(unifiedArrays.resultOther()).isEqualTo(expectedB);
  }

  @Test
  void complexUnifyTest() {
    var arrayA = parseIntervalFunArray("{0} [-∞, ∞] {a-1} [-∞, ∞] {a} [-∞, ∞] {b} [-∞, ∞] {A.length}");
    var arrayB = parseIntervalFunArray("{0} [-∞, ∞] {a b} [-∞, ∞] {b+1} [-∞, ∞] {A.length}");

    var expectedA = parseIntervalFunArray("{0} [-∞, ∞] {a} [-∞, ∞] {b} [-∞, ∞] {A.length}");
    var expectedB = parseIntervalFunArray("{0} [-∞, ∞] {a} ⊥ {b}? [-∞, ∞] {A.length}");

    var unifiedArrays = arrayA.unify(arrayB, Interval.unreachable(), Interval.unreachable());

    assertThat(unifiedArrays.resultThis()).isEqualTo(expectedA);
    assertThat(unifiedArrays.resultOther()).isEqualTo(expectedB);
  }

  @Test
  void joinTest() {
    var arrayA = parseIntervalFunArray("{0 i} [-∞, ∞] {n}");
    var arrayB = parseIntervalFunArray("{0 i-1} [0, 0] {1 i} [-∞, ∞] {n}?");
    var expected = parseIntervalFunArray("{0} [0, 0] {i}? [-∞, ∞] {n}?");
    var joined = arrayA.join(arrayB, Interval.unreachable());
    assertThat(joined).isEqualTo(expected);
  }

  @Test
  void getTest() {
    var funArray = parseIntervalFunArray("{0} [0, 0] {1} [1, 1] {2}");

    assertThat(funArray.get(new Expression(new VariableReference("0"))))
            .isEqualTo(Interval.of(0));

    assertThat(funArray.get(new Expression(new VariableReference("0"), 1)))
            .isEqualTo(Interval.of(1));
  }
}
