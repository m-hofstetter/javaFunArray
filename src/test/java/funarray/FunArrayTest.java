package funarray;

import static base.interval.Interval.unknown;
import static funarray.util.FunArrayBuilder.buildFunArray;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import base.infint.InfInt;
import base.interval.Interval;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;

public class FunArrayTest {

  static final VariableReference LENGTH = new VariableReference("A.length");

  @Test
  void addToVariableTest() {
    var interval = Interval.of(0, 10);
    var length = new Expression(new VariableReference("A.length"), InfInt.of(0));
    var funArray = new Environment<>(
            new FunArray<>(
                    List.of(new Bound(new Expression(new VariableReference("0"), 0)), new Bound(length)),
                    List.of(unknown()),
                    List.of(false)
            ),
            Map.of(new VariableReference("A.length"), interval)
    );

    var modified = funArray.addToVariable(length.variable(), InfInt.of(3));

    assertEquals("A: {0} [-∞, ∞] {A.length-3}\nA.length: [3, 13]", modified.toString());
  }

  @Test
  void insertTest() {
    var interval = Interval.of(0, 0);
    var length = new Expression(new VariableReference("A.length"), InfInt.of(0));
    var funArray = new Environment(
            new FunArray<>(
                    List.of(new Bound(new Expression(new VariableReference("0"), 0)), new Bound(length)),
                    List.of(unknown()),
                    List.of(false)
            ),
            Map.of(new VariableReference("A.length"), interval)
    );
    assertThat(funArray.funArray()).isEqualTo(
            buildFunArray()
                    .bound(0)
                    .unknownValue()
                    .bound(LENGTH).build()
    );

    funArray = funArray.assignArrayElement(new Expression(new VariableReference("0"), 0), Interval.of(0, 0));
    assertThat(funArray.funArray()).isEqualTo(
            buildFunArray()
                    .bound(0)
                    .value(0)
                    .bound(1)
                    .unknownValue()
                    .mightBeEmpty()
                    .bound(LENGTH).build()
    );

    funArray = funArray.assignArrayElement(new Expression(new VariableReference("0"), 1), Interval.of(0, 0));
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
                    .value(0)
                    .bound(1)
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

    var i = new VariableReference("i");
    var n = new VariableReference("n");

    var arrayA = buildFunArray()
            .bound(new VariableReference("0"), i)
            .unknownValue()
            .bound(n).build();

    var arrayB = buildFunArray()
            .bound(new Expression(new VariableReference("0"), 0), new Expression(i, -1))
            .value(0)
            .bound(new Expression(new VariableReference("0"), 1), new Expression(i))
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

    assertThat(unifiedArrays.resultThis()).isEqualTo(expectedA);
    assertThat(unifiedArrays.resultOther()).isEqualTo(expectedB);
  }

  @Test
  void complexUnifyTest() {
    var a = new VariableReference("a");
    var b = new VariableReference("b");
    var zero = new VariableReference("0");
    var length = new VariableReference("A.length");

    var arrayA = buildFunArray()
            .bound(zero)
            .value(unknown())
            .bound(new Expression(a, -1))
            .value(unknown())
            .bound(a)
            .value(unknown())
            .bound(b)
            .value(unknown())
            .bound(length)
            .build();

    var arrayB = buildFunArray()
            .bound(zero)
            .value(unknown())
            .bound(a, b)
            .value(unknown())
            .bound(new Expression(b, 1))
            .value(unknown())
            .bound(length)
            .build();

    System.out.println(arrayA);
    System.out.println(arrayB);

    var unified = arrayA.unify(arrayB, Interval.unreachable(), Interval.unreachable());

    System.out.println();
    System.out.println("This: " + unified.resultThis());
    System.out.println("Other: " + unified.resultOther());
  }

  @Test
  void joinTest() {
    var i = new VariableReference("i");
    var n = new VariableReference("n");

    var arrayA = buildFunArray()
            .bound(new VariableReference("0"), i)
            .unknownValue()
            .bound(n).build();

    var arrayB = buildFunArray()
            .bound(new Expression(new VariableReference("0"), 0), new Expression(i, -1))
            .value(0)
            .bound(new Expression(new VariableReference("0"), 1), new Expression(i))
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

    var joined = arrayA.join(arrayB, Interval.unreachable());
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
            funArray.get(new Expression(new VariableReference("0"), 0))
    ).isEqualTo(firstValue);

    assertThat(
            funArray.get(new Expression(new VariableReference("0"), 1))
    ).isEqualTo(secondValue);
  }
}
