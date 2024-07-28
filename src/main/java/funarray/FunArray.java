package funarray;

import abstractdomain.DomainValue;
import exception.FunArrayLogicException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public record FunArray<ElementT extends DomainValue<ElementT>>(
        List<Bound> bounds, List<ElementT> values,
        List<Boolean> emptiness) {

  /**
   * Constructor for FunArray.
   *
   * @param bounds    the FunArray's segment bounds.
   * @param values    the FunArray's values.
   * @param emptiness a list determining whether a segment might be empty.
   */
  public FunArray(List<Bound> bounds, List<ElementT> values, List<Boolean> emptiness) {

    if (bounds.size() < 2) {
      throw new IllegalArgumentException("FunArray requires at least two bounds.");
    }

    if (values.size() != bounds.size() - 1) {
      throw new IllegalArgumentException(
              "Number of segment values does not match up with count of bound count. Needs to be"
                      + "exactly one less."
      );
    }

    if (emptiness.size() != bounds.size() - 1) {
      throw new IllegalArgumentException(
              "Number of emptiness values does not match up with count of bound count. Needs to be"
                      + "exactly one less."
      );
    }

    this.bounds = List.copyOf(bounds);
    this.values = List.copyOf(values);
    this.emptiness = List.copyOf(emptiness);
  }

  @Override
  public String toString() {
    return IntStream.range(0, bounds.size())
            .mapToObj(i -> {
              if (values().size() <= i) {
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
   * @param varRef   the variable reference.
   * @param value    the value.
   * @return the altered FunArray.
   */
  public FunArray<ElementT> addToVariable(String varRef, int value) {
    var newBounds = bounds.stream()
            .map(s -> s.addToVariableInFunArray(varRef, value))
            .toList();
    return new FunArray<>(newBounds, values, emptiness);
  }

  public FunArray<ElementT> insertExpression(String varRef, Expression expression) {
    var newBounds = new ArrayList<>(bounds.stream()
            .map(b -> b.insertExpressionIfVariablePresent(varRef, expression))
            .toList());
    var newValues = new ArrayList<>(values);
    var newEmptiness = new ArrayList<>(emptiness);

    return new FunArray<>(newBounds, newValues, newEmptiness);
  }

  public FunArray<ElementT> removeVariableOccurrences(String varRef) {
    return new FunArray<>(
            bounds.stream().map(b -> b.removeVariableOccurrences(varRef)).toList(),
            values, emptiness
    ).removeEmptyBounds();
  }


  public FunArray<ElementT> restrictExpressionOccurrences(Set<Expression> allowedExpressions) {
    var newBounds = bounds.stream()
            .map(b -> b.intersection(allowedExpressions))
            .toList();
    return new FunArray<>(newBounds, values, emptiness)
            .removeEmptyBounds();
  }

  public FunArray<ElementT> removeEmptyBounds() {
    var newBounds = new ArrayList<>(bounds);
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
  public FunArray<ElementT> insert(Expression index, ElementT value) {
    var trailingIndex = index.increase(1);
    int greatestLowerBoundIndex = getRightmostLowerBoundIndex(index);
    int leastUpperBoundIndex = getLeastUpperBoundIndex(trailingIndex);

    var newBounds = new ArrayList<>(bounds);
    var newValues = new ArrayList<>(values);
    var newEmptiness = new ArrayList<>(this.emptiness);

    var leftAdjacent = bounds.get(greatestLowerBoundIndex).contains(index);
    var rightAdjacent = bounds.get(leastUpperBoundIndex).contains(trailingIndex);

    if (leftAdjacent && rightAdjacent) {
      newValues.set(greatestLowerBoundIndex, value);
      return new FunArray<>(newBounds, newValues, newEmptiness);
    }

    Bound leftBound = new Bound(index);
    Bound rightBound = new Bound(trailingIndex);

    if (rightAdjacent) {
      for (int i = leastUpperBoundIndex - 1; i > greatestLowerBoundIndex; i--) {
        if (!newEmptiness.get(i)) {
          greatestLowerBoundIndex = i;
          break;
        }
      }
      rightBound = newBounds.get(leastUpperBoundIndex);
      leftBound = rightBound.increase(-1);
    }

    if (leftAdjacent) {
      for (int i = greatestLowerBoundIndex; i < leastUpperBoundIndex; i++) {
        if (!newEmptiness.get(i)) {
          leastUpperBoundIndex = i + 1;
          break;
        }
      }
      leftBound = newBounds.get(greatestLowerBoundIndex);
      rightBound = leftBound.increase(1);
    }

    var jointValue = getJointValue(greatestLowerBoundIndex, leastUpperBoundIndex);

    var boundsSubList = newBounds.subList(greatestLowerBoundIndex + 1, leastUpperBoundIndex);
    var valuesSubList = newValues.subList(greatestLowerBoundIndex, leastUpperBoundIndex);
    var emptinessSubList = newEmptiness.subList(greatestLowerBoundIndex, leastUpperBoundIndex);

    boundsSubList.clear();
    valuesSubList.clear();
    emptinessSubList.clear();

    if (!leftAdjacent) {
      emptinessSubList.add(true);
      valuesSubList.add(jointValue);
      boundsSubList.add(leftBound);
    }

    emptinessSubList.add(false);
    valuesSubList.add(value);

    if (!rightAdjacent) {
      emptinessSubList.add(true);
      valuesSubList.add(jointValue);
      boundsSubList.add(rightBound);
    }

    return new FunArray<>(newBounds, newValues, newEmptiness);
  }

  public ElementT get(Expression abstractIndex) {
    int greatestLowerBoundIndex = getRightmostLowerBoundIndex(abstractIndex);
    int leastUpperBoundIndex = getLeastUpperBoundIndex(abstractIndex.increase(1));
    return getJointValue(greatestLowerBoundIndex, leastUpperBoundIndex);
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
      if (bounds.get(i).contains(e -> e.isLessEqualThan(expression))) {
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
      if (bounds.get(i).contains(e -> e.isGreaterEqualThan(expression))) {
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
  private ElementT getJointValue(int from, int to) {
    var jointValue = values.get(from);
    for (int i = from + 1; i < to; i++) {
      jointValue = jointValue.join(values.get(i));
    }
    return jointValue;
  }

  public record UnifyResult<E extends DomainValue<E>>(
          FunArray<E> resultThis,
          FunArray<E> resultOther) {
  }

  /**
   * Unifies this FunArray with another one, so their segment bounds coincide.
   *
   * @param other               the other.
   * @param thisNeutralElement  the neutral element for this.
   * @param otherNeutralElement the neutral element for the other.
   * @return two unified FunArrays.
   */
  public UnifyResult<ElementT> unify(FunArray<ElementT> other,
                                     ElementT thisNeutralElement, ElementT otherNeutralElement) {

    var thisExpressions = this.getExpressions();
    var otherExpressions = other.getExpressions();

    var commonExpressions = new HashSet<>(thisExpressions);
    commonExpressions.retainAll(otherExpressions);

    var thisReduced = this.restrictExpressionOccurrences(commonExpressions);
    var otherReduced = other.restrictExpressionOccurrences(commonExpressions);

    List<Bound> boundsThis = new ArrayList<>(thisReduced.bounds);
    List<ElementT> valuesThis = new ArrayList<>(thisReduced.values);
    List<Boolean> emptinessThis = new ArrayList<>(thisReduced.emptiness);

    List<Bound> boundsOther = new ArrayList<>(otherReduced.bounds);
    List<ElementT> valuesOther = new ArrayList<>(otherReduced.values);
    List<Boolean> emptinessOther = new ArrayList<>(otherReduced.emptiness);


    int i = 0;
    while (i < boundsThis.size() && i < boundsOther.size()) {
      var currentBoundThis = boundsThis.get(i);
      var currentBoundOther = boundsOther.get(i);

      var intersection = currentBoundThis.intersection(currentBoundOther);
      var difference = currentBoundThis.difference(currentBoundOther);
      var relativeComplement = currentBoundThis.relativeComplement(currentBoundOther);

      if (!difference.isEmpty()) {
        boundsThis.set(i, difference);
        boundsThis.add(i, intersection);
        valuesThis.add(i, thisNeutralElement);
        emptinessThis.add(i, true);
      }

      if (!relativeComplement.isEmpty()) {
        boundsOther.set(i, relativeComplement);
        boundsOther.add(i, intersection);
        valuesOther.add(i, otherNeutralElement);
        emptinessOther.add(i, true);
      }
      i++;
    }

    return new UnifyResult<>(
            new FunArray<>(boundsThis, valuesThis, emptinessThis),
            new FunArray<>(boundsOther, valuesOther, emptinessOther)
    );
  }

  /**
   * Joins the value in a list at a given index with the element proceeding it.
   *
   * @param list the list.
   * @param i    the index.
   */
  private static <ElementT extends DomainValue<ElementT>>
  void joinValueWithPredecessor(List<ElementT> list, int i) {

    var joinedValue = list.get(i - 1).join(list.get(i));
    list.remove(i);
    list.remove(i - 1);
    list.add(i - 1, joinedValue);
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
  private FunArray<ElementT> unifyOperation(BinaryOperator<ElementT> operation,
                                            FunArray<ElementT> other,
                                            ElementT thisNeutralElement,
                                            ElementT otherNeutralElement) {

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

  public FunArray<ElementT> satisfyBoundExpressionLessEqualThan(Expression left, Expression right) {
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
      var squashedBound = Bound.union(boundsToBeSquashed);
      boundsToBeSquashed.clear();
      boundsToBeSquashed.add(squashedBound);

      modifiedValues.subList(rightIndex, leftIndex).clear();
      modifiedEmptiness.subList(rightIndex, leftIndex).clear();

      return new FunArray<>(modifiedBounds, modifiedValues, modifiedEmptiness);
    }
  }

  public FunArray<ElementT> satisfyBoundExpressionLessThan(Expression left, Expression right) {
    int leftIndex;
    int rightIndex;
    try {
      leftIndex = findIndex(left);
      rightIndex = findIndex(right);
    } catch (IndexOutOfBoundsException e) {
      return this;
    }

    if (leftIndex + 1 == rightIndex) {
      // Since the condition requires strict inequality, a single segment between the expressions
      // cannot be empty.
      var modifiedEmptiness = new ArrayList<>(emptiness);
      modifiedEmptiness.set(leftIndex, false);
      return new FunArray<>(bounds, values, modifiedEmptiness);
    } else if (leftIndex < rightIndex) {
      // If there is more than one segment in between the expressions it cannot be decided which one
      // is not empty.
      return this;
    } else {
      // Bound order states that left expression is greater than right expression
      // Condition states that left expression is less than right expression
      // --> Condition cannot be satisfied
      throw new FunArrayLogicException("Condition cannot be satisfied!");
    }
  }

  private int findIndex(Expression expression) {
    for (int i = 0; i < bounds.size(); i++) {
      if (bounds.get(i).contains(expression)) {
        return i;
      }
    }
    throw new IndexOutOfBoundsException();
  }


  public FunArray<ElementT> join(FunArray<ElementT> other, ElementT unreachable) {
    return unifyOperation(ElementT::join, other, unreachable, unreachable);
  }

  public FunArray<ElementT> meet(FunArray<ElementT> other, ElementT unknown) {
    return unifyOperation(ElementT::meet, other, unknown, unknown);
  }

  public FunArray<ElementT> widen(FunArray<ElementT> other, ElementT unreachable) {
    return unifyOperation(ElementT::widen, other, unreachable, unreachable);
  }

  public FunArray<ElementT> narrow(FunArray<ElementT> other, ElementT unknown) {
    return unifyOperation(ElementT::narrow, other, unknown, unknown);
  }

  public Set<Expression> getExpressions() {
    return bounds.stream().flatMap(e -> e.expressions().stream()).collect(Collectors.toSet());
  }
}
