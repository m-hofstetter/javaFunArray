package abstractdomain.sign;

import static abstractdomain.sign.value.Sign.SignElement.NEGATIVE;
import static abstractdomain.sign.value.Sign.SignElement.POSITIVE;
import static abstractdomain.sign.value.Sign.SignElement.ZERO;

import abstractdomain.Domain;
import abstractdomain.ValueRelation;
import abstractdomain.exception.ConcretizationException;
import abstractdomain.sign.value.Sign;
import java.util.Set;

public class SignDomain implements Domain<Sign> {

  public static final SignDomain INSTANCE = new SignDomain();

  private SignDomain() {
  }

  @Override
  public Sign abstract_(long concreteValue) {
    return Sign.of(concreteValue);
  }

  @Override
  public long concretize(Sign sign) throws ConcretizationException {
    if (sign.equals(Sign.of(0))) {
      return 0;
    }
    throw new ConcretizationException(sign);
  }

  @Override
  public Sign getUnknown() {
    return new Sign(Set.of(NEGATIVE, ZERO, POSITIVE));
  }

  @Override
  public Sign getUnreachable() {
    return new Sign(Set.of());
  }

  @Override
  public Sign getZeroValue() {
    return new Sign(Set.of(ZERO));
  }

  @Override
  public ValueRelation<Sign> lessThan() {
    return null; //TODO
  }

  @Override
  public ValueRelation<Sign> lessEqualThan() {
    return null; //TODO
  }

  @Override
  public ValueRelation<Sign> greaterThan() {
    return null; //TODO
  }

  @Override
  public ValueRelation<Sign> greaterEqualThan() {
    return null; //TODO
  }

  @Override
  public ValueRelation<Sign> equalTo() {
    return null; //TODO
  }

  @Override
  public ValueRelation<Sign> unequalTo() {
    return null; //TODO
  }
}
