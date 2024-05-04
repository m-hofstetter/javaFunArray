package funarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import base.Interval;
import base.infint.InfInt;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Set;

public class FunArrayTest {

  static final Bound END_BOUND = new Bound(new Expression(
          new Variable(new Interval(0, 0), "A.length"),
          InfInt.of(0)
  ));

  static final Bound END_MINUS_ONE_BOUND = new Bound(new Expression(
          new Variable(new Interval(0, 0), "A.length"),
          InfInt.of(-1)
  ));

  //A: {0} [-∞, ∞] {A.length}
  static final FunArray FUN_ARRAY_NEWLY_INSTANTIATED = new FunArray(
          List.of(new Bound(0), END_BOUND),
          List.of(Interval.getUnknown()),
          List.of(false)
  );

  //A: {0} [0, 0] {1}? [-∞, ∞] {A.length}
  static final FunArray FUN_ARRAY_AFTER_SINGLE_INSERTION = new FunArray(
          List.of(new Bound(0), new Bound(1), END_BOUND),
          List.of(new Interval(0, 0), Interval.getUnknown()),
          List.of(false, true)
  );

  //A: {0} [0, 0] {1} [0, 0] {2} [-∞, ∞] {A.length}?
  static final FunArray FUN_ARRAY_AFTER_TWO_INSERTIONS = new FunArray(
          List.of(new Bound(0), new Bound(1), new Bound(2), END_BOUND),
          List.of(new Interval(0, 0), new Interval(0, 0), Interval.getUnknown()),
          List.of(false, false, true)
  );

  //A: {0} [-∞, ∞] {A.length-1}? [0, 0] {A.length}
  static final FunArray FUN_ARRAY_AFTER_SEGMENT_JOINING_INSERTION = new FunArray(
          List.of(new Bound(0), END_MINUS_ONE_BOUND, END_BOUND),
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

    funArray = funArray.assignArrayElement(new Expression(0), new Interval(0, 0));
    assertThat(funArray.funArray()).isEqualTo(FUN_ARRAY_AFTER_SINGLE_INSERTION);

    funArray = funArray.assignArrayElement(new Expression(1), new Interval(0, 0));
    assertThat(funArray.funArray()).isEqualTo(FUN_ARRAY_AFTER_TWO_INSERTIONS);

    funArray = funArray.assignArrayElement(length.increase(InfInt.of(-1)), new Interval(0, 0));
    assertThat(funArray.funArray()).isEqualTo(FUN_ARRAY_AFTER_SEGMENT_JOINING_INSERTION);
  }

  /**
   * From Cousot, Cousot, Logozzo (POPL 2011), Example 8
   */
  @Test
  void unifyTest() {
    var exp0 = new Expression(Variable.ZERO_VALUE, InfInt.of(0));
    var expI = new Expression(new Variable(Interval.getUnknown(), "i"), InfInt.of(0));
    var expN = new Expression(new Variable(Interval.getUnknown(), "n"), InfInt.of(0));
    var expIm1 = new Expression(new Variable(Interval.getUnknown(), "i"), InfInt.of(-1));
    var exp1 = new Expression(Variable.ZERO_VALUE, InfInt.of(1));

    var arrayA = new FunArray(
            List.of(new Bound(Set.of(exp0, expI)), new Bound(Set.of(expN))),
            List.of(Interval.getUnknown()),
            List.of(false)
    );

    var arrayB = new FunArray(
            List.of(
                    new Bound(Set.of(exp0, expIm1)),
                    new Bound(Set.of(exp1, expI)),
                    new Bound(Set.of(expN))
            ),
            List.of(
                    new Interval(0, 0),
                    Interval.getUnknown()),
            List.of(false, true)
    );

    var expectedA = new FunArray(
            List.of(
                    new Bound(Set.of(exp0)),
                    new Bound(Set.of(expI)),
                    new Bound(Set.of(expN))
            ),
            List.of(
                    Interval.UNREACHABLE,
                    Interval.getUnknown()),
            List.of(true, false)
    );

    var expectedB = new FunArray(
            List.of(
                    new Bound(Set.of(exp0)),
                    new Bound(Set.of(expI)),
                    new Bound(Set.of(expN))
            ),
            List.of(
                    new Interval(0, 0),
                    Interval.getUnknown()),
            List.of(false, true)
    );

    var unifiedArrays = arrayA.unify(arrayB, Interval.UNREACHABLE, Interval.UNREACHABLE);

    assertThat(unifiedArrays.get(0)).isEqualTo(expectedA);
    assertThat(unifiedArrays.get(1)).isEqualTo(expectedB);
  }
}
