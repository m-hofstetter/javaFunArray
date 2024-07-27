package base.util;

import base.infint.FiniteInteger;
import base.interval.Interval;
import base.interval.ReachableInterval;
import base.sign.Sign;

public class Concretization {
  public static int concretiseInterval(Interval interval) throws ConcretizationException {
    if (interval instanceof ReachableInterval reachableInterval) {
      if (reachableInterval.getLowerLimit().equals(reachableInterval.getUpperLimit()))
        if (reachableInterval.getLowerLimit() instanceof FiniteInteger finiteInt) {
          return finiteInt.getValue();
        }
    }
    throw new ConcretizationException(interval);
  }

  public static int concretiseSign(Sign sign) throws ConcretizationException {
    if (sign.equals(Sign.of(0))) {
      return 0;
    }
    throw new ConcretizationException(sign);
  }
}
