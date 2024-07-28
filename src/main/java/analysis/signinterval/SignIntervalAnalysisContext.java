package analysis.signinterval;

import static abstractdomain.sign.value.Sign.SignElement.NEGATIVE;
import static abstractdomain.sign.value.Sign.SignElement.POSITIVE;
import static abstractdomain.sign.value.Sign.SignElement.ZERO;

import abstractdomain.Domain;
import abstractdomain.interval.IntervalDomain;
import abstractdomain.interval.value.Interval;
import abstractdomain.interval.value.ReachableInterval;
import abstractdomain.sign.SignDomain;
import abstractdomain.sign.value.Sign;
import analysis.common.AnalysisContext;
import base.infint.InfInt;
import java.util.HashSet;
import java.util.Set;

public class SignIntervalAnalysisContext {
  public static final AnalysisContext<Sign, Interval> INSTANCE = new AnalysisContext<>() {
    @Override
    public Domain<Sign> getElementDomain() {
      return SignDomain.INSTANCE;
    }

    @Override
    public Domain<Interval> getVariableDomain() {
      return IntervalDomain.INSTANCE;
    }

    @Override
    public Sign convertVariableValueToArrayElementValue(Interval variableValue) {
      if (variableValue instanceof ReachableInterval reachableInterval) {
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

    @Override
    public Interval convertArrayElementValueToVariableValue(Sign arrayElementValue) {
      var combined = Interval.unreachable();
      for (var element : arrayElementValue.elements().stream()
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
  };
}
