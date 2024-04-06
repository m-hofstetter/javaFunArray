package funarray;

import base.IntegerWithInfinity;
import base.Interval;
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
  public Segmentation(Variable length, boolean isPossiblyEmpty) {
    this(List.of(
        new Segment(null,
            false, List.of(Expression.getZero())
        ),
        new Segment(Interval.getUnknown(),
            isPossiblyEmpty, List.of(new Expression(length, new IntegerWithInfinity(0)))
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
}
