package abstractdomain.interval.value;

import static abstractdomain.TriBool.FALSE;
import static abstractdomain.TriBool.TRUE;
import static abstractdomain.TriBool.UNKNOWN;

import abstractdomain.TriBool;
import base.infint.InfInt;
import lombok.Getter;

/**
 * An {@link Interval}, that is not unreachable.
 */
@Getter
public final class ReachableInterval extends Interval {

  private final InfInt lowerLimit;
  private final InfInt upperLimit;

  public ReachableInterval(InfInt lowerLimit, InfInt upperLimit) {
    this.lowerLimit = lowerLimit;
    this.upperLimit = upperLimit;
  }

  @Override
  public Interval join(Interval other) {
    if (other instanceof ReachableInterval reachableOther) {
      var lower = InfInt.min(this.lowerLimit, reachableOther.lowerLimit);
      var upper = InfInt.max(this.upperLimit, reachableOther.upperLimit);
      return new ReachableInterval(lower, upper);
    }
    return this;
  }


  @Override
  public Interval meet(Interval other) {
    if (other instanceof ReachableInterval reachableOther) {
      var lower = InfInt.max(this.lowerLimit, reachableOther.lowerLimit);
      var upper = InfInt.min(this.upperLimit, reachableOther.upperLimit);

      if (!lower.isGreaterThan(upper)) {
        return new ReachableInterval(lower, upper);
      }
    }

    return new Unreachable();
  }

  /**
   * The abstract widening operator. See: Cousot, P., Cousot, R. (1992). Comparing the Galois
   * connection and widening/narrowing approaches to abstract interpretation. In: Bruynooghe, M.,
   * Wirsing, M. (eds) Programming Language Implementation and Logic Programming. PLILP 1992.
   * Lecture Notes in Computer Science, vol 631. Springer, Berlin, Heidelberg. <a
   * href="https://doi.org/10.1007/3-540-55844-6_142">https://doi.org/10.1007/3-540-55844-6_142</a>.
   *
   * @param other the interval to widen this with.
   * @return the widened interval.
   */
  @Override
  public Interval widen(Interval other) {
    if (other instanceof ReachableInterval reachableOther) {
      var lower = this.lowerLimit;
      var upper = this.upperLimit;

      if (reachableOther.lowerLimit.isLessThan(this.lowerLimit)) {
        lower = InfInt.negInf();
      }
      if (reachableOther.upperLimit.isGreaterThan(this.upperLimit)) {
        upper = InfInt.posInf();
      }
      return new ReachableInterval(lower, upper);
    }
    return this;
  }

  /**
   * The abstract narrowing operator. See: Cousot, P., Cousot, R. (1992). Comparing the Galois
   * connection and widening/narrowing approaches to abstract interpretation. In: Bruynooghe, M.,
   * Wirsing, M. (eds) Programming Language Implementation and Logic Programming. PLILP 1992.
   * Lecture Notes in Computer Science, vol 631. Springer, Berlin, Heidelberg. <a
   * href="https://doi.org/10.1007/3-540-55844-6_142">https://doi.org/10.1007/3-540-55844-6_142</a>.
   *
   * @param other the interval to narrow this with.
   * @return the narrowed interval.
   */
  @Override
  public Interval narrow(Interval other) {
    if (other instanceof ReachableInterval reachableOther) {
      var lower = this.lowerLimit;
      var upper = this.upperLimit;
      if (this.lowerLimit.isNegInf()) {
        lower = reachableOther.lowerLimit;
      }
      if (this.upperLimit.isPosInf()) {
        upper = reachableOther.upperLimit;
      }
      return new ReachableInterval(lower, upper);
    }
    return new Unreachable();
  }

  @Override
  public String toString() {
    return "[%s, %s]".formatted(lowerLimit, upperLimit);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof ReachableInterval reachableOther) {
      return this.lowerLimit.equals(reachableOther.lowerLimit)
              && this.upperLimit.equals(reachableOther.upperLimit);
    }
    return false;
  }

  @Override
  public Interval add(Interval value) {
    if (value instanceof ReachableInterval reachableValue) {
      return new ReachableInterval(
              lowerLimit.add(reachableValue.lowerLimit),
              upperLimit.add(reachableValue.upperLimit)
      );
    }
    return new Unreachable();
  }

  @Override
  public ReachableInterval negate() {
    return new ReachableInterval(upperLimit.negate(), lowerLimit.negate());
  }

  @Override
  public Interval subtract(Interval value) {
    return add(value.negate());
  }

  @Override
  public Interval addConstant(long constant) {
    return add(of(constant, constant));
  }

  @Override
  public Interval subtractConstant(long constant) {
    return addConstant(-constant);
  }

  @Override
  public Interval multiply(Interval other) {
    if (other instanceof ReachableInterval reachableOther) {
      var lowerBound = InfInt.min(
              this.lowerLimit.multiply(reachableOther.lowerLimit),
              this.lowerLimit.multiply(reachableOther.upperLimit),
              this.upperLimit.multiply(reachableOther.lowerLimit),
              this.upperLimit.multiply(reachableOther.upperLimit)
      );
      var upperBound = InfInt.max(
              this.lowerLimit.multiply(reachableOther.lowerLimit),
              this.lowerLimit.multiply(reachableOther.upperLimit),
              this.upperLimit.multiply(reachableOther.lowerLimit),
              this.upperLimit.multiply(reachableOther.upperLimit)
      );
      return new ReachableInterval(lowerBound, upperBound);
    }
    return new Unreachable();
  }

  @Override
  public Interval multiplyByConstant(long constant) {
    return new ReachableInterval(this.lowerLimit.multiply(constant), this.upperLimit.multiply(constant));
  }

  @Override
  public Interval divide(Interval other) {
    if (other instanceof ReachableInterval reachableOther) {
      var otherLower = reachableOther.lowerLimit;
      var otherUpper = reachableOther.upperLimit;

      if (otherLower.equals(InfInt.of(0)) && otherUpper.equals(InfInt.of(0))) {
        throw new ArithmeticException("Cannot divide by zero interval.");
      }
      otherLower = otherLower.equals(InfInt.of(0)) ? InfInt.of(1) : otherLower;
      otherUpper = otherUpper.equals(InfInt.of(0)) ? InfInt.of(-1) : otherUpper;

      var lower = InfInt.min(
              this.lowerLimit.divide(otherLower),
              this.lowerLimit.divide(otherUpper),
              this.upperLimit.divide(otherLower),
              this.upperLimit.divide(otherUpper)
      );

      var upper = InfInt.max(
              this.lowerLimit.divide(otherLower),
              this.lowerLimit.divide(otherUpper),
              this.upperLimit.divide(otherLower),
              this.upperLimit.divide(otherUpper)
      );
      return new ReachableInterval(lower, upper);
    }
    return new Unreachable();
  }

  @Override
  public Interval divideByConstant(long constant) {
    if (constant == 0) {
      throw new ArithmeticException("Cannot divide by zero.");
    }
    return new ReachableInterval(this.lowerLimit.divide(constant), this.upperLimit.divide(constant));
  }


  /**
   * To simplify implementation, modulo of intervals is implemented
   */
  @Override
  public Interval modulo(Interval other) {
    if (other instanceof ReachableInterval reachableOther) {
      var divisor = reachableOther.absoluteValue();
      divisor = new ReachableInterval(divisor.lowerLimit, divisor.upperLimit.subtract(1));

      if (this.lowerLimit.isGreaterEqualThan(InfInt.of(0))) {
        return new ReachableInterval(InfInt.of(0), InfInt.min(this.upperLimit, divisor.upperLimit));
      }

      if (this.upperLimit.isLessThan(InfInt.of(0))) {
        return new ReachableInterval(InfInt.max(this.lowerLimit, divisor.negate().lowerLimit), InfInt.of(0));
      }

      return new ReachableInterval(InfInt.max(this.lowerLimit, divisor.negate().lowerLimit), InfInt.min(this.upperLimit, divisor.upperLimit));
    }
    return unreachable();
  }

  @Override
  public Interval modulo(long constant) {
    return new ReachableInterval(this.lowerLimit.modulo(constant), this.upperLimit.modulo(constant));
  }

  @Override
  public ReachableInterval absoluteValue() {
    if (this.lowerLimit.isLessEqualThan(InfInt.of(0)) && this.upperLimit.isGreaterEqualThan(InfInt.of(0))) {
      return new ReachableInterval(InfInt.of(0), InfInt.max(this.lowerLimit.negate(), this.upperLimit));
    }
    if (this.lowerLimit.isLessEqualThan(InfInt.of(0)) && this.upperLimit.isLessEqualThan(InfInt.of(0))) {
      return new ReachableInterval(this.upperLimit.negate(), this.lowerLimit.negate());
    }
    return this;
  }

  @Override
  public Interval satisfyLessEqualThan(Interval other) {
    if (other instanceof ReachableInterval reachableOther) {
      return this.meet(of(InfInt.negInf(), reachableOther.upperLimit));
    }
    return new Unreachable();
  }

  @Override
  public Interval satisfyGreaterEqualThan(Interval other) {
    if (other instanceof ReachableInterval reachableOther) {
      return this.meet(of(reachableOther.lowerLimit, InfInt.posInf()));
    }
    return new Unreachable();
  }

  @Override
  public Interval satisfyLessThan(Interval other) {
    if (other instanceof ReachableInterval reachableOther) {
      return this.meet(of(InfInt.negInf(), reachableOther.upperLimit.subtract(1)));
    }
    return new Unreachable();
  }

  @Override
  public Interval satisfyGreaterThan(Interval other) {
    if (other instanceof ReachableInterval reachableOther) {
      return this.meet(of(reachableOther.lowerLimit.add(1), InfInt.posInf()));
    }
    return new Unreachable();
  }

  @Override
  public Interval satisfyEqual(Interval other) {
    return this.meet(other);
  }

  @Override
  public Interval satisfyNotEqual(Interval other) {
    if (other instanceof ReachableInterval reachableOther) {
      if (this.lowerLimit.isLessThan(reachableOther.lowerLimit)) {
        if (this.upperLimit.isGreaterThan(reachableOther.upperLimit)) {
          return new ReachableInterval(this.lowerLimit, this.upperLimit);
        } else {
          return new ReachableInterval(
                  this.lowerLimit,
                  InfInt.min(this.upperLimit, reachableOther.lowerLimit.subtract(1))
          );
        }
      } else {
        if (this.upperLimit.isGreaterThan(reachableOther.upperLimit)) {
          return new ReachableInterval(
                  InfInt.max(reachableOther.upperLimit.add(1), this.lowerLimit),
                  this.upperLimit
          );
        }
      }
    }
    return new Unreachable();
  }

  @Override
  public TriBool greaterThan(Interval other) {
    return switch (other) {
      case ReachableInterval reachable -> {
        if (this.lowerLimit.isGreaterThan(reachable.getLowerLimit())) {
          if (this.lowerLimit.isGreaterThan(reachable.getUpperLimit())) {
            yield TRUE;
          }
          yield UNKNOWN;
        }
        yield FALSE;
      }
      case Unreachable _ -> UNKNOWN;
    };
  }

  @Override
  public TriBool lessThan(Interval other) {
    return switch (other) {
      case ReachableInterval reachable -> {
        if (this.upperLimit.isLessThan(reachable.getUpperLimit())) {
          if (this.upperLimit.isLessThan(reachable.getLowerLimit())) {
            yield TRUE;
          }
          yield UNKNOWN;
        }
        yield FALSE;
      }
      case Unreachable _ -> UNKNOWN;
    };
  }

  @Override
  public TriBool greaterEqualThan(Interval other) {
    return switch (other) {
      case ReachableInterval reachable -> {
        if (this.lowerLimit.isGreaterEqualThan(reachable.getLowerLimit())) {
          if (this.lowerLimit.isGreaterEqualThan(reachable.upperLimit)) {
            yield TRUE;
          }
          yield UNKNOWN;
        }
        yield FALSE;
      }
      case Unreachable _ -> UNKNOWN;
    };
  }

  @Override
  public TriBool lessEqualThan(Interval other) {
    return switch (other) {
      case ReachableInterval reachable -> {
        if (this.upperLimit.isLessEqualThan(reachable.getUpperLimit())) {
          if (this.upperLimit.isLessEqualThan(reachable.getLowerLimit())) {
            yield TRUE;
          }
          yield UNKNOWN;
        }
        yield FALSE;
      }
      case Unreachable _ -> UNKNOWN;
    };
  }

  @Override
  public TriBool equal(Interval other) {
    return switch (other) {
      case ReachableInterval reachable -> {
        if (this.lowerLimit.equals(reachable.getLowerLimit())
                && this.upperLimit.equals(reachable.getUpperLimit())
                && this.lowerLimit.equals(this.upperLimit)) {
          yield TRUE;
        }
        if (this.upperLimit.isLessThan(reachable.lowerLimit) || this.lowerLimit.isGreaterThan(reachable.upperLimit)) {
          yield FALSE;
        }
        yield UNKNOWN;
      }
      case Unreachable _ -> UNKNOWN;
    };
  }

  @Override
  public TriBool notEqual(Interval other) {
    return switch (other) {
      case ReachableInterval reachable -> {
        if (this.lowerLimit.equals(reachable.getLowerLimit())
                && this.upperLimit.equals(reachable.getUpperLimit())
                && this.lowerLimit.equals(this.upperLimit)) {
          yield FALSE;
        }
        if (this.upperLimit.isLessThan(reachable.lowerLimit) || this.lowerLimit.isGreaterThan(reachable.upperLimit)) {
          yield TRUE;
        }
        yield UNKNOWN;
      }
      case Unreachable _ -> UNKNOWN;
    };
  }

  @Override
  public boolean isReachable() {
    return true;
  }
}
