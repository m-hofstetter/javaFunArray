package abstractdomain.sign.value;

import static abstractdomain.sign.value.Sign.SignElement.NEGATIVE;
import static abstractdomain.sign.value.Sign.SignElement.POSITIVE;
import static abstractdomain.sign.value.Sign.SignElement.ZERO;

import abstractdomain.DomainValue;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The sign abstract domain.
 *
 * @param elements its elements.
 */
public record Sign(Set<SignElement> elements) implements DomainValue<Sign> {

  public Sign(Set<SignElement> elements) {
    this.elements = Set.copyOf(elements);
  }

  public static Sign of(int value) {
    if (value == 0) {
      return new Sign(Set.of(ZERO));
    } else if (value >= 1) {
      return new Sign(Set.of(POSITIVE));
    } else {
      return new Sign(Set.of(NEGATIVE));
    }
  }

  /**
   * The atomic values of the sign domain.
   */
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
    return add(other.negate());
  }

  @Override
  public Sign addConstant(int constant) {
    SignElement constantSign;
    if (constant == 0) {
      constantSign = SignElement.ZERO;
    } else if (constant < 0) {
      constantSign = NEGATIVE;
    } else {
      constantSign = POSITIVE;
    }
    return add(new Sign(Set.of(constantSign)));
  }

  @Override
  public Sign subtractConstant(int constant) {
    return addConstant(-constant);
  }

  @Override
  public Sign multiply(Sign other) {
    return null; //TODO
  }

  @Override
  public Sign multiplyByConstant(int constant) {
    return null;//TODO
  }

  @Override
  public Sign divide(Sign other) {
    return null;//TODO
  }

  @Override
  public Sign divideByConstant(int constant) {
    return null;//TODO
  }

  @Override
  public Sign modulo(Sign other) {
    return null;//TODO
  }

  @Override
  public Sign modulo(int constant) {
    return null;//TODO
  }

  @Override
  public Sign absoluteValue() {
    return null; //TODO
  }

  @Override
  public Sign negate() {
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

  @Override
  public Sign satisfyLessEqualThan(Sign other) {
    var modifiedSet = new HashSet<>(elements);
    if (!other.elements.contains(POSITIVE)) {
      modifiedSet.remove(POSITIVE);
      if (!other.elements.contains(ZERO)) {
        modifiedSet.remove(ZERO);
        if (!other.elements.contains(NEGATIVE)) {
          modifiedSet.remove(NEGATIVE);
        }
      }
    }
    return new Sign(modifiedSet);
  }

  @Override
  public Sign satisfyGreaterEqualThan(Sign other) {
    var modifiedSet = new HashSet<>(elements);
    if (!other.elements.contains(NEGATIVE)) {
      modifiedSet.remove(NEGATIVE);
      if (!other.elements.contains(ZERO)) {
        modifiedSet.remove(ZERO);
        if (!other.elements.contains(POSITIVE)) {
          modifiedSet.remove(POSITIVE);
        }
      }
    }
    return new Sign(modifiedSet);
  }

  @Override
  public Sign satisfyLessThan(Sign other) {
    var modifiedSet = new HashSet<>(elements);
    if (other.elements.contains(POSITIVE)) {
      modifiedSet.remove(POSITIVE);
    }
    if (other.elements.contains(ZERO)) {
      modifiedSet.remove(ZERO);
      modifiedSet.remove(POSITIVE);
    }
    if (other.elements.contains(NEGATIVE)) {
      modifiedSet.remove(NEGATIVE);
      modifiedSet.remove(ZERO);
      modifiedSet.remove(POSITIVE);
    }
    if (other.elements.isEmpty()) {
      modifiedSet.remove(NEGATIVE);
      modifiedSet.remove(ZERO);
      modifiedSet.remove(POSITIVE);
    }
    return new Sign(modifiedSet);
  }

  @Override
  public Sign satisfyGreaterThan(Sign other) {
    var modifiedSet = new HashSet<>(elements);
    if (other.elements.contains(NEGATIVE)) {
      modifiedSet.remove(NEGATIVE);
    }
    if (other.elements.contains(ZERO)) {
      modifiedSet.remove(ZERO);
      modifiedSet.remove(NEGATIVE);
    }
    if (other.elements.contains(POSITIVE)) {
      modifiedSet.remove(NEGATIVE);
      modifiedSet.remove(ZERO);
      modifiedSet.remove(POSITIVE);
    }
    if (other.elements.isEmpty()) {
      modifiedSet.remove(NEGATIVE);
      modifiedSet.remove(ZERO);
      modifiedSet.remove(POSITIVE);
    }
    return new Sign(modifiedSet);
  }

  @Override
  public Sign satisfyEqual(Sign other) {
    return new Sign(other.elements);
  }

  @Override
  public Sign satisfyNotEqual(Sign other) {
    return new Sign(Set.of(NEGATIVE, ZERO, POSITIVE).stream()
            .filter(e -> !other.elements.contains(e))
            .collect(Collectors.toSet()));
  }
}
