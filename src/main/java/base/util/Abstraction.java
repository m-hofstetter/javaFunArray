package base.util;

import base.interval.Interval;
import base.sign.Sign;

public class Abstraction {
  public static Sign abstractSign(int value) {
    return Sign.of(value);
  }

  public static Interval abstractInterval(int value) {
    return Interval.of(value);
  }
}
