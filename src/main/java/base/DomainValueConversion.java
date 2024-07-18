package base;

import static base.sign.Sign.SignElement.*;

import base.infint.InfInt;
import base.interval.Interval;
import base.interval.ReachableInterval;
import base.sign.Sign;
import java.util.HashSet;
import java.util.Set;

public class DomainValueConversion {

  public static Sign convertIntervalToSign(Interval interval) {
    if (interval instanceof ReachableInterval reachableInterval) {
      var lowerLimit = reachableInterval.getLowerLimit();
      var upperLimit = reachableInterval.getUpperLimit();
      var zero = InfInt.of(0);
      var elements = new HashSet<Sign.SignElement>();

      if (lowerLimit.isLessThan(zero)) {
        elements.add(NEGATIVE);
      }
      if (upperLimit.isGreaterThan(zero)) {
        elements.add(POSITIVE);
      }
      if (lowerLimit.isLessEqualThan(zero) && upperLimit.isGreaterEqualThan(zero)) {
        elements.add(ZERO);
      }

      return new Sign(elements);
    }
    return new Sign(Set.of());
  }

  public static Interval convertSignToInterval(Sign sign) {
    var combined = Interval.unreachable();
    for (var element : sign.getElements().stream()
            .map(signElement -> switch (signElement) {
              case NEGATIVE -> Interval.of(InfInt.negInf(), -1);
              case ZERO -> Interval.of(0, 0);
              case POSITIVE -> Interval.of(1, InfInt.posInf());
            })
            .toList()) {
      combined = combined.join(element);
    }
    return combined;
  }

  public static Interval keepInterval(Interval interval) {
    return interval;
  }

}
