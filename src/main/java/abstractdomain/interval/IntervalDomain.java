package abstractdomain.interval;

import abstractdomain.Domain;
import abstractdomain.exception.ConcretizationException;
import abstractdomain.interval.value.Interval;
import abstractdomain.interval.value.ReachableInterval;
import base.infint.FiniteInteger;

public class IntervalDomain implements Domain<Interval> {

  public static final IntervalDomain INSTANCE = new IntervalDomain();

  private IntervalDomain() {
  }

  @Override
  public Interval abstract_(long concreteValue) {
    return Interval.of(concreteValue);
  }

  @Override
  public long concretize(Interval interval) throws ConcretizationException {
    if (interval instanceof ReachableInterval reachableInterval) {
      if (reachableInterval.getLowerLimit().equals(reachableInterval.getUpperLimit()))
        if (reachableInterval.getLowerLimit() instanceof FiniteInteger finiteInt) {
          return finiteInt.getValue();
        }
    }
    throw new ConcretizationException(interval);
  }

  @Override
  public Interval getUnknown() {
    return Interval.unknown();
  }

  @Override
  public Interval getUnreachable() {
    return Interval.unreachable();
  }

  @Override
  public Interval getZeroValue() {
    return Interval.of(0, 0);
  }
}
