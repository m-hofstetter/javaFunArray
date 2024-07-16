package funarray;

import base.DomainValue;
import base.infint.InfInt;
import exception.FunArrayLogicException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
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
public record FunArray<ELEMENT_TYPE extends DomainValue<ELEMENT_TYPE>, VARIABLE_TYPE extends DomainValue<VARIABLE_TYPE>>(
        List<Bound<VARIABLE_TYPE>> bounds, List<ELEMENT_TYPE> values,
        List<Boolean> emptiness) {

  /**
   * Constructor for FunArray.
   *
   * @param bounds    the FunArray's segment bounds.
   * @param values    the FunArray's values.
   * @param emptiness a list determining whether a segment might be empty.
   */
  public FunArray(List<Bound<VARIABLE_TYPE>> bounds, List<ELEMENT_TYPE> values, List<Boolean> emptiness) {

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
  public FunArray<ELEMENT_TYPE, VARIABLE_TYPE> addToVariable(Variable<VARIABLE_TYPE> variable, InfInt value) {
    var newBounds = bounds.stream()
            .map(s -> s.addToVariableInFunArray(variable, value))
            .toList();
    return new FunArray<>(newBounds, values, emptiness);
  }

  public FunArray<ELEMENT_TYPE, VARIABLE_TYPE> insertExpression(Variable<VARIABLE_TYPE> variable, Expression<VARIABLE_TYPE> expression) {
    var newBounds = new ArrayList<>(bounds.stream()
            .map(b -> b.insertExpression(variable, expression))
            .toList());
    var newValues = new ArrayList<>(values);
    var newEmptiness = new ArrayList<>(emptiness);

    return new FunArray<>(newBounds, newValues, newEmptiness);
  }

  public FunArray<ELEMENT_TYPE, VARIABLE_TYPE> removeVariableOccurrences(Variable<VARIABLE_TYPE> variable) {
    var newBounds = new ArrayList<>(bounds.stream().map(b -> b.removeVariableOccurrences(variable)).toList());
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
    return new FunArray<>(newBounds, newValues, newEmptiness);
  }

  /**
   * Inserts a value into the FunArray.
   *
   * @param index the leading bound expression for the new value.
   * @param value the value to be inserted.
   * @return the modified Segmentation.
   */
  public FunArray<ELEMENT_TYPE, VARIABLE_TYPE> insert(Expression<VARIABLE_TYPE> index, ELEMENT_TYPE value) {
    var trailingIndex = index.increase(InfInt.of(1));
    int greatestLowerBoundIndex = getRightmostLowerBoundIndex(index);
    int leastUpperBoundIndex = getLeastUpperBoundIndex(trailingIndex);

    var jointValue = getJointValue(greatestLowerBoundIndex, leastUpperBoundIndex);

    var newBounds = new ArrayList<>(bounds);
    var newValues = new ArrayList<>(values);
    var newEmptiness = new ArrayList<>(this.emptiness);

    var leftAdjacent = bounds.get(greatestLowerBoundIndex).contains(index);
    var rightAdjacent = bounds.get(leastUpperBoundIndex).contains(trailingIndex);

    var rightSideStrictlyGreater = !newEmptiness.get(greatestLowerBoundIndex);
    var leftSideStrictlyLess = !newEmptiness.get(leastUpperBoundIndex - 1);

    if (leftAdjacent && rightAdjacent) {
      newValues.set(greatestLowerBoundIndex, value);
      return new FunArray<>(newBounds, newValues, newEmptiness);
    }

    var boundsSubList = newBounds.subList(greatestLowerBoundIndex + 1, leastUpperBoundIndex);
    var valuesSubList = newValues.subList(greatestLowerBoundIndex, leastUpperBoundIndex);
    var emptinessSubList = newEmptiness.subList(greatestLowerBoundIndex, leastUpperBoundIndex);

    if (rightSideStrictlyGreater && leftAdjacent) {
      newEmptiness.set(greatestLowerBoundIndex, true);
      emptinessSubList.addFirst(false);
      valuesSubList.addFirst(value);
      boundsSubList.addFirst(new Bound<>(trailingIndex));
      return new FunArray<>(newBounds, newValues, newEmptiness);
    }

    if (leftSideStrictlyLess && rightAdjacent) {
      newEmptiness.set(leastUpperBoundIndex, true);
      emptinessSubList.add(false);
      valuesSubList.add(value);
      boundsSubList.add(new Bound<>(index));
      return new FunArray<>(newBounds, newValues, newEmptiness);
    }

    boundsSubList.clear();
    valuesSubList.clear();
    emptinessSubList.clear();

    if (!leftAdjacent) {
      emptinessSubList.add(true);
      valuesSubList.add(jointValue);
      boundsSubList.add(new Bound<>(index));
    }

    emptinessSubList.add(false);
    valuesSubList.add(value);

    if (!rightAdjacent) {
      emptinessSubList.add(true);
      valuesSubList.add(jointValue);
      boundsSubList.add(new Bound<>(trailingIndex));
    }

    return new FunArray<>(newBounds, newValues, newEmptiness);
  }

  public ELEMENT_TYPE get(Expression<VARIABLE_TYPE> abstractIndex) {
    int greatestLowerBoundIndex = getRightmostLowerBoundIndex(abstractIndex);
    int leastUpperBoundIndex = getLeastUpperBoundIndex(abstractIndex.increase(InfInt.of(1)));
    return getJointValue(greatestLowerBoundIndex, leastUpperBoundIndex);
  }

  /**
   * Gets the index of the rightmost segment s such that the trailing bound of the segment s
   * contains an expression that is equal to or less than the given expression.
   *
   * @param expression the expression
   * @return the calculated index
   */
  private int getRightmostLowerBoundIndex(Expression<VARIABLE_TYPE> expression) {
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
  private int getLeastUpperBoundIndex(Expression<VARIABLE_TYPE> expression) {
    int leastUpperBoundIndex = bounds.size() - 1;
    for (int i = bounds.size() - 1; i >= 0; i--) {
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
  private ELEMENT_TYPE getJointValue(int from, int to) {
    var jointValue = values.get(from);
    for (int i = from + 1; i < to; i++) {
      jointValue = jointValue.join(values.get(i));
    }
    return jointValue;
  }

  public record UnifyResult<E extends DomainValue<E>, V extends DomainValue<V>>(
          FunArray<E, V> resultThis,
          FunArray<E, V> resultOther) {
  }

  /**
   * Unifies this FunArray with another one, so their segment bounds coincide.
   *
   * @param other               the other.
   * @param thisNeutralElement  the neutral element for this.
   * @param otherNeutralElement the neutral element for the other.
   * @return two unified FunArrays.
   */
  public UnifyResult<ELEMENT_TYPE, VARIABLE_TYPE> unify(FunArray<ELEMENT_TYPE, VARIABLE_TYPE> other,
                                                           ELEMENT_TYPE thisNeutralElement, ELEMENT_TYPE otherNeutralElement) {

    List<Bound<VARIABLE_TYPE>> boundsL = new ArrayList<>(this.bounds);
    List<ELEMENT_TYPE> valuesL = new ArrayList<>(this.values);
    List<Boolean> emptinessL = new ArrayList<>(this.emptiness);

    List<Bound<VARIABLE_TYPE>> boundsR = new ArrayList<>(other.bounds);
    List<ELEMENT_TYPE> valuesR = new ArrayList<>(other.values);
    List<Boolean> emptinessR = new ArrayList<>(other.emptiness);

    int i = 0;
    while (i < boundsL.size() && i < boundsR.size()) {

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

    i--;

    joinRemainingBounds(boundsL, i);
    joinRemainingBounds(boundsR, i);

    prune(valuesL, i);
    prune(valuesR, i);
    prune(emptinessL, i);
    prune(emptinessR, i);

    return new UnifyResult<>(
            new FunArray<>(boundsL, valuesL, emptinessL),
            new FunArray<>(boundsR, valuesR, emptinessR)
    );
  }

  /**
   * Joins the value in a list at a given index with the element proceeding it.
   *
   * @param list the list.
   * @param i    the index.
   */
  private static <ELEMENT_TYPE extends DomainValue<ELEMENT_TYPE>> void joinValueWithPredecessor(List<ELEMENT_TYPE> list, int i) {
    var joinedValue = list.get(i - 1).join(list.get(i));
    list.remove(i);
    list.remove(i - 1);
    list.add(joinedValue);
  }

  private static <VARIABLE_TYPE extends DomainValue<VARIABLE_TYPE>> void joinRemainingBounds(List<Bound<VARIABLE_TYPE>> bounds, int i) {
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
  private FunArray<ELEMENT_TYPE, VARIABLE_TYPE> unifyOperation(BinaryOperator<ELEMENT_TYPE> operation, FunArray<ELEMENT_TYPE, VARIABLE_TYPE> other,
                                                               ELEMENT_TYPE thisNeutralElement, ELEMENT_TYPE otherNeutralElement) {

    var unifiedArrays = this.unify(other, thisNeutralElement, otherNeutralElement);
    var thisUnified = unifiedArrays.resultThis();
    var otherUnified = unifiedArrays.resultOther();

    var modifiedValues = IntStream.range(0, thisUnified.values.size())
            .mapToObj(i -> operation.apply(thisUnified.values.get(i), otherUnified.values.get(i)))
            .toList();

    var modifiedEmptiness = IntStream.range(0, thisUnified.emptiness.size())
            .mapToObj(i -> thisUnified.emptiness.get(i) || otherUnified.emptiness.get(i))
            .toList();

    return new FunArray<>(thisUnified.bounds, modifiedValues, modifiedEmptiness);
  }

  public FunArray<ELEMENT_TYPE, VARIABLE_TYPE> satisfyBoundExpressionLessEqualThan(Expression<VARIABLE_TYPE> left, Expression<VARIABLE_TYPE> right) {
    int leftIndex;
    int rightIndex;
    try {
      leftIndex = findIndex(left);
      rightIndex = findIndex(right);
    } catch (IndexOutOfBoundsException e) {
      return this;
    }

    var modifiedBounds = new ArrayList<>(bounds);
    var modifiedValues = new ArrayList<>(values);
    var modifiedEmptiness = new ArrayList<>(emptiness);

    if (leftIndex <= rightIndex) {
      // Condition is already met, change nothing
      return this;
    } else if (modifiedEmptiness.subList(rightIndex, leftIndex).stream().anyMatch(e -> !e)) {
      // Bound order states that left expression is greater than right expression
      // Condition states that left expression is less equal than right expression
      // --> Condition cannot be satisfied
      throw new FunArrayLogicException("Condition cannot be satisfied!");
    } else {
      // Bound order states that left expression is greater equal than right expression
      // Condition states that left expression is less equal than right expression
      // --> left expression has to be equal to right expression --> Squash segments
      var boundsToBeSquashed = modifiedBounds.subList(rightIndex, leftIndex + 1);
      var squashedBoundExpressions = boundsToBeSquashed.stream()
              .flatMap(b -> b.expressions().stream())
              .collect(Collectors.toSet());
      boundsToBeSquashed.clear();
      boundsToBeSquashed.add(new Bound<>(squashedBoundExpressions));

      modifiedValues.subList(rightIndex, leftIndex).clear();
      modifiedEmptiness.subList(rightIndex, leftIndex).clear();

      return new FunArray<>(modifiedBounds, modifiedValues, modifiedEmptiness);
    }
  }

  public FunArray<ELEMENT_TYPE, VARIABLE_TYPE> satisfyBoundExpressionLessThan(Expression<VARIABLE_TYPE> left, Expression<VARIABLE_TYPE> right) {
    int leftIndex;
    int rightIndex;
    try {
      leftIndex = findIndex(left);
      rightIndex = findIndex(right);
    } catch (IndexOutOfBoundsException e) {
      return this;
    }

    if (leftIndex + 1 == rightIndex) {
      // Since the condition requires strict inequality, a single segment between the expressions cannot be empty.
      var modifiedEmptiness = new ArrayList<>(emptiness);
      modifiedEmptiness.set(leftIndex, false);
      return new FunArray<>(bounds, values, modifiedEmptiness);
    } else if (leftIndex < rightIndex) {
      // If there is more than one segment in between the expressions it cannot be decided which one is not empty.
      return this;
    } else {
      // Bound order states that left expression is greater than right expression
      // Condition states that left expression is less than right expression
      // --> Condition cannot be satisfied
      throw new FunArrayLogicException("Condition cannot be satisfied!");
    }
  }

  private int findIndex(Expression<VARIABLE_TYPE> expression) {
    for (int i = 0; i < bounds.size(); i++) {
      if (bounds.get(i).contains(expression)) {
        return i;
      }
    }
    throw new IndexOutOfBoundsException();
  }


  public FunArray<ELEMENT_TYPE, VARIABLE_TYPE> join(FunArray<ELEMENT_TYPE, VARIABLE_TYPE> other, ELEMENT_TYPE unreachable) {
    return unifyOperation(ELEMENT_TYPE::join, other, unreachable, unreachable);
  }

  public FunArray<ELEMENT_TYPE, VARIABLE_TYPE> meet(FunArray<ELEMENT_TYPE, VARIABLE_TYPE> other, ELEMENT_TYPE unknown) {
    return unifyOperation(ELEMENT_TYPE::join, other, unknown, unknown);
  }

  public FunArray<ELEMENT_TYPE, VARIABLE_TYPE> widen(FunArray<ELEMENT_TYPE, VARIABLE_TYPE> other, ELEMENT_TYPE unreachable) {
    return unifyOperation(ELEMENT_TYPE::join, other, unreachable, unreachable);
  }

  public FunArray<ELEMENT_TYPE, VARIABLE_TYPE> narrow(FunArray<ELEMENT_TYPE, VARIABLE_TYPE> other, ELEMENT_TYPE unknown) {
    return unifyOperation(ELEMENT_TYPE::join, other, unknown, unknown);
  }
}
