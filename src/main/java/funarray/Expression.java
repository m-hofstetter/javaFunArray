package funarray;

import static base.TriBoolean.*;

import base.TriBoolean;
import base.infint.InfInt;

/**
 * An expression in normal form v + k. See Patrick Cousot, Radhia Cousot, and Francesco Logozzo.
 * 2011. A parametric segmentation functor for fully automatic and scalable array content analysis.
 * SIGPLAN Not. 46, 1 (January 2011), 105–118. <a
 * href="https://doi.org/10.1145/1925844.1926399">https://doi.org/10.1145/1925844.1926399</a>
 *
 * @param variable the variable v
 * @param constant the constant k
 */
public record Expression(Variable variable, InfInt constant) {

  public Expression(int constant) {
    this(Variable.ZERO_VALUE, InfInt.of(constant));
  }

  @Override
  public String toString() {
    if (constant.equals(InfInt.of(0))) {
      return variable.toString();
    }
    if (variable == Variable.ZERO_VALUE) {
      return constant().toString();
    }
    if (constant.isLessThan(InfInt.of(0))) {
      return "%s%s".formatted(variable, constant);
    }
    return "%s+%s".formatted(variable, constant);
  }

  public boolean equals(Expression other) {
    return this.variable.equals(other.variable()) && this.constant.equals(other.constant());
  }

  /**
   * If a variable v changes reversibly to f(v), all occurrences of v in an FunArray have to be
   * replaced by f^-1(v), so that it still represents the Array correctly. See Cousot et al. 2011.
   * For adding to a variable v by an amount i, this means replacing alle occurrences of v by v-1.
   *
   * @param variable the variable v.
   * @param value    the value by which it is being increased.
   * @return the altered variable.
   */
  public Expression addToVariableInFunArray(Variable variable, InfInt value) {
    if (this.variable().equals(variable)) {
      return new Expression(variable, constant.subtract(value));
    }
    return this;
  }

  public Expression increase(InfInt value) {
    return new Expression(variable, constant.add(value));
  }

  /**
   * Syntactically compares two expressions.
   *
   * @param other the expression to compare this to.
   * @return TRUE if this is definitely less than the other, FALSE if this is definitely not less
   *         than the other and UNKNOWN if it can't be determined
   */
  public TriBoolean isLessThan(Expression other) {
    if (!this.variable().equals(other.variable())) {
      return UNKNOWN;
    }
    return this.constant().isLessThan(other.constant()) ? TRUE : FALSE;
  }

  public TriBoolean isLessEqualThan(Expression other) {
    return isGreaterThan(other).invert();
  }

  /**
   * Syntactically compares two expressions.
   *
   * @param other the expression to compare this to.
   * @return TRUE if this is definitely greater than the other, FALSE if this is definitely not
   *         greater than the other and UNKNOWN if it can't be determined
   */
  public TriBoolean isGreaterThan(Expression other) {
    if (!this.variable().equals(other.variable())) {
      return UNKNOWN;
    }
    return this.constant().isGreaterThan(other.constant()) ? TRUE : FALSE;
  }

  public TriBoolean isGreaterEqualThan(Expression other) {
    return isLessThan(other).invert();
  }
}
