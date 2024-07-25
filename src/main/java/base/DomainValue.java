package base;

/**
 * An interface representing a value from an abstract domain.
 *
 * @param <T> the domain.
 */
public interface DomainValue<T extends DomainValue<T>> {

  /**
   * The abstract join operation.
   *
   * @param other another domain value.
   * @return the joined domain value.
   */
  T join(T other);

  /**
   * The abstract meet operation.
   *
   * @param other another domain value.
   * @return the intersection of both domain values.
   */
  T meet(T other);

  /**
   * The abstract widening operation.
   *
   * @param other another domain value.
   * @return the widened domain value.
   */
  T widen(T other);

  /**
   * The abstract narrowing operation.
   *
   * @param other another domain value.
   * @return the narrowed domain value.
   */
  T narrow(T other);

  /**
   * The abstract addition transformation.
   *
   * @param other another domain value.
   * @return the sum of both.
   */
  T add(T other);

  /**
   * The abstract subtraction transformation.
   *
   * @param other another domain value.
   * @return the difference of both.
   */
  T subtract(T other);

  /**
   * The abstract addition transformation.
   *
   * @param constant an integer.
   * @return the transformed domain value.
   */
  T addConstant(int constant);

  /**
   * The abstract subtraction transformation.
   *
   * @param constant an integer.
   * @return the transformed domain value.
   */
  T subtractConstant(int constant);

  /**
   * The abstract negation transformation. Inverses the sign in the concrete. Equal to
   * multiplication by -1.
   *
   * @return the transformed domain value.
   */
  T negate();

  /**
   * Satisfies the condition that this domain value is less equal than another.
   *
   * @param other the other domain value.
   * @return the domain value with the condition satisfied.
   */
  T satisfyLessEqualThan(T other);

  /**
   * Satisfies the condition that this domain value is greater equal than another.
   *
   * @param other the other domain value.
   * @return the domain value with the condition satisfied.
   */
  T satisfyGreaterEqualThan(T other);

  /**
   * Satisfies the condition that this domain value is less than another.
   *
   * @param other the other domain value.
   * @return the domain value with the condition satisfied.
   */
  T satisfyLessThan(T other);

  /**
   * Satisfies the condition that this domain value is greater than another.
   *
   * @param other the other domain value.
   * @return the domain value with the condition satisfied.
   */
  T satisfyGreaterThan(T other);

  /**
   * Satisfies the condition that this domain value is equal to another.
   *
   * @param other the other domain value.
   * @return the domain value with the condition satisfied.
   */
  T satisfyEqual(T other);

  /**
   * Satisfies the condition that this domain value is not equal to another.
   *
   * @param other the other domain value.
   * @return the domain value with the condition satisfied.
   */
  T satisfyNotEqual(T other);
}
