package funarray;

import base.infint.InfInt;
import base.interval.Interval;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * A FunArray to represent an Array in abstract interpretation static analysis. See Patrick Cousot,
 * Radhia Cousot, and Francesco Logozzo. 2011. A parametric segmentation functor for fully automatic
 * and scalable array content analysis. SIGPLAN Not. 46, 1 (January 2011), 105–118. <a
 * href="https://doi.org/10.1145/1925844.1926399">https://doi.org/10.1145/1925844.1926399</a>
 *
 * @param bounds    the FunArray's segment bounds.
 * @param values    the FunArray's values.
 * @param emptiness a list determining whether a segment might be empty.
 */
public record FunArray(List<Bound> bounds, List<Interval> values, List<Boolean> emptiness) {

  /**
   * Constructor for FunArray.
   *
   * @param bounds    the FunArray's segment bounds.
   * @param values    the FunArray's values.
   * @param emptiness a list determining whether a segment might be empty.
   */
  public FunArray(List<Bound> bounds, List<Interval> values, List<Boolean> emptiness) {

    if (bounds.size() < 2) {
      throw new IllegalArgumentException("FunArray requires at least two bounds.");
    }

    if (values.size() != bounds.size() - 1) {
      throw new IllegalArgumentException("Number of segment values does not match up with count of bound count. Needs to be exactly one less.");
    }

    if (emptiness.size() != bounds.size() - 1) {
      throw new IllegalArgumentException("Number of emptiness values does not match up with count of bound count. Needs to be exactly one less.");
    }

    this.bounds = List.copyOf(bounds);
    this.values = List.copyOf(values);
    this.emptiness = List.copyOf(emptiness);
  }

  /**
   * Constructor for a FunArray consisting of a single value and its bounds.
   *
   * @param length          the length of the segment.
   * @param isPossiblyEmpty whether the single segment might be empty.
   */
  public FunArray(Expression length, boolean isPossiblyEmpty) {
    this(List.of(new Bound(0), new Bound(length)),
            List.of(Interval.unknown()),
            List.of(isPossiblyEmpty)
    );
  }

  @Override
  public String toString() {
    return IntStream.range(0, bounds.size())
            .mapToObj(i -> {
              if (values().size() <= i || emptiness().size() <= i) {
                return "%s%s".formatted(
                        bounds.get(i),
                        emptiness.get(i - 1) ? "?" : ""
                );
              }
              if (i == 0) {
                return "%s %s".formatted(
                        bounds.get(i),
                        values.get(i)
                );
              }
              return "%s%s %s".formatted(
                      bounds.get(i),
                      emptiness.get(i - 1) ? "?" : "",
                      values.get(i)
              );
            })
            .collect(Collectors.joining(" "));
  }

  /**
   * Adapt FunArray to a changed variable value.
   *
   * @param variable the variable.
   * @param value    the value.
   * @return the altered FunArray.
   */
  public FunArray addToVariable(Variable variable, InfInt value) {
    var newBounds = bounds.stream()
            .map(s -> s.addToVariableInFunArray(variable, value))
            .toList();
    return new FunArray(newBounds, values, emptiness);
  }

  public FunArray assignVariable(Variable variable, Expression expression) {
    var newBounds = new ArrayList<>(bounds.stream().map(b -> b.assignVariableInFunArray(variable, expression)).toList());
    var newValues = new ArrayList<>(values);
    var newEmptiness = new ArrayList<>(emptiness);

    var i = 1;
    while (i < newBounds.size()) {
      if (newBounds.get(i).isEmpty()) {
        joinValueWithPredecessor(newValues, i);
        newBounds.remove(i);
        newEmptiness.set(i - 1, newEmptiness.get(i - 1) && newEmptiness.get(i));
        newEmptiness.remove(i);
        continue;
      }
      i++;
    }
    return new FunArray(newBounds, newValues, newEmptiness);
  }

  public FunArray assignVariable(Variable variable, Interval interval) {
    var newBounds = new ArrayList<>(bounds.stream().map(b -> b.assignVariableInFunArray(variable, interval)).toList());
    var newValues = new ArrayList<>(values);
    var newEmptiness = new ArrayList<>(emptiness);

    var i = 1;
    while (i < newBounds.size()) {
      if (newBounds.get(i).isEmpty()) {
        joinValueWithPredecessor(newValues, i);
        newBounds.remove(i);
        newEmptiness.set(i - 1, newEmptiness.get(i - 1) && newEmptiness.get(i));
        newEmptiness.remove(i);
        continue;
      }
      i++;
    }
    return new FunArray(newBounds, newValues, newEmptiness);
  }


  public FunArray modify(Expression from, Expression to, UnaryOperator<Interval> modifyValue) {
    int greatestLowerBoundIndex = getRightmostLowerBoundIndex(from);
    int leastUpperBoundIndex = getLeastUpperBoundIndex(to);

    var jointValue = getJointValue(greatestLowerBoundIndex, leastUpperBoundIndex - 1);

    var newBounds = new ArrayList<>(bounds);
    var newValues = new ArrayList<>(values);
    var newPossiblyEmpty = new ArrayList<>(emptiness);

    var boundsSubList = newBounds.subList(greatestLowerBoundIndex + 1, leastUpperBoundIndex);
    var valuesSubList = newValues.subList(greatestLowerBoundIndex, leastUpperBoundIndex);
    var emptinessSubList = newPossiblyEmpty.subList(greatestLowerBoundIndex, leastUpperBoundIndex);

    boundsSubList.clear();
    valuesSubList.clear();
    emptinessSubList.clear();

    //no left touching
    if (!bounds.get(greatestLowerBoundIndex).expressionEquals(from)) {
      emptinessSubList.add(true);
      valuesSubList.add(jointValue);
      boundsSubList.add(new Bound(from));
    }

    emptinessSubList.add(false);
    valuesSubList.add(modifyValue.apply(jointValue));

    // no right touching
    if (!bounds.get(leastUpperBoundIndex).expressionEquals(to)) {
      emptinessSubList.add(true);
      valuesSubList.add(jointValue);
      boundsSubList.add(new Bound(to));
    }

    return new FunArray(newBounds, newValues, newPossiblyEmpty);
  }

  /**
   * Inserts a value into the FunArray.
   *
   * @param from  the leading bound expression for the new value.
   * @param to    the trailing bound expression for the new value.
   * @param value the value to be inserted.
   * @return the modified Segmentation.
   */
  public FunArray insert(Expression from, Expression to, Interval value) {
    return modify(from, to, _ -> value);
  }

  /**
   * Inserts a value into the FunArray.
   *
   * @param from  the leading bound variable for the new value.
   * @param to    the trailing bound variable for the new value.
   * @param value the value to be inserted.
   * @return the modified Segmentation.
   */
  public FunArray insert(Variable from, Variable to, Interval value) {
    return modify(new Expression(from), new Expression(to), _ -> value);
  }

  public Interval get(Expression abstractIndex) {
    int greatestLowerBoundIndex = getRightmostLowerBoundIndex(abstractIndex);
    int leastUpperBoundIndex = getLeastUpperBoundIndex(abstractIndex);
    return getJointValue(greatestLowerBoundIndex, leastUpperBoundIndex - 1);
  }

  public FunArray assumeContentLeqThan(Expression from, Expression to, Expression compare) {
    return modify(from, to, e -> e.assumeLessEqThan(compare));
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
   * Gets the index of the leftmost segment s such that the trailing bound of the segment s contains
   * an expression that is greater than the given expression.
   *
   * @param expression the expression
   * @return the calculated index
   */
  private int getLeastUpperBoundIndex(Expression expression) {
    int leastUpperBoundIndex = bounds.size() - 1;
    for (int i = bounds.size() - 1; i >= 0; i--) {
      if (bounds.get(i).expressionIsGreaterThan(expression)) {
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
    var jointValue = Interval.unreachable();
    for (int i = from; i <= to; i++) {
      jointValue = jointValue.join(values.get(i));
    }
    return jointValue;
  }

  /**
   * Unifies this FunArray with another one, so their segment bounds coincide.
   *
   * @param other               the other.
   * @param thisNeutralElement  the neutral element for this.
   * @param otherNeutralElement the neutral element for the other.
   * @return a list of two unified FunArrays.
   */
  public List<FunArray> unify(FunArray other,
                              Interval thisNeutralElement, Interval otherNeutralElement) {

    List<Bound> boundsL = new ArrayList<>(this.bounds);
    List<Interval> valuesL = new ArrayList<>(this.values);
    List<Boolean> emptinessL = new ArrayList<>(this.emptiness);

    List<Bound> boundsR = new ArrayList<>(other.bounds);
    List<Interval> valuesR = new ArrayList<>(other.values);
    List<Boolean> emptinessR = new ArrayList<>(other.emptiness);

    int i = 0;
    while (i < boundsL.size() - 1 && i < boundsR.size() - 1) {

      var currentBoundL = boundsL.get(i);
      var currentBoundR = boundsR.get(i);

      var intersection = currentBoundR.intersect(currentBoundL);

      if (intersection.isEmpty()) {
        joinValueWithPredecessor(valuesL, i);
        boundsL.remove(i);
        emptinessL.remove(i);
        joinValueWithPredecessor(valuesR, i);
        boundsR.remove(i);
        emptinessR.remove(i);
        continue;
      }

      var complementInThis = currentBoundL.getComplementBound(currentBoundR);
      var complementInThisWithOccurrencesInOther = complementInThis
              .intersect(boundsR.subList(i, boundsR.size()));

      boundsL.remove(i);
      boundsR.remove(i);

      if (!complementInThisWithOccurrencesInOther.isEmpty()) {
        boundsL.add(i, complementInThisWithOccurrencesInOther);
        emptinessL.add(i, true);
        valuesL.add(i, thisNeutralElement);
      }

      var complementInOther = currentBoundR.getComplementBound(currentBoundL);
      var complementInOtherWithOccurrencesInThis = complementInOther
              .intersect(boundsL.subList(i, boundsL.size()));

      if (!complementInOtherWithOccurrencesInThis.isEmpty()) {
        boundsR.add(i, complementInOtherWithOccurrencesInThis);
        emptinessR.add(i, true);
        valuesR.add(i, otherNeutralElement);
      }

      boundsL.add(i, intersection);
      boundsR.add(i, intersection);
      i++;
    }

    joinRemainingBounds(boundsL, i);
    joinRemainingBounds(boundsR, i);

    prune(valuesL, i);
    prune(valuesR, i);
    prune(emptinessL, i);
    prune(emptinessR, i);

    return List.of(
            new FunArray(boundsL, valuesL, emptinessL),
            new FunArray(boundsR, valuesR, emptinessR)
    );
  }

  /**
   * Joins the value in a list at a given index with the element proceeding it.
   *
   * @param list the list.
   * @param i    the index.
   */
  private static void joinValueWithPredecessor(List<Interval> list, int i) {
    var joinedValue = list.get(i - 1).join(list.get(i));
    list.remove(i);
    list.remove(i - 1);
    list.add(joinedValue);
  }

  private static void joinRemainingBounds(List<Bound> bounds, int i) {
    var remainingBounds = bounds.subList(i, bounds.size());
    var joinedBound = Bound.join(remainingBounds);
    remainingBounds.clear();
    remainingBounds.add(joinedBound);
  }

  private static <T> void prune(List<T> list, int i) {
    if (i < list.size()) {
      list.subList(i, list.size()).clear();
    }
  }

  /**
   * Utility function. The abstract domain functions join, meet, widen and narrow utilise the same
   * with only the neutral elements and the operation that is applied on the values is different.
   *
   * @param operation           the operation applied on the values.
   * @param other               the other FunArray.
   * @param thisNeutralElement  the neutral element for unifying of this FunArray.
   * @param otherNeutralElement the neutral element for unifying of the other FunArray.
   * @return the joined/met/widened/narrowed FunArray
   */
  private FunArray unifyOperation(BinaryOperator<Interval> operation, FunArray other,
                                  Interval thisNeutralElement, Interval otherNeutralElement) {

    var unifiedArrays = this.unify(other, thisNeutralElement, otherNeutralElement);
    var thisUnified = unifiedArrays.getFirst();
    var otherUnified = unifiedArrays.getLast();

    var modifiedValues = IntStream.range(0, thisUnified.values.size())
            .mapToObj(i -> operation.apply(thisUnified.values.get(i), otherUnified.values.get(i)))
            .toList();

    var modifiedEmptiness = IntStream.range(0, thisUnified.emptiness.size())
            .mapToObj(i -> thisUnified.emptiness.get(i) || otherUnified.emptiness.get(i))
            .toList();

    return new FunArray(thisUnified.bounds, modifiedValues, modifiedEmptiness);
  }

  public FunArray join(FunArray other) {
    return unifyOperation(Interval::join, other, Interval.unreachable(), Interval.unreachable());
  }

  public FunArray meet(FunArray other) {
    return unifyOperation(Interval::join, other, Interval.unknown(), Interval.unknown());
  }

  public FunArray widen(FunArray other) {
    return unifyOperation(Interval::join, other, Interval.unreachable(), Interval.unreachable());
  }

  public FunArray narrow(FunArray other) {
    return unifyOperation(Interval::join, other, Interval.unknown(), Interval.unknown());
  }
}
