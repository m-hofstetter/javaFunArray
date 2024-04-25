package funarray;

import base.IntegerWithInfinity;
import base.Interval;
import org.junit.jupiter.api.Test;

import static base.IntegerWithInfinity.NEGATIVE_INFINITY;
import static base.IntegerWithInfinity.POSITIVE_INFINITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class FunArrayTest {

  @Test
  void instantiateTest() {
    var interval = new Interval(0, 10);
    var length = new Expression(new Variable(interval, "A.length"), new IntegerWithInfinity(0));
    var funArray = new FunArray(length);

    assertEquals("A: {0} [-∞, ∞] {A.length}\nA.length: [0, 10]", funArray.toString());
  }

  @Test
  void addToVariableTest() {
    var interval = new Interval(0, 10);
    var length = new Expression(new Variable(interval, "A.length"), new IntegerWithInfinity(0));
    var funArray = new FunArray(length);

    var modified = funArray.addToVariable(length.variable(), 3);

    assertEquals("A: {0} [-∞, ∞] {A.length-3}\nA.length: [3, 13]", modified.toString());
  }

  @Test
  void insertTest() {
    var interval = new Interval(0, 0);
    var length = new Expression(new Variable(interval, "A.length"), new IntegerWithInfinity(0));
    var funArray = new FunArray(length);

    assertThat(funArray.segmentation().bounds()).hasSize(2);
    assertThat(funArray.segmentation().values().get(0)).isEqualTo(new Interval(NEGATIVE_INFINITY, POSITIVE_INFINITY));

    funArray = funArray.insert(Expression.getZero(), new Interval(0, 0));

    assertThat(funArray.segmentation().bounds()).hasSize(3);
    assertThat(funArray.segmentation().values().get(0)).isEqualTo(new Interval(0, 0));
    assertThat(funArray.segmentation().values().get(1)).isEqualTo(new Interval(NEGATIVE_INFINITY, POSITIVE_INFINITY));

    funArray = funArray.insert(Expression.getConstant(1), new Interval(0, 0));

    assertThat(funArray.segmentation().bounds()).hasSize(4);
    assertThat(funArray.segmentation().values().get(0)).isEqualTo(new Interval(0, 0));
    assertThat(funArray.segmentation().values().get(1)).isEqualTo(new Interval(0, 0));
    assertThat(funArray.segmentation().values().get(2)).isEqualTo(new Interval(NEGATIVE_INFINITY, POSITIVE_INFINITY));

    funArray = funArray.insert(length.increase(-1), new Interval(0, 0));

    assertThat(funArray.segmentation().bounds()).hasSize(3);
    assertThat(funArray.segmentation().values().get(0)).isEqualTo(new Interval(NEGATIVE_INFINITY, POSITIVE_INFINITY));
    assertThat(funArray.segmentation().values().get(1)).isEqualTo(new Interval(0, 0));
  }
}
