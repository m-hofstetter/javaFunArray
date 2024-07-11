package base.sign;

import static base.sign.Sign.SignElement.*;

import base.DomainValue;
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
}
