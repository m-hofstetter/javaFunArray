package funarray;

/**
 * An expression in normal form v + k. See Patrick Cousot, Radhia Cousot, and Francesco Logozzo.
 * 2011. A parametric segmentation functor for fully automatic and scalable array content analysis.
 * SIGPLAN Not. 46, 1 (January 2011), 105–118. <a
 * href="https://doi.org/10.1145/1925844.1926399">https://doi.org/10.1145/1925844.1926399</a>
 *
 * @param varRef the reference for a variable v
 * @param constant the constant k
 */
public record NormalExpression(String varRef, int constant) {

  public NormalExpression(String variable) {
    this(variable, 0);
  }

  public NormalExpression increase(int value) {
    return new NormalExpression(varRef, constant + value);
  }

  /**
   * Syntactically compares two expressions.
   *
   * @param other the expression to compare this to.
   * @return TRUE if this is definitely less equal than the other, FALSE if this is definitely not less
   *         than the other and UNKNOWN if it can't be determined
   */
  public boolean isLessEqualThan(NormalExpression other) {
    if (!this.varRef.equals(other.varRef)) {
      return false;
    }
    return this.constant() <= other.constant();
  }

  /**
   * Syntactically compares two expressions.
   *
   * @param other the expression to compare this to.
   * @return TRUE if this is definitely greater equal than the other, FALSE if this is definitely not
   *         greater than the other and UNKNOWN if it can't be determined
   */
  public boolean isGreaterEqualThan(NormalExpression other) {
    if (!this.varRef.equals(other.varRef)) {
      return false;
    }
    return this.constant() >= other.constant();
  }

  public boolean containsVariable(String varRef) {
    return this.varRef.equals(varRef);
  }

  @Override
  public String toString() {
    if (constant == 0) {
      return varRef;
    }
    if (varRef.equals("0")) {
      return String.valueOf(constant);
    }
    return "%s%s%s".formatted(varRef, constant < 0 ? "" : "+", constant);
  }
}
