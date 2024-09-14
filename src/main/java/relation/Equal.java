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

public class Equal<T extends DomainValue<T>> extends Relation<T> {

  public Equal() {
    super(T::satisfyEqual, T::equal, Equal::new, NotEqual::new);
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
    if (indexA == indexB) {
      return before;
    }

    int leftIndex = Integer.min(indexA, indexB);
    int rightIndex = Integer.max(indexA, indexB);


    var modifiedBounds = new ArrayList<>(before.bounds());
    var modifiedValues = new ArrayList<>(before.values());
    var modifiedEmptiness = new ArrayList<>(before.emptiness());

    var boundsToBeSquashed = modifiedBounds.subList(leftIndex, rightIndex + 1);
    var squashedBound = Bound.union(boundsToBeSquashed);
    boundsToBeSquashed.clear();
    boundsToBeSquashed.add(squashedBound);

    modifiedValues.subList(leftIndex, rightIndex).clear();
    modifiedEmptiness.subList(leftIndex, rightIndex).clear();

    return new FunArray<>(modifiedBounds, modifiedValues, modifiedEmptiness);
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
      return TRUE;
    }
    if (array.emptiness().subList(leftIndex, rightIndex).stream().allMatch(e -> e)) {
      return UNKNOWN;
    }
    return FALSE;
  }
}
