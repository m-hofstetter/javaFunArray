package funarray;

import static base.IntegerWithInfinity.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import base.*;
import org.junit.jupiter.api.*;

public class FunArrayTest {

  @Test
  void instantiateTest() {
    var interval = new Interval(0, 10);
    var length = new Expression(new Variable(interval, "A.length"), new IntegerWithInfinity(0));
    var funArray = new FunArrayEnvironment(length);

    assertEquals("A: {0} [-∞, ∞] {A.length}\nA.length: [0, 10]", funArray.toString());
  }

  @Test
  void addToVariableTest() {
    var interval = new Interval(0, 10);
    var length = new Expression(new Variable(interval, "A.length"), new IntegerWithInfinity(0));
    var funArray = new FunArrayEnvironment(length);

    var modified = funArray.addToVariable(length.variable(), 3);

    assertEquals("A: {0} [-∞, ∞] {A.length-3}\nA.length: [3, 13]", modified.toString());
  }

  @Test
  void insertTest() {
    var interval = new Interval(0, 0);
    var length = new Expression(new Variable(interval, "A.length"), new IntegerWithInfinity(0));
    var funArray = new FunArrayEnvironment(length);

    System.out.println();

    assertThat(funArray.funArray().bounds()).hasSize(2);
    assertThat(funArray.funArray().values().get(0)).isEqualTo(new Interval(NEGATIVE_INFINITY, POSITIVE_INFINITY));

    funArray = funArray.assignArrayElement(Expression.getZero(), new Interval(0, 0));

    assertThat(funArray.funArray().bounds()).hasSize(3);
    assertThat(funArray.funArray().values().get(0)).isEqualTo(new Interval(0, 0));
    assertThat(funArray.funArray().values().get(1)).isEqualTo(new Interval(NEGATIVE_INFINITY, POSITIVE_INFINITY));

    funArray = funArray.assignArrayElement(Expression.getConstant(1), new Interval(0, 0));

    assertThat(funArray.funArray().bounds()).hasSize(4);
    assertThat(funArray.funArray().values().get(0)).isEqualTo(new Interval(0, 0));
    assertThat(funArray.funArray().values().get(1)).isEqualTo(new Interval(0, 0));
    assertThat(funArray.funArray().values().get(2)).isEqualTo(new Interval(NEGATIVE_INFINITY, POSITIVE_INFINITY));

    funArray = funArray.assignArrayElement(length.increase(-1), new Interval(0, 0));

    assertThat(funArray.funArray().bounds()).hasSize(3);
    assertThat(funArray.funArray().values().get(0)).isEqualTo(new Interval(NEGATIVE_INFINITY, POSITIVE_INFINITY));
    assertThat(funArray.funArray().values().get(1)).isEqualTo(new Interval(0, 0));
  }
}
