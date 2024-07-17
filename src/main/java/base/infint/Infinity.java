package base.infint;

public abstract sealed class Infinity extends InfInt permits NegativeInfinity, PositiveInfinity {
  @Override
  public InfInt add(InfInt value) {
    return this;
  }

}
