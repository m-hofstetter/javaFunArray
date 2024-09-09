package abstractdomain;

public interface Relation<T extends DomainValue<T>> {

  T satisfy(T comparandum, T comparand);

  boolean isSatisfied(T comparandum, T comparand);

  Relation<T> inverseOrder();

  Relation<T> complementaryOrder();
}