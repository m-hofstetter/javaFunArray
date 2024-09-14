package relation;

import static abstractdomain.TriBool.FALSE;
import static abstractdomain.TriBool.TRUE;
import static abstractdomain.TriBool.UNKNOWN;

import abstractdomain.DomainValue;
import abstractdomain.TriBool;
import funarray.FunArray;
import funarray.NormalExpression;
import java.util.ArrayList;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

public abstract class StrictInequality<T extends DomainValue<T>> extends Relation<T> {
  protected StrictInequality(BinaryOperator<T> satisfactionOperator, BiFunction<T, T, TriBool> satisfactionPredicate, Supplier<Relation<T>> inverseSupplier, Supplier<Relation<T>> complementSupplier) {
    super(satisfactionOperator, satisfactionPredicate, inverseSupplier, complementSupplier);
  }

  public <ElementT extends DomainValue<ElementT>> FunArray<ElementT> satisfyOrder(FunArray<ElementT> before, NormalExpression lesser, NormalExpression greater) {
    int leftIndex;
    int rightIndex;
    try {
      leftIndex = before.findIndex(lesser);
      rightIndex = before.findIndex(greater);
    } catch (IndexOutOfBoundsException e) {
      return before;
    }

    if (leftIndex + 1 == rightIndex) {
      // Since the condition requires strict inequality, a single segment between the expressions
      // cannot be empty.
      var modifiedEmptiness = new ArrayList<>(before.emptiness());
      modifiedEmptiness.set(leftIndex, false);
      return new FunArray<>(before.bounds(), before.values(), modifiedEmptiness);
    } else if (leftIndex < rightIndex) {
      // If there is more than one segment in between the expressions it cannot be decided which one
      // is not empty.
      return before;
    } else {
      // Bound order states that left expression is greater than right expression
      // Condition states that left expression is less than right expression
      // --> Condition cannot be satisfied --> Remove expressions in question

      var newBounds = before.bounds().stream()
              .map(b -> b.difference(Set.of(lesser, greater)))
              .toList();
      return new FunArray<>(newBounds, before.values(), before.emptiness())
              .removeEmptyBounds();
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
      return TRUE;
    }
    return FALSE;
  }
}
