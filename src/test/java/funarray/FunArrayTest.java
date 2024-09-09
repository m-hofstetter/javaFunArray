package funarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static util.IntervalFunArrayParser.parseIntervalFunArray;

import abstractdomain.interval.value.Interval;
import funarray.exception.FunArrayLogicException;
import funarray.varref.Reference;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class FunArrayTest {

  @Test
  void addToVariableTest() {
    var funArray = parseIntervalFunArray("{a} [-∞, ∞] {b}");
    var modified = funArray.insertExpression(Reference.of("b"), Set.of(new NormalExpression("b", 3)));
    assertThat(modified).isEqualTo(parseIntervalFunArray("{a} [-∞, ∞] {b-3}"));
  }

  @Test
  void insertTest() {
    var funArray = parseIntervalFunArray("{0} [-∞, ∞] {x A.length}");

    funArray = funArray.insert(new NormalExpression(0), Interval.of(0));
    assertThat(funArray).isEqualTo(
            parseIntervalFunArray("{0} [0, 0] {1} [-∞, ∞] {x A.length}?")
    );

    funArray = funArray.insert(new NormalExpression(1), Interval.of(0, 0));
    assertThat(funArray).isEqualTo(
            parseIntervalFunArray("{0} [0, 0] {1} [0, 0] {2} [-∞, ∞] {x A.length}?")
    );

    funArray = funArray.insert(new NormalExpression("A.length", -1), Interval.of(0));
    assertThat(funArray).isEqualTo(
            parseIntervalFunArray("{0} [0, 0] {1} [-∞, ∞] {x-1 A.length-1}? [0, 0] {x A.length}")
    );

    funArray = funArray.insert(new NormalExpression(0), Interval.of(0));
    assertThat(funArray).isEqualTo(
            parseIntervalFunArray("{0} [0, 0] {1} [-∞, ∞] {x-1 A.length-1}? [0, 0] {x A.length}")
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
    var arrayB = parseIntervalFunArray("{0} [-∞, ∞] {a} [-∞, ∞] {b} [-∞, ∞] {b+1} [-∞, ∞] {A.length}");

    var expectedA = parseIntervalFunArray("{0} [-∞, ∞] {a} [-∞, ∞] {b} [-∞, ∞] {A.length}");
    var expectedB = parseIntervalFunArray("{0} [-∞, ∞] {a} [-∞, ∞] {b} [-∞, ∞] {A.length}");

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

    assertThat(funArray.get(new NormalExpression(0)))
            .isEqualTo(Interval.of(0));

    assertThat(funArray.get(new NormalExpression(1)))
            .isEqualTo(Interval.of(1));
  }

  @Test
  void satisfyBoundExpressionLessEqualThanTest_alreadySatisfied() {
    var funArray = parseIntervalFunArray("{0} [-∞, ∞] {a} [-∞, ∞] {b} [-∞, ∞] {A.length}");

    funArray = funArray.satisfyBoundExpressionLessEqualThan(
            new NormalExpression("a"),
            new NormalExpression("b")
    );

    assertThat(funArray).isEqualTo(
            parseIntervalFunArray("{0} [-∞, ∞] {a} [-∞, ∞] {b} [-∞, ∞] {A.length}")
    );
  }

  @Test
  void satisfyBoundExpressionLessEqualThanTest_notAlreadySatisfied() {
    var funArray = parseIntervalFunArray("{0} [-∞, ∞] {a} [-∞, ∞] {b}? [-∞, ∞] {A.length}");

    funArray = funArray.satisfyBoundExpressionLessEqualThan(
            new NormalExpression("b"),
            new NormalExpression("a")
    );

    assertThat(funArray).isEqualTo(
            parseIntervalFunArray("{0} [-∞, ∞] {a b} [-∞, ∞] {A.length}")
    );
  }

  @Test
  void satisfyBoundExpressionLessEqualThanTest_notPossible() {
    var funArray = parseIntervalFunArray("{0} [-∞, ∞] {a} [-∞, ∞] {b} [-∞, ∞] {A.length}");

    assertThrows(FunArrayLogicException.class, () -> funArray.satisfyBoundExpressionLessEqualThan(
            new NormalExpression("b"),
            new NormalExpression("a")
    ));
  }

  @Test
  void satisfyBoundExpressionLessThanTest_removeEmptiness() {
    var funArray = parseIntervalFunArray("{0} [-∞, ∞] {a} [-∞, ∞] {b}? [-∞, ∞] {A.length}");

    funArray = funArray.satisfyBoundExpressionLessThan(
            new NormalExpression("a"),
            new NormalExpression("b")
    );

    assertThat(funArray).isEqualTo(
            parseIntervalFunArray("{0} [-∞, ∞] {a} [-∞, ∞] {b} [-∞, ∞] {A.length}")
    );
  }

  @Test
  void satisfyBoundExpressionLessThanTest_removeEmptinessNotDecidable() {
    var funArray = parseIntervalFunArray("{0} [-∞, ∞] {a} [-∞, ∞] {b}? [-∞, ∞] {c}? [-∞, ∞] {A.length}");

    funArray = funArray.satisfyBoundExpressionLessThan(
            new NormalExpression("a"),
            new NormalExpression("c")
    );

    assertThat(funArray).isEqualTo(
            parseIntervalFunArray("{0} [-∞, ∞] {a} [-∞, ∞] {b}? [-∞, ∞] {c}? [-∞, ∞] {A.length}")
    );
  }

  @Test
  void satisfyBoundExpressionLessThanTest_notPossible() {
    var funArray = parseIntervalFunArray("{0} [-∞, ∞] {a} [-∞, ∞] {b} [-∞, ∞] {A.length}");

    assertThrows(FunArrayLogicException.class, () -> funArray.satisfyBoundExpressionLessThan(
            new NormalExpression("b"),
            new NormalExpression("a")
    ));
  }

  @Test
  void toStringTest() {
    var funArray = parseIntervalFunArray("{0} [-∞, ∞] {a} [-∞, ∞] {b}? [-∞, ∞] {A.length}");
    assertThat(funArray.toString()).isEqualTo("{0} [-∞, ∞] {a} [-∞, ∞] {b}? [-∞, ∞] {A.length}");
  }

  @Test
  void insertVariableTest() {
    var funArray = parseIntervalFunArray("{0} [-∞, ∞] {a} [-∞, ∞] {A.length}");

    funArray = funArray.insertExpression(Reference.of("b"), Set.of(new NormalExpression("a")));

    assertThat(funArray).isEqualTo(
            parseIntervalFunArray("{0} [-∞, ∞] {a b} [-∞, ∞] {A.length}")
    );
  }

  @Test
  void removeVariableOccurrenceTest() {
    var funArray = parseIntervalFunArray("{0} [-∞, ∞] {a b} [-∞, ∞] {A.length}");

    funArray = funArray.removeVariableOccurrences(Reference.of("b"));

    assertThat(funArray).isEqualTo(
            parseIntervalFunArray("{0} [-∞, ∞] {a} [-∞, ∞] {A.length}")
    );
  }

  @Test
  void test() {
    var p = parseIntervalFunArray("N: {0 n p-1} ⊥ {N.length}");
  }

}
