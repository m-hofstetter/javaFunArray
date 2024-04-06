package funarray;

import static org.junit.jupiter.api.Assertions.assertEquals;

import base.IntegerWithInfinity;
import base.Interval;
import org.junit.jupiter.api.Test;

public class FunArrayTest {

  @Test
  void instantiateTest() {
    var interval = new Interval(new IntegerWithInfinity(0), new IntegerWithInfinity(10));
    var length = new Variable(interval, "A.length");
    var funArray = new FunArray(length);

    assertEquals("A: {0} [-∞, ∞] {A.length}\nA.length: [0, 10]", funArray.toString());
  }

  @Test
  void addToVariableTest() {
    var interval = new Interval(new IntegerWithInfinity(0), new IntegerWithInfinity(10));
    var length = new Variable(interval, "A.length");
    var funArray = new FunArray(length);

    var modified = funArray.addToVariable(length, 3);

    assertEquals("A: {0} [-∞, ∞] {A.length-3}\nA.length: [3, 13]", modified.toString());
  }
}
