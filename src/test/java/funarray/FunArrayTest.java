package funarray;

import static funarray.util.FunArrayBuilder.buildFunArray;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import base.infint.InfInt;
import base.interval.Interval;
import org.junit.jupiter.api.Test;

public class FunArrayTest {

  static final Variable LENGTH = new Variable(0, "A.length");

  @Test
  void addToVariableTest() {
    var interval = Interval.of(0, 10);
    var length = new Expression(new Variable(interval, "A.length"), InfInt.of(0));
    var funArray = new Environment(length);

    var modified = funArray.addToVariable(length.variable(), InfInt.of(3));

    assertEquals("A: {0} [-∞, ∞] {A.length-3}\nA.length: [3, 13]", modified.toString());
  }

  @Test
  void insertTest() {
    var interval = Interval.of(0, 0);
    var length = new Expression(new Variable(interval, "A.length"), InfInt.of(0));
    var funArray = new Environment(length);
    assertThat(funArray.funArray()).isEqualTo(
            buildFunArray()
                    .bound(0)
                    .unknownValue()
                    .bound(LENGTH).build()
    );

    funArray = funArray.assignArrayElement(new Expression(0), Interval.of(0, 0));
    assertThat(funArray.funArray()).isEqualTo(
            buildFunArray()
                    .bound(0)
                    .value(0)
                    .bound(1)
                    .unknownValue()
                    .mightBeEmpty()
                    .bound(LENGTH).build()
    );

    funArray = funArray.assignArrayElement(new Expression(1), Interval.of(0, 0));
    assertThat(funArray.funArray()).isEqualTo(
            buildFunArray()
                    .bound(0)
                    .value(0)
                    .bound(1)
                    .value(0)
                    .bound(2)
                    .unknownValue()
                    .mightBeEmpty()
                    .bound(LENGTH).build()
    );

    funArray = funArray.assignArrayElement(length.increase(InfInt.of(-1)), Interval.of(0, 0));
    assertThat(funArray.funArray()).isEqualTo(
            buildFunArray()
                    .bound(0)
                    .unknownValue()
                    .mightBeEmpty()
                    .bound(new Expression(LENGTH, -1))
                    .value(0)
                    .bound(LENGTH).build()
    );
  }

  /**
   * From Cousot, Cousot, Logozzo (POPL 2011), Example 8
   */
  @Test
  void unifyTest() {

    var i = new Variable(Interval.unknown(), "i");
    var n = new Variable(Interval.unknown(), "n");

    var arrayA = buildFunArray()
            .bound(Variable.ZERO_VALUE, i)
            .unknownValue()
            .bound(n).build();

    var arrayB = buildFunArray()
            .bound(new Expression(0), new Expression(i, -1))
            .value(0)
            .bound(new Expression(1), new Expression(i))
            .unknownValue()
            .mightBeEmpty()
            .bound(n).build();

    var expectedA = buildFunArray()
            .bound(0)
            .unreachableValue()
            .mightBeEmpty()
            .bound(i)
            .unknownValue()
            .bound(n).build();

    var expectedB = buildFunArray()
            .bound(0)
            .value(0, 0)
            .bound(i)
            .unknownValue()
            .mightBeEmpty()
            .bound(n).build();

    var unifiedArrays = arrayA.unify(arrayB, Interval.unreachable(), Interval.unreachable());

    assertThat(unifiedArrays.get(0)).isEqualTo(expectedA);
    assertThat(unifiedArrays.get(1)).isEqualTo(expectedB);
  }

  @Test
  void joinTest() {
    var i = new Variable(Interval.unknown(), "i");
    var n = new Variable(Interval.unknown(), "n");

    var arrayA = buildFunArray()
            .bound(Variable.ZERO_VALUE, i)
            .unknownValue()
            .bound(n).build();

    var arrayB = buildFunArray()
            .bound(new Expression(0), new Expression(i, -1))
            .value(0)
            .bound(new Expression(1), new Expression(i))
            .unknownValue()
            .mightBeEmpty()
            .bound(n).build();

    var expected = buildFunArray()
            .bound(0)
            .value(0, 0)
            .mightBeEmpty()
            .bound(i)
            .unknownValue()
            .mightBeEmpty()
            .bound(n).build();

    var joined = arrayA.join(arrayB);
    assertThat(joined).isEqualTo(expected);
  }

  @Test
  void getTest() {

    var firstValue = Interval.of(0);
    var secondValue = Interval.of(1);

    var funArray = buildFunArray()
            .bound(0)
            .value(firstValue)
            .bound(1)
            .value(secondValue)
            .bound(2).build();

    assertThat(
            funArray.get(new Expression(0))
    ).isEqualTo(firstValue);

    assertThat(
            funArray.get(new Expression(1))
    ).isEqualTo(secondValue);
  }

  @Test
  void dutchNationalFlagTest() {

    var length = new Variable(Interval.unknown(), "A.length");
    var r = new Variable(0, 0, "r");
    var w = new Variable(Interval.unknown(), "w");
    var b = new Variable(Interval.unknown(), "b");

    var funArray = new FunArray(new Expression(length), true);

    //var env = new Environment(funArray, List.of(length, r, w, b));
  }
}
