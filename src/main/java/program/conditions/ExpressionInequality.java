package program.conditions;

import base.interval.Interval;
import exception.FunArrayLogicException;
import funarray.Bound;
import funarray.Expression;
import funarray.FunArray;
import java.util.ArrayList;
import java.util.stream.Collectors;

public abstract class ExpressionInequality implements Condition {

  Expression left;
  Expression right;

  static FunArray<Interval> lessEqualThan(Expression left, Expression right, FunArray<Interval> funArray) {

    int leftIndex;
    int rightIndex;
    try {
      leftIndex = findIndex(left, funArray);
      rightIndex = findIndex(right, funArray);
    } catch (IndexOutOfBoundsException e) {
      return funArray;
    }

    var bounds = new ArrayList<>(funArray.bounds());
    var values = new ArrayList<>(funArray.values());
    var emptiness = new ArrayList<>(funArray.emptiness());

    if (leftIndex <= rightIndex) {
      // Condition is already met, change nothing
      return funArray;
    } else if (emptiness.subList(rightIndex, leftIndex).stream().anyMatch(e -> !e)) {
      // Bound order states that left expression is greater than right expression
      // Condition states that left expression is less equal than right expression
      // --> Condition cannot be satisfied
      throw new FunArrayLogicException("Condition cannot be satisfied!");
    } else {
      // Bound order states that left expression is greater equal than right expression
      // Condition states that left expression is less equal than right expression
      // --> left expression has to be equal to right expression --> Squash segments
      var boundsToBeSquashed = bounds.subList(rightIndex, leftIndex + 1);
      var squashedBoundExpressions = boundsToBeSquashed.stream()
              .flatMap(b -> b.expressions().stream())
              .collect(Collectors.toSet());
      boundsToBeSquashed.clear();
      boundsToBeSquashed.add(new Bound(squashedBoundExpressions));

      values.subList(rightIndex, leftIndex).clear();
      emptiness.subList(rightIndex, leftIndex).clear();

      return new FunArray<>(bounds, values, emptiness);
    }
  }

  static FunArray<Interval> lessThan(Expression left, Expression right, FunArray<Interval> funArray) {

    int leftIndex;
    int rightIndex;
    try {
      leftIndex = findIndex(left, funArray);
      rightIndex = findIndex(right, funArray);
    } catch (IndexOutOfBoundsException e) {
      return funArray;
    }

    if (leftIndex + 1 == rightIndex) {
      // Since the condition requires strict inequality, a single segment between the expressions cannot be empty.
      var emptiness = new ArrayList<>(funArray.emptiness());
      emptiness.set(leftIndex, false);
      return new FunArray<>(funArray.bounds(), funArray.values(), emptiness);
    } else if (leftIndex < rightIndex) {
      // If there is more than one segment in between the expressions it cannot be decided which one is not empty.
      return funArray;
    } else {
      // Bound order states that left expression is greater than right expression
      // Condition states that left expression is less than right expression
      // --> Condition cannot be satisfied
      throw new FunArrayLogicException("Condition cannot be satisfied!");
    }
  }

  private static int findIndex(Expression expression, FunArray<Interval> environment) {
    var bounds = environment.bounds();
    for (int i = 0; i < bounds.size(); i++) {
      if (bounds.get(i).contains(expression)) {
        return i;
      }
    }
    throw new IndexOutOfBoundsException();
  }

  @Override
  public abstract String toString();
}
