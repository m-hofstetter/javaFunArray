package abstractdomain;

public interface ValueRelation<T extends DomainValue<T>> {

  T satisfy(T comparandum, T comparand);

  TriBool isSatisfied(T comparandum, T comparand);

  ValueRelation<T> inverseOrder();

  ValueRelation<T> complementaryOrder();
}