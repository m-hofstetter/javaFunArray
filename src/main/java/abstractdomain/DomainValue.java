package abstractdomain;

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
   * The abstract multiplication transformation.
   *
   * @param other another domain value.
   * @return the product of both.
   */
  T multiply(T other);

  /**
   * The abstract multiplication transformation.
   *
   * @param constant an integer.
   * @return the transformed domain value.
   */
  T multiplyByConstant(int constant);

  /**
   * The abstract division transformation.
   *
   * @param other another domain value.
   * @return the division of both.
   */
  T divide(T other);

  /**
   * The abstract division transformation.
   *
   * @param constant an integer.
   * @return the transformed domain value.
   */
  T divideByConstant(int constant);

  /**
   * The abstract modulo transformation.
   *
   * @param other another domain value.
   * @return the modulo of both.
   */
  T modulo(T other);

  /**
   * The abstract modulo transformation.
   *
   * @param constant an integer.
   * @return the modulo.
   */
  T modulo(int constant);

  /**
   * The absolute value transformation.
   *
   * @return the absolute value.
   */
  T absoluteValue();

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

  boolean greaterThan(T other);

  boolean lessThan(T other);

  boolean greaterEqualThan(T other);

  boolean lessEqualThan(T other);

  boolean equal(T other);

  boolean notEqual(T other);

  boolean isReachable();
}
