package funarray;

import base.Interval;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * The abstract segmentation for the {@link FunArray}.
 *
 */
public record Segmentation(List<Bound> bounds, List<Interval> values, List<Boolean> possiblyEmpty) {

  public Segmentation {
    bounds = List.copyOf(bounds);
    values = List.copyOf(values);
    possiblyEmpty = List.copyOf(possiblyEmpty);
  }

  /**
   * Constructor for an abstract segmentation consisting of a single value and its bounds.
   *
   * @param length          the length of the segment.
   * @param isPossiblyEmpty whether the single segment might be empty.
   */
  public Segmentation(Expression length, boolean isPossiblyEmpty) {
    this(List.of(Bound.ofConstant(0), Bound.of(length)),
            List.of(Interval.getUnknown()),
            List.of(isPossiblyEmpty)
    );
  }

  @Override
  public String toString() {
    return IntStream.range(0, bounds.size())
            .mapToObj(i -> {
              if (values().size() <= i || possiblyEmpty().size() <= i) {
                return bounds.get(i).toString();
              }
              return "%s%s %s".formatted(bounds.get(i), possiblyEmpty.get(i) ? "?" : "", values.get(i));
            })
            .collect(Collectors.joining(" "));
  }

  public Segmentation addToVariable(Variable variable, int value) {
    return new Segmentation(bounds.stream().map(s -> s.addToVariable(variable, value)).toList(), values, possiblyEmpty);
  }

  /**
   * Inserts a value into the segmentation.
   *
   * @param from  the leading bound expression for the new value.
   * @param to    the trailing bound expression for the new value.
   * @param value the value to be inserted.
   * @return the modified Segmentation.
   */
  public Segmentation insert(Expression from, Expression to, Interval value) {
    int greatestLowerBoundIndex = getRightmostLowerBoundIndex(from);
    int leastUpperBoundIndex = getLeastUpperBoundIndex(to);

    var jointValue = getJointValue(greatestLowerBoundIndex, leastUpperBoundIndex - 1);

    var newBounds = new ArrayList<>(bounds);
    var newValues = new ArrayList<>(values);
    var newPossiblyEmpty = new ArrayList<>(possiblyEmpty);

    var boundsSubList = newBounds.subList(greatestLowerBoundIndex + 1, leastUpperBoundIndex);
    var valuesSubList = newValues.subList(greatestLowerBoundIndex, leastUpperBoundIndex);
    var possiblyEmptySubList = newPossiblyEmpty.subList(greatestLowerBoundIndex, leastUpperBoundIndex);

    boundsSubList.clear();
    valuesSubList.clear();
    possiblyEmptySubList.clear();

    //no left touching
    if (!bounds.get(greatestLowerBoundIndex).expressionEquals(from)) {
      possiblyEmptySubList.add(true);
      valuesSubList.add(jointValue);
      boundsSubList.add(Bound.of(from));
    }

    possiblyEmptySubList.add(false);
    valuesSubList.add(value);

    // no right touching
    if (!bounds.get(leastUpperBoundIndex).expressionEquals(to)) {
      possiblyEmptySubList.add(true);
      valuesSubList.add(jointValue);
      boundsSubList.add(Bound.of(to));
    }

    return new Segmentation(newBounds, newValues, newPossiblyEmpty);
  }

  /**
   * Gets the index of the rightmost segment s such that the trailing bound of the segment s
   * contains an expression that is equal to or less than the given expression.
   *
   * @param expression the expression
   * @return the calculated index
   */
  private int getRightmostLowerBoundIndex(Expression expression) {
    int greatestLowerBoundIndex = 0;
    for (int i = 0; i <= bounds.size() - 1; i++) {
      if (bounds.get(i).expressionIsLessEqualThan(expression)) {
        greatestLowerBoundIndex = i;
      }
    }
    return greatestLowerBoundIndex;
  }

  /**
   * Gets the index of the leftmost segment s such that the trailing bound of the segment s
   * contains an expression that is greater than the given expression.
   *
   * @param expression the expression
   * @return the calculated index
   */
  private int getLeastUpperBoundIndex(Expression expression) {
    int leastUpperBoundIndex = bounds.size() - 1;
    for (int i = 0; i >= bounds.size() - 1; i--) {
      if (bounds.get(i).expressionIsGreaterEqualThan(expression)) {
        leastUpperBoundIndex = i;
      }
    }
    return leastUpperBoundIndex;
  }

  /**
   * Returns the joint of the values from the given segments.
   *
   * @param from the index of the first segment.
   * @param to   the index of the last segment (inclusive).
   * @return the joint of all values.
   */
  private Interval getJointValue(int from, int to) {
    var jointValue = Interval.UNREACHABLE;
    for (int i = from; i <= to; i++) {
      jointValue = jointValue.join(values.get(i));
    }
    return jointValue;
  }
}
