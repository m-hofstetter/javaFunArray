package abstractdomain.interval;

import abstractdomain.Domain;
import abstractdomain.Relation;
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

  @Override
  public Relation<Interval> lessThan() {
    return new Relation<>() {
      @Override
      public Interval satisfy(Interval comparandum, Interval comparand) {
        return comparandum.satisfyLessThan(comparand);
      }

      @Override
      public boolean isSatisfied(Interval comparandum, Interval comparand) {
        return comparandum.lessThan(comparand);
      }

      @Override
      public Relation<Interval> inverseOrder() {
        return INSTANCE.greaterThan();
      }

      @Override
      public Relation<Interval> complementaryOrder() {
        return INSTANCE.greaterEqualThan();
      }
    };
  }

  @Override
  public Relation<Interval> lessEqualThan() {
    return new Relation<>() {
      @Override
      public Interval satisfy(Interval comparandum, Interval comparand) {
        return comparandum.satisfyLessEqualThan(comparand);
      }

      @Override
      public boolean isSatisfied(Interval comparandum, Interval comparand) {
        return comparandum.lessEqualThan(comparand);
      }

      @Override
      public Relation<Interval> inverseOrder() {
        return INSTANCE.greaterEqualThan();
      }

      @Override
      public Relation<Interval> complementaryOrder() {
        return INSTANCE.greaterThan();
      }
    };
  }

  @Override
  public Relation<Interval> greaterThan() {
    return new Relation<>() {
      @Override
      public Interval satisfy(Interval comparandum, Interval comparand) {
        return comparandum.satisfyGreaterThan(comparand);
      }

      @Override
      public boolean isSatisfied(Interval comparandum, Interval comparand) {
        return comparandum.greaterThan(comparand);
      }

      @Override
      public Relation<Interval> inverseOrder() {
        return INSTANCE.lessThan();
      }

      @Override
      public Relation<Interval> complementaryOrder() {
        return INSTANCE.lessEqualThan();
      }
    };
  }

  @Override
  public Relation<Interval> greaterEqualThan() {
    return new Relation<>() {
      @Override
      public Interval satisfy(Interval comparandum, Interval comparand) {
        return comparandum.satisfyGreaterEqualThan(comparand);
      }

      @Override
      public boolean isSatisfied(Interval comparandum, Interval comparand) {
        return comparandum.greaterEqualThan(comparand);
      }

      @Override
      public Relation<Interval> inverseOrder() {
        return INSTANCE.lessEqualThan();
      }

      @Override
      public Relation<Interval> complementaryOrder() {
        return INSTANCE.lessThan();
      }
    };
  }

  @Override
  public Relation<Interval> equalTo() {
    return new Relation<>() {
      @Override
      public Interval satisfy(Interval comparandum, Interval comparand) {
        return comparandum.satisfyEqual(comparand);
      }

      @Override
      public boolean isSatisfied(Interval comparandum, Interval comparand) {
        return comparandum.equal(comparand);
      }

      @Override
      public Relation<Interval> inverseOrder() {
        return INSTANCE.equalTo();
      }

      @Override
      public Relation<Interval> complementaryOrder() {
        return INSTANCE.unequalTo();
      }
    };
  }

  @Override
  public Relation<Interval> unequalTo() {
    return new Relation<>() {
      @Override
      public Interval satisfy(Interval comparandum, Interval comparand) {
        return comparandum.satisfyNotEqual(comparand);
      }

      @Override
      public boolean isSatisfied(Interval comparandum, Interval comparand) {
        return comparandum.notEqual(comparand);
      }

      @Override
      public Relation<Interval> inverseOrder() {
        return INSTANCE.unequalTo();
      }

      @Override
      public Relation<Interval> complementaryOrder() {
        return INSTANCE.equalTo();
      }
    };
  }
}
