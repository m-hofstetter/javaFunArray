package base;

import static org.junit.jupiter.api.Assertions.*;

import base.infint.InfInt;
import org.junit.jupiter.api.Test;

public class InfIntTest {

  @Test
  public void testCompareTo() {
    var one = InfInt.of(1);
    var zero = InfInt.of(0);
    var posInf = InfInt.posInf();
    var negInf = InfInt.negInf();

    assertEquals(negInf.compareTo(InfInt.negInf()), 0);
    assertEquals(negInf.compareTo(zero), -1);
    assertEquals(negInf.compareTo(one), -1);
    assertEquals(negInf.compareTo(posInf), -1);

    assertEquals(zero.compareTo(negInf), 1);
    assertEquals(zero.compareTo(InfInt.of(0)), 0);
    assertEquals(zero.compareTo(one), -1);
    assertEquals(zero.compareTo(posInf), -1);

    assertEquals(one.compareTo(negInf), 1);
    assertEquals(one.compareTo(zero), 1);
    assertEquals(one.compareTo(InfInt.of(1)), 0);
    assertEquals(one.compareTo(posInf), -1);

    assertEquals(posInf.compareTo(negInf), 1);
    assertEquals(posInf.compareTo(zero), 1);
    assertEquals(posInf.compareTo(one), 1);
    assertEquals(posInf.compareTo(InfInt.posInf()), 0);
  }

  @Test
  public void testMin() {
    var one = InfInt.of(1);
    var zero = InfInt.of(0);
    var posInf = InfInt.posInf();
    var negInf = InfInt.negInf();

    assertEquals(InfInt.min(negInf, zero), negInf);
    assertEquals(InfInt.min(negInf, one), negInf);
    assertEquals(InfInt.min(negInf, posInf), negInf);

    assertEquals(InfInt.min(zero, one), zero);
    assertEquals(InfInt.min(one, zero), zero);

    assertEquals(InfInt.min(posInf, zero), zero);
    assertEquals(InfInt.min(posInf, one), one);
    assertEquals(InfInt.min(posInf, negInf), negInf);
  }

  @Test
  public void testMax() {
    var one = InfInt.of(1);
    var zero = InfInt.of(0);
    var posInf = InfInt.posInf();
    var negInf = InfInt.negInf();

    assertEquals(InfInt.max(negInf, zero), zero);
    assertEquals(InfInt.max(negInf, one), one);
    assertEquals(InfInt.max(negInf, posInf), posInf);

    assertEquals(InfInt.max(zero, one), one);
    assertEquals(InfInt.max(one, zero), one);

    assertEquals(InfInt.max(posInf, zero), posInf);
    assertEquals(InfInt.max(posInf, one), posInf);
    assertEquals(InfInt.max(posInf, negInf), posInf);
  }

  @Test
  public void isLessThanTest() {
    var one = InfInt.of(1);
    var zero = InfInt.of(0);
    var posInf = InfInt.posInf();
    var negInf = InfInt.negInf();

    assertFalse(negInf.isLessThan(negInf));
    assertTrue(negInf.isLessThan(zero));
    assertTrue(negInf.isLessThan(one));
    assertTrue(negInf.isLessThan(posInf));

    assertFalse(zero.isLessThan(negInf));
    assertFalse(zero.isLessThan(zero));
    assertTrue(zero.isLessThan(one));
    assertTrue(zero.isLessThan(posInf));

    assertFalse(one.isLessThan(negInf));
    assertFalse(one.isLessThan(zero));
    assertFalse(one.isLessThan(one));
    assertTrue(one.isLessThan(posInf));

    assertFalse(posInf.isLessThan(negInf));
    assertFalse(posInf.isLessThan(zero));
    assertFalse(posInf.isLessThan(one));
    assertFalse(posInf.isLessThan(posInf));
  }

  @Test
  public void isGreaterThanTest() {
    var one = InfInt.of(1);
    var zero = InfInt.of(0);
    var posInf = InfInt.posInf();
    var negInf = InfInt.negInf();

    assertFalse(negInf.isGreaterThan(negInf));
    assertFalse(negInf.isGreaterThan(zero));
    assertFalse(negInf.isGreaterThan(one));
    assertFalse(negInf.isGreaterThan(posInf));

    assertTrue(zero.isGreaterThan(negInf));
    assertFalse(zero.isGreaterThan(zero));
    assertFalse(zero.isGreaterThan(one));
    assertFalse(zero.isGreaterThan(posInf));

    assertTrue(one.isGreaterThan(negInf));
    assertTrue(one.isGreaterThan(zero));
    assertFalse(one.isGreaterThan(one));
    assertFalse(one.isGreaterThan(posInf));

    assertTrue(posInf.isGreaterThan(negInf));
    assertTrue(posInf.isGreaterThan(zero));
    assertTrue(posInf.isGreaterThan(one));
    assertFalse(posInf.isGreaterThan(posInf));
  }

  @Test
  public void isInfTest() {
    var one = InfInt.of(1);
    var zero = InfInt.of(0);
    var posInf = InfInt.posInf();
    var negInf = InfInt.negInf();

    assertTrue(posInf.isPosInf());
    assertFalse(zero.isPosInf());
    assertFalse(one.isPosInf());
    assertFalse(negInf.isPosInf());

    assertTrue(negInf.isNegInf());
    assertFalse(zero.isNegInf());
    assertFalse(one.isNegInf());
    assertFalse(posInf.isNegInf());
  }

  @Test
  public void additionTest() {
    var one = InfInt.of(1);
    var zero = InfInt.of(0);
    var posInf = InfInt.posInf();
    var negInf = InfInt.negInf();

    assertEquals(zero.add(1), one);
    assertEquals(zero.add(InfInt.of(1)), one);
    assertEquals(one.add(-1), zero);
    assertEquals(one.add(InfInt.of(-1)), zero);
    assertEquals(zero.add(0), zero);
    assertEquals(zero.add(InfInt.of(0)), zero);
    assertEquals(zero.add(negInf), negInf);
    assertEquals(zero.add(posInf), posInf);

    assertEquals(posInf.add(1), posInf);
    assertEquals(posInf.add(-1), posInf);

    assertEquals(negInf.add(1), negInf);
    assertEquals(negInf.add(-1), negInf);

    assertEquals(zero.subtract(-1), one);
    assertEquals(zero.subtract(InfInt.of(-1)), one);
    assertEquals(one.subtract(1), zero);
    assertEquals(one.subtract(InfInt.of(1)), zero);
    assertEquals(zero.subtract(0), zero);
    assertEquals(zero.subtract(InfInt.of(0)), zero);
    assertEquals(zero.subtract(negInf), posInf);
    assertEquals(zero.subtract(posInf), negInf);

    assertEquals(posInf.subtract(1), posInf);
    assertEquals(posInf.subtract(-1), posInf);

    assertEquals(negInf.subtract(1), negInf);
    assertEquals(negInf.subtract(-1), negInf);
  }

  @Test
  public void negateTest() {
    var one = InfInt.of(1);
    var zero = InfInt.of(0);
    var posInf = InfInt.posInf();
    var negInf = InfInt.negInf();

    assertEquals(negInf.negate(), posInf);
    assertEquals(posInf.negate(), negInf);
    assertEquals(one.negate(), InfInt.of(-1));
    assertEquals(zero.negate(), zero);
  }

  @Test
  public void toStringTest() {
    var one = InfInt.of(1);
    var zero = InfInt.of(0);
    var posInf = InfInt.posInf();
    var negInf = InfInt.negInf();

    assertEquals(one.toString(), "1");
    assertEquals(zero.toString(), "0");
    assertEquals(posInf.toString(), "∞");
    assertEquals(negInf.toString(), "-∞");
  }

}
