package funarray;

import static base.IntegerWithInfinity.NEGATIVE_INFINITY;
import static base.IntegerWithInfinity.POSITIVE_INFINITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import base.IntegerWithInfinity;
import base.Interval;
import org.junit.jupiter.api.Test;
import java.util.List;

public class FunArrayTest {

  static final Bound END_BOUND = Bound.of(new Expression(
          new Variable(new Interval(0, 0), "A.length"),
          new IntegerWithInfinity(0)
  ));

  static final Bound END_MINUS_ONE_BOUND = Bound.of(new Expression(
          new Variable(new Interval(0, 0), "A.length"),
          new IntegerWithInfinity(-1)
  ));

  //A: {0} [-∞, ∞] {A.length}
  static final FunArray FUN_ARRAY_NEWLY_INSTANTIATED = new FunArray(
          List.of(Bound.ofConstant(0), END_BOUND),
          List.of(new Interval(NEGATIVE_INFINITY, POSITIVE_INFINITY)),
          List.of(false)
  );

  //A: {0} [0, 0] {1}? [-∞, ∞] {A.length}
  static final FunArray FUN_ARRAY_AFTER_SINGLE_INSERTION = new FunArray(
          List.of(Bound.ofConstant(0), Bound.ofConstant(1), END_BOUND),
          List.of(new Interval(0, 0), new Interval(NEGATIVE_INFINITY, POSITIVE_INFINITY)),
          List.of(false, true)
  );

  //A: {0} [0, 0] {1} [0, 0] {2} [-∞, ∞] {A.length}?
  static final FunArray FUN_ARRAY_AFTER_TWO_INSERTIONS = new FunArray(
          List.of(Bound.ofConstant(0), Bound.ofConstant(1), Bound.ofConstant(2), END_BOUND),
          List.of(new Interval(0, 0), new Interval(0, 0), new Interval(NEGATIVE_INFINITY, POSITIVE_INFINITY)),
          List.of(false, false, true)
  );

  //A: {0} [-∞, ∞] {A.length-1}? [0, 0] {A.length}
  static final FunArray FUN_ARRAY_AFTER_SEGMENT_JOINING_INSERTION = new FunArray(
          List.of(Bound.ofConstant(0), END_MINUS_ONE_BOUND, END_BOUND),
          List.of(new Interval(NEGATIVE_INFINITY, POSITIVE_INFINITY), new Interval(0, 0)),
          List.of(true, false)
  );

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
    assertThat(funArray.funArray()).isEqualTo(FUN_ARRAY_NEWLY_INSTANTIATED);

    funArray = funArray.assignArrayElement(Expression.getZero(), new Interval(0, 0));
    assertThat(funArray.funArray()).isEqualTo(FUN_ARRAY_AFTER_SINGLE_INSERTION);

    funArray = funArray.assignArrayElement(Expression.getConstant(1), new Interval(0, 0));
    assertThat(funArray.funArray()).isEqualTo(FUN_ARRAY_AFTER_TWO_INSERTIONS);

    funArray = funArray.assignArrayElement(length.increase(-1), new Interval(0, 0));
    assertThat(funArray.funArray()).isEqualTo(FUN_ARRAY_AFTER_SEGMENT_JOINING_INSERTION);
  }
}
