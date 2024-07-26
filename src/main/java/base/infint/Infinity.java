package base.infint;

/**
 * Abstract class for representing infinities in {@link InfInt}.
 */
public abstract sealed class Infinity extends InfInt permits NegativeInfinity, PositiveInfinity {
  @Override
  public InfInt add(InfInt value) {
    return this;
  }

  @Override
  public InfInt modulo(InfInt value) {
    throw new ArithmeticException("Cannot mod infinity");
  }
}
