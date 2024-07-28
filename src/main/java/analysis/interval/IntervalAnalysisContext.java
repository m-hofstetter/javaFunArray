package analysis.interval;

import abstractdomain.Domain;
import abstractdomain.interval.IntervalDomain;
import abstractdomain.interval.value.Interval;
import analysis.common.AnalysisContext;

public class IntervalAnalysisContext {
  public static final AnalysisContext<Interval, Interval> INSTANCE = new AnalysisContext<>() {
    @Override
    public Domain<Interval> getElementDomain() {
      return IntervalDomain.INSTANCE;
    }

    @Override
    public Domain<Interval> getVariableDomain() {
      return IntervalDomain.INSTANCE;
    }

    @Override
    public Interval convertVariableValueToArrayElementValue(Interval variableValue) {
      return variableValue;
    }

    @Override
    public Interval convertArrayElementValueToVariableValue(Interval arrayElementValue) {
      return arrayElementValue;
    }
  };
}
