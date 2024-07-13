package base.sign;

import static base.sign.Sign.SignElement.*;

import base.DomainValue;
import base.infint.InfInt;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Sign implements DomainValue<Sign> {

  final Set<SignElement> elements;

  public Sign(Set<SignElement> elements) {
    this.elements = Set.copyOf(elements);
  }

  public enum SignElement {
    POSITIVE, NEGATIVE, ZERO
  }

  @Override
  public Sign join(Sign other) {
    var joinedSet = new HashSet<>(elements);
    joinedSet.addAll(other.elements);
    return new Sign(joinedSet);
  }

  @Override
  public Sign meet(Sign other) {
    var metSet = elements.stream()
            .filter(other.elements::contains)
            .collect(Collectors.toSet());
    return new Sign(metSet);
  }

  @Override
  public Sign widen(Sign other) {
    // Finite lattice converges towards a fixpoint by itself. No widening necessary.
    return other;
  }

  @Override
  public Sign narrow(Sign other) {
    // Finite lattice converges towards a fixpoint by itself. No narrowing necessary.
    return other;
  }

  @Override
  public String toString() {
    if (elements.equals(Set.of())) {
      return "⊥";
    }
    if (elements.equals(Set.of(POSITIVE))) {
      return ">0";
    }
    if (elements.equals(Set.of(NEGATIVE))) {
      return "<0";
    }
    if (elements.equals(Set.of(ZERO))) {
      return "0";
    }
    if (elements.equals(Set.of(POSITIVE, NEGATIVE))) {
      return "≠0";
    }
    if (elements.equals(Set.of(POSITIVE, ZERO))) {
      return "≥0";
    }
    if (elements.equals(Set.of(NEGATIVE, ZERO))) {
      return "≤0";
    }
    if (elements.equals(Set.of(POSITIVE, NEGATIVE, ZERO))) {
      return "⊤";
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Sign otherSign) {
      return elements.equals(otherSign.elements);
    }
    return false;
  }

  @Override
  public Sign add(Sign other) {
    if (elements.isEmpty() || other.elements.isEmpty()) {
      return new Sign(Set.of());
    }
    var modifiedSet = new HashSet<>(elements);
    if (elements.contains(NEGATIVE)) {
      if (other.elements.contains(POSITIVE)) {
        modifiedSet.add(ZERO);
        modifiedSet.add(POSITIVE);
      }
    }
    if (elements.contains(ZERO)) {
      if (other.elements.contains(POSITIVE)) {
        modifiedSet.add(POSITIVE);
        if (!other.elements.contains(ZERO)) {
          modifiedSet.remove(ZERO);
        }
      }
      if (other.elements.contains(NEGATIVE)) {
        modifiedSet.add(NEGATIVE);
        if (!other.elements.contains(ZERO)) {
          modifiedSet.remove(ZERO);
        }
      }
    }
    if (elements.contains(POSITIVE)) {
      if (other.elements.contains(NEGATIVE)) {
        modifiedSet.add(ZERO);
        modifiedSet.add(NEGATIVE);
      }
    }
    return new Sign(modifiedSet);
  }

  @Override
  public Sign subtract(Sign other) {
    return add(other.inverse());
  }

  @Override
  public Sign addConstant(InfInt constant) {
    SignElement constantSign;
    if (constant.equals(InfInt.of(0))) {
      constantSign = SignElement.ZERO;
    } else if (constant.isLessThan(InfInt.of(0))) {
      constantSign = NEGATIVE;
    } else {
      constantSign = POSITIVE;
    }
    return add(new Sign(Set.of(constantSign)));
  }

  @Override
  public Sign subtractConstant(InfInt constant) {
    return addConstant(constant.negate());
  }

  @Override
  public Sign inverse() {
    var modifiedSet = new HashSet<>(elements);
    if (elements.contains(NEGATIVE)) {
      modifiedSet.add(POSITIVE);
    } else {
      modifiedSet.remove(POSITIVE);
    }
    if (elements.contains(POSITIVE)) {
      modifiedSet.add(NEGATIVE);
    } else {
      modifiedSet.remove(NEGATIVE);
    }
    return new Sign(modifiedSet);
  }
}
