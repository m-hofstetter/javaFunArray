package funarray;

import base.IntegerWithInfinity;
import base.Interval;
import org.junit.jupiter.api.Test;

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
    var interval = new Interval(0, 10);
    var length = new Expression(new Variable(interval, "A.length"), new IntegerWithInfinity(0));
    var funArray = new FunArray(length);

    funArray = funArray.insert(Expression.getZero(), new Interval(3, 3));
    System.out.println(funArray);

    funArray = funArray.insert(Expression.getZero().increase(3), new Interval(6, 6));
    System.out.println(funArray);

    funArray = funArray.insert(length.increase(-1), new Interval(100, 200));
    System.out.println(funArray);
  }
}
