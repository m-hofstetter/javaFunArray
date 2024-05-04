package funarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import base.Interval;
import base.infint.InfInt;
import org.junit.jupiter.api.Test;
import java.util.List;

public class FunArrayTest {

  static final Bound END_BOUND = Bound.of(new Expression(
          new Variable(new Interval(0, 0), "A.length"),
          InfInt.of(0)
  ));

  static final Bound END_MINUS_ONE_BOUND = Bound.of(new Expression(
          new Variable(new Interval(0, 0), "A.length"),
          InfInt.of(-1)
  ));

  //A: {0} [-∞, ∞] {A.length}
  static final FunArray FUN_ARRAY_NEWLY_INSTANTIATED = new FunArray(
          List.of(Bound.of(0), END_BOUND),
          List.of(Interval.getUnknown()),
          List.of(false)
  );

  //A: {0} [0, 0] {1}? [-∞, ∞] {A.length}
  static final FunArray FUN_ARRAY_AFTER_SINGLE_INSERTION = new FunArray(
          List.of(Bound.of(0), Bound.of(1), END_BOUND),
          List.of(new Interval(0, 0), Interval.getUnknown()),
          List.of(false, true)
  );

  //A: {0} [0, 0] {1} [0, 0] {2} [-∞, ∞] {A.length}?
  static final FunArray FUN_ARRAY_AFTER_TWO_INSERTIONS = new FunArray(
          List.of(Bound.of(0), Bound.of(1), Bound.of(2), END_BOUND),
          List.of(new Interval(0, 0), new Interval(0, 0), Interval.getUnknown()),
          List.of(false, false, true)
  );

  //A: {0} [-∞, ∞] {A.length-1}? [0, 0] {A.length}
  static final FunArray FUN_ARRAY_AFTER_SEGMENT_JOINING_INSERTION = new FunArray(
          List.of(Bound.of(0), END_MINUS_ONE_BOUND, END_BOUND),
          List.of(Interval.getUnknown(), new Interval(0, 0)),
          List.of(true, false)
  );

  @Test
  void instantiateTest() {
    var interval = new Interval(0, 10);
    var length = new Expression(new Variable(interval, "A.length"), InfInt.of(0));
    var funArray = new Environment(length);

    assertEquals("A: {0} [-∞, ∞] {A.length}\nA.length: [0, 10]", funArray.toString());
  }

  @Test
  void addToVariableTest() {
    var interval = new Interval(0, 10);
    var length = new Expression(new Variable(interval, "A.length"), InfInt.of(0));
    var funArray = new Environment(length);

    var modified = funArray.addToVariable(length.variable(), InfInt.of(3));

    assertEquals("A: {0} [-∞, ∞] {A.length-3}\nA.length: [3, 13]", modified.toString());
  }

  @Test
  void insertTest() {
    var interval = new Interval(0, 0);
    var length = new Expression(new Variable(interval, "A.length"), InfInt.of(0));
    var funArray = new Environment(length);
    assertThat(funArray.funArray()).isEqualTo(FUN_ARRAY_NEWLY_INSTANTIATED);

    funArray = funArray.assignArrayElement(Expression.getZero(), new Interval(0, 0));
    assertThat(funArray.funArray()).isEqualTo(FUN_ARRAY_AFTER_SINGLE_INSERTION);

    funArray = funArray.assignArrayElement(Expression.getConstant(1), new Interval(0, 0));
    assertThat(funArray.funArray()).isEqualTo(FUN_ARRAY_AFTER_TWO_INSERTIONS);

    funArray = funArray.assignArrayElement(length.increase(InfInt.of(-1)), new Interval(0, 0));
    assertThat(funArray.funArray()).isEqualTo(FUN_ARRAY_AFTER_SEGMENT_JOINING_INSERTION);
  }
}
