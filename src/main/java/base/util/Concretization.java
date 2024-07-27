package base.util;

import base.DomainValue;
import base.infint.FiniteInteger;
import base.interval.Interval;
import base.interval.ReachableInterval;
import base.sign.Sign;

public class Concretization {

  public static <T extends DomainValue<T>> int concretize(T value) throws ConcretizationException {
    return switch (value) {
      case Interval interval -> concretiseInterval(interval);
      case Sign sign -> concretiseSign(sign);
      default -> throw new ConcretizationException(value);
    };
  }

  private static int concretiseInterval(Interval interval) throws ConcretizationException {
    if (interval instanceof ReachableInterval reachableInterval) {
      if (reachableInterval.getLowerLimit().equals(reachableInterval.getUpperLimit()))
        if (reachableInterval.getLowerLimit() instanceof FiniteInteger finiteInt) {
          return finiteInt.getValue();
        }
    }
    throw new ConcretizationException(interval);
  }

  private static int concretiseSign(Sign sign) throws ConcretizationException {
    if (sign.equals(Sign.of(0))) {
      return 0;
    }
    throw new ConcretizationException(sign);
  }
}
