package relation;

import static abstractdomain.TriBool.FALSE;
import static abstractdomain.TriBool.TRUE;
import static abstractdomain.TriBool.UNKNOWN;

import abstractdomain.DomainValue;
import abstractdomain.TriBool;
import funarray.Bound;
import funarray.FunArray;
import funarray.NormalExpression;
import java.util.ArrayList;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

public abstract class NonStrictInequality<T extends DomainValue<T>> extends Relation<T> {
  protected NonStrictInequality(BinaryOperator<T> satisfactionOperator, BiFunction<T, T, TriBool> satisfactionPredicate, Supplier<Relation<T>> inverseSupplier, Supplier<Relation<T>> complementSupplier) {
    super(satisfactionOperator, satisfactionPredicate, inverseSupplier, complementSupplier);
  }

  protected <ElementT extends DomainValue<ElementT>> FunArray<ElementT> satisfyOrder(FunArray<ElementT> before, NormalExpression lesser, NormalExpression greater) {
    int leftIndex;
    int rightIndex;
    try {
      leftIndex = before.findIndex(lesser);
      rightIndex = before.findIndex(greater);
    } catch (IndexOutOfBoundsException e) {
      return before;
    }

    var modifiedBounds = new ArrayList<>(before.bounds());
    var modifiedValues = new ArrayList<>(before.values());
    var modifiedEmptiness = new ArrayList<>(before.emptiness());

    if (leftIndex <= rightIndex) {
      // Condition is already met, change nothing
      return before;
    } else if (modifiedEmptiness.subList(rightIndex, leftIndex).stream().anyMatch(e -> !e)) {
      // Bound order states that left expression is greater than right expression
      // Condition states that left expression is less equal than right expression
      // --> Condition cannot be satisfied --> Remove expressions in question
      var newBounds = modifiedBounds.stream()
              .map(b -> b.difference(Set.of(lesser, greater)))
              .toList();
      return new FunArray<>(newBounds, before.values(), before.emptiness())
              .removeEmptyBounds();
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

  protected <ElementT extends DomainValue<ElementT>> TriBool isOrderSatisfied(FunArray<ElementT> array, NormalExpression lesser, NormalExpression greater) {
    int indexLesser;
    int indexGreater;
    try {
      indexLesser = array.findIndex(lesser);
      indexGreater = array.findIndex(greater);
    } catch (IndexOutOfBoundsException e) {
      return UNKNOWN;
    }

    if (indexLesser <= indexGreater) {
      if (array.emptiness().subList(indexLesser, indexGreater).stream().allMatch(e -> e)) {
        return UNKNOWN;
      }
      return TRUE;
    }
    return FALSE;
  }
}
