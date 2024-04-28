package base.infint;

sealed abstract class Infinity extends InfInt permits NegativeInfinity, PositiveInfinity {
  @Override
  public InfInt add(InfInt value) {
    return this;
  }

}
