package funarray.util;

import base.interval.Interval;
import funarray.Bound;
import funarray.Expression;
import funarray.FunArray;
import funarray.Variable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FunArrayBuilder {
  private final List<Bound> bounds = new ArrayList<>();
  private final List<Interval> values = new ArrayList<>();
  private final List<Boolean> emptiness = new ArrayList<>();

  public static FunArrayBuilder buildFunArray() {
    return new FunArrayBuilder();
  }

  public FunArrayBuilder bound(Bound bound) {
    if (emptiness.size() < values.size()) {
      emptiness.add(false);
    }
    bounds.add(bound);
    return this;
  }

  public FunArrayBuilder bound(Expression... expressions) {
    var bound = new Bound(expressions);
    return bound(bound);
  }

  public FunArrayBuilder bound(int boundValue) {
    var bound = new Bound(boundValue);
    return bound(bound);
  }

  public FunArrayBuilder bound(Variable variable) {
    var expression = new Expression(variable, 0);
    var bound = new Bound(expression);
    return bound(bound);
  }

  public FunArrayBuilder bound(Variable... variables) {
    var expressions = Arrays.stream(variables).map(Expression::new).collect(Collectors.toSet());
    var bound = new Bound(expressions);
    return bound(bound);
  }

  public FunArrayBuilder value(Interval value) {
    values.add(value);
    return this;
  }

  public FunArrayBuilder value(int lowerLimit, int upperLimit) {
    var value = Interval.of(lowerLimit, upperLimit);
    values.add(value);
    return this;
  }

  public FunArrayBuilder value(int value) {
    return value(value, value);
  }

  public FunArrayBuilder unknownValue() {
    values.add(Interval.unknown());
    return this;
  }

  public FunArrayBuilder unreachableValue() {
    values.add(Interval.unreachable());
    return this;
  }

  public FunArrayBuilder mightBeEmpty() {
    emptiness.add(true);
    return this;
  }

  public FunArray build() {
    if (bounds.size() - 1 != values.size() || values.size() != emptiness.size()) {
      throw new IllegalStateException();
    }
    return new FunArray(bounds, values, emptiness);
  }

}
