package funarray;

import base.IntegerWithInfinity;

/**
 * An expression in normal form v + k. See Patrick Cousot, Radhia Cousot, and Francesco Logozzo.
 * 2011. A parametric segmentation functor for fully automatic and scalable array content analysis.
 * SIGPLAN Not. 46, 1 (January 2011), 105–118. <a
 * href="https://doi.org/10.1145/1925844.1926399">https://doi.org/10.1145/1925844.1926399</a>
 *
 * @param variable the variable v
 * @param constant the constant k
 */
public record Expression(Variable variable, IntegerWithInfinity constant) {

  public static Expression getZero() {
    return new Expression(Variable.ZERO_VALUE, new IntegerWithInfinity(0));
  }

  @Override
  public String toString() {
    if (constant.equals(new IntegerWithInfinity(0))) {
      return variable.toString();
    }
    if (variable == Variable.ZERO_VALUE) {
      return constant().toString();
    }
    if (constant.isLessThan(new IntegerWithInfinity(0))) {
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
  public Expression addToVariableInFunArray(Variable variable, int value) {
    if (this.variable().equals(variable)) {
      return new Expression(variable, new IntegerWithInfinity(constant().value() - value));
    }
    return this;
  }
}
