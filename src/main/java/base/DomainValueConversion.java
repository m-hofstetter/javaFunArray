package base;

import static abstractdomain.sign.value.Sign.SignElement.NEGATIVE;
import static abstractdomain.sign.value.Sign.SignElement.POSITIVE;
import static abstractdomain.sign.value.Sign.SignElement.ZERO;

import abstractdomain.DomainValue;
import abstractdomain.interval.value.Interval;
import abstractdomain.interval.value.ReachableInterval;
import abstractdomain.sign.value.Sign;
import base.infint.InfInt;
import java.util.HashSet;
import java.util.Set;

/**
 * A utility class for providing conversion functions between abstract domains.
 */
public class DomainValueConversion {

  /**
   * Converts a value from the interval abstract domain to the sign abstract domain.
   *
   * @param interval the interval value.
   * @return the sign value.
   */
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

  /**
   * Converts a value from the sign abstract domain to the interval abstract domain.
   *
   * @param sign the sign value.
   * @return the interval value.
   */
  public static Interval convertSignToInterval(Sign sign) {
    var combined = Interval.unreachable();
    for (var element : sign.elements().stream()
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

  /**
   * The identity function for domain values. Takes a value from an abstract domain and returns the
   * same value from the same domain.
   *
   * @param value the value
   * @param <T>   the abstract domain of the value
   * @return the same value
   */
  public static <T extends DomainValue<T>> T identity(T value) {
    return value;
  }

}
