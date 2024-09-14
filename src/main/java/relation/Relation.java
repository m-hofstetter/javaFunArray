package relation;

import abstractdomain.DomainValue;
import abstractdomain.TriBool;
import funarray.FunArray;
import funarray.NormalExpression;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;

public abstract class Relation<T extends DomainValue<T>> {

  private final BinaryOperator<T> satisfactionOperator;
  private final BiFunction<T, T, TriBool> satisfactionPredicate;
  private final Supplier<Relation<T>> inverseSupplier;
  private final Supplier<Relation<T>> complementSupplier;

  protected Relation(
          BinaryOperator<T> satisfactionOperator,
          BiFunction<T, T, TriBool> satisfactionPredicate,
          Supplier<Relation<T>> inverseSupplier,
          Supplier<Relation<T>> complementSupplier
  ) {
    this.satisfactionOperator = satisfactionOperator;
    this.satisfactionPredicate = satisfactionPredicate;
    this.inverseSupplier = inverseSupplier;
    this.complementSupplier = complementSupplier;
  }

  public T satisfy(T comparandum, T comparand) {
    return satisfactionOperator.apply(comparandum, comparand);
  }

  public abstract <ElementT extends DomainValue<ElementT>> FunArray<ElementT> satisfy(FunArray<ElementT> before, NormalExpression left, NormalExpression right);

  public TriBool isSatisfied(T comparandum, T comparand) {
    return satisfactionPredicate.apply(comparandum, comparand);
  }

  public abstract <ElementT extends DomainValue<ElementT>> TriBool isSatisfied(FunArray<ElementT> array, NormalExpression left, NormalExpression right);

  public Relation<T> inverseOrder() {
    return inverseSupplier.get();
  }

  public Relation<T> complementaryOrder() {
    return complementSupplier.get();
  }
}
