package funarray;

import base.Interval;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * The abstract segmentation for the {@link FunArray}.
 *
 * @param segments the segments contained in the segmentation.
 */
public record Segmentation(List<Segment> segments) {

  public Segmentation {
    segments = List.copyOf(segments);
  }

  /**
   * Constructor for an abstract segmentation consisting of a single value and its bounds.
   *
   * @param length          the length of the segment.
   * @param isPossiblyEmpty whether the single segment might be empty.
   */
  public Segmentation(Expression length, boolean isPossiblyEmpty) {
    this(List.of(
        new Segment(null,
                false, Bound.ofConstant(0)
        ),
        new Segment(Interval.getUnknown(),
                isPossiblyEmpty, Bound.of(length)
        )
    ));
  }

  @Override
  public String toString() {
    return segments.stream()
        .map(Segment::toString)
        .collect(Collectors.joining());
  }

  public Segmentation addToVariable(Variable variable, int value) {
    return new Segmentation(segments.stream().map(s -> s.addToVariable(variable, value)).toList());
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

    var jointValue = getJointValue(greatestLowerBoundIndex + 1, leastUpperBoundIndex);

    var newSegments = new ArrayList<>(segments);
    var insertIndex = greatestLowerBoundIndex + 1;

    var rightBound = segments.get(leastUpperBoundIndex).bound();
    newSegments.subList(insertIndex, leastUpperBoundIndex + 1).clear();

    if (!segments.get(leastUpperBoundIndex).bound.expressionEquals(to)) {
      var rightFill = new Segment(jointValue, true, rightBound);
      newSegments.add(insertIndex, rightFill);
    }

    var inserted = new Segment(value, false, Bound.of(to));
    newSegments.add(insertIndex, inserted);

    if (!segments.get(greatestLowerBoundIndex).bound.expressionEquals(from)) {
      var rightFill = new Segment(jointValue, true, Bound.of(from));
      newSegments.add(insertIndex, rightFill);
    }
    return new Segmentation(newSegments);
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
    for (int i = 0; i <= segments().size() - 1; i++) {
      if (segments.get(i).bound.expressionIsLessEqualThan(expression)) {
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
    int leastUpperBoundIndex = segments().size() - 1;
    for (int i = 0; i >= segments().size() - 1; i--) {
      if (segments.get(i).bound.expressionIsGreaterEqualThan(expression)) {
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
      jointValue = jointValue.join(segments().get(i).value());
    }
    return jointValue;
  }

  /**
   * A single segment in a {@link Segmentation} consisting of a segment value and its trailing
   * segment bound.
   *
   * @param value         the value.
   * @param possiblyEmpty whether the segment might be empty.
   * @param bound         the  trailing bound.
   */
  public record Segment(Interval value, boolean possiblyEmpty, Bound bound) {

    @Override
    public String toString() {
      if (value == null) {
        return bound.toString();
      }
      return " %s %s%s".formatted(value, bound.toString(), possiblyEmpty ? "?" : "");
    }

    public Segment addToVariable(Variable variable, int value) {
      return new Segment(this.value, possiblyEmpty, bound.addToVariable(variable, value));
    }
  }
}
