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

public class NotEqual<T extends DomainValue<T>> extends Relation<T> {

  public NotEqual() {
    super(T::satisfyNotEqual, T::notEqual, NotEqual::new, Equal::new);
  }

  @Override
  public <ElementT extends DomainValue<ElementT>> FunArray<ElementT> satisfy(FunArray<ElementT> before, NormalExpression a, NormalExpression b) {
    int indexA;
    int indexB;

    try {
      indexA = before.findIndex(a);
      indexB = before.findIndex(b);
    } catch (IndexOutOfBoundsException e) {
      return before;
    }

    int leftIndex = Integer.min(indexA, indexB);
    int rightIndex = Integer.max(indexA, indexB);

    if (leftIndex != rightIndex) {
      return before;
    }

    var modifiedBounds = new ArrayList<>(before.bounds());
    var newLeftBound = modifiedBounds.get(leftIndex).difference(Set.of(a));
    var newRightBound = modifiedBounds.get(rightIndex).difference(Set.of(b));

    modifiedBounds.set(leftIndex, newLeftBound);
    modifiedBounds.set(rightIndex, newRightBound);

    return new FunArray<>(modifiedBounds, before.values(), before.emptiness()).removeEmptyBounds();
  }

  @Override
  public <ElementT extends DomainValue<ElementT>> TriBool isSatisfied(FunArray<ElementT> array, NormalExpression a, NormalExpression b) {
    int indexA;
    int indexB;

    try {
      indexA = array.findIndex(a);
      indexB = array.findIndex(b);
    } catch (IndexOutOfBoundsException e) {
      return UNKNOWN;
    }

    int leftIndex = Integer.min(indexA, indexB);
    int rightIndex = Integer.max(indexA, indexB);

    if (leftIndex == rightIndex) {
      return FALSE;
    }
    if (array.emptiness().subList(leftIndex, rightIndex).stream().allMatch(e -> e)) {
      return UNKNOWN;
    }
    return TRUE;
  }
}
