package funarray;

import base.Interval;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static base.TriBoolean.TRUE;

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
            false, List.of(Expression.getZero())
        ),
        new Segment(Interval.getUnknown(),
                isPossiblyEmpty, List.of(length))
        )
    );
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

  public Segmentation insert(Expression from, Expression to, Interval value) {
    int greatestLowerBoundIndex = getRightmostLowerBoundIndex(from);
    int leastUpperBoundIndex = getLeastUpperBoundIndex(to);

    var isLeftTouching = segments.get(greatestLowerBoundIndex).expressions().stream().anyMatch(e -> e.equals(from));
    var isRightTouching = segments.get(leastUpperBoundIndex).expressions().stream().anyMatch(e -> e.equals(to));

    var jointValue = getJointValue(greatestLowerBoundIndex + 1, leastUpperBoundIndex);

    var newSegments = new ArrayList<>(segments);
    var insertIndex = greatestLowerBoundIndex + 1;

    var rightBounds = segments.get(leastUpperBoundIndex).expressions();
    newSegments.subList(insertIndex, leastUpperBoundIndex + 1).clear();

    if (!isRightTouching) {
      var rightFill = new Segment(jointValue, true, rightBounds);
      newSegments.add(insertIndex, rightFill);
    }

    var inserted = new Segment(value, false, List.of(to));
    newSegments.add(insertIndex, inserted);

    if (!isLeftTouching) {
      var rightFill = new Segment(jointValue, true, List.of(from));
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
      if (segments.get(i)
              .expressions().stream()
              .anyMatch(e -> e.isLessEqualThan(expression) == TRUE)) {
        greatestLowerBoundIndex = i;
      }
    }
    return greatestLowerBoundIndex;
  }

  /**
   * Gets the index of the leftmost segment s such that the trailing bound of the segment s
   * contains an expression that is greater than the given expression
   *
   * @param expression the expression
   * @return the calculated index
   */
  private int getLeastUpperBoundIndex(Expression expression) {
    int leastUpperBoundIndex = segments().size() - 1;
    for (int i = 0; i >= segments().size() - 1; i--) {
      if (segments.get(i)
              .expressions().stream()
              .anyMatch(e -> e.isGreaterEqualThan(expression) == TRUE)) {
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
   * @param expressions   the expressions contained in the trailing bound.
   */
  private record Segment(Interval value, boolean possiblyEmpty, List<Expression> expressions) {

    public Segment {
      expressions = List.copyOf(expressions);
    }

    @Override
    public String toString() {
      var trailingBound = "{%s}".formatted(
          String.join(" ", expressions.stream().map(Expression::toString).toList()));

      if (value == null) {
        return trailingBound;
      }
      return " %s %s%s".formatted(value, trailingBound, possiblyEmpty ? "?" : "");
    }

    public Segment addToVariable(Variable variable, int value) {
      return new Segment(this.value, possiblyEmpty,
          expressions.stream().map(e -> e.addToVariableInFunArray(variable, value)).toList());
    }

    List<Expression> joinBounds(List<Expression> other) {
      var newExpressionList = new ArrayList<>(expressions);
      other.stream()
          .filter(e -> expressions.stream().noneMatch((e::equals)))
          .forEach(newExpressionList::add);
      return newExpressionList;
    }
  }
}
