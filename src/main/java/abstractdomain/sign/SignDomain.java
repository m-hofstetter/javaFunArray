package abstractdomain.sign;

import static abstractdomain.sign.value.Sign.SignElement.NEGATIVE;
import static abstractdomain.sign.value.Sign.SignElement.POSITIVE;
import static abstractdomain.sign.value.Sign.SignElement.ZERO;

import abstractdomain.Domain;
import abstractdomain.exception.ConcretizationException;
import abstractdomain.sign.value.Sign;
import java.util.Set;

public class SignDomain implements Domain<Sign> {

  public static final SignDomain INSTANCE = new SignDomain();

  private SignDomain() {
  }

  @Override
  public Sign abstract_(int concreteValue) {
    return Sign.of(concreteValue);
  }

  @Override
  public int concretize(Sign sign) throws ConcretizationException {
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
}
