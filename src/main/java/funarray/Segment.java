package funarray;

import base.Interval;
import java.util.ArrayList;
import java.util.List;

/**
 * A single segment in a {@link Segmentation} consisting of a segment value and its trailing segment
 * bound.
 *
 * @param value         the value.
 * @param possiblyEmpty whether the segment might be empty.
 * @param expressions   the expressions contained in the trailing bound.
 */
public record Segment(Interval value, boolean possiblyEmpty, List<Expression> expressions) {

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
