package base.sign;

import static base.sign.Sign.SignElement.*;

import base.DomainValue;
import exception.DomainException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Sign implements DomainValue {

  final Set<SignElement> elements;

  public Sign(Set<SignElement> elements) {
    this.elements = Set.copyOf(elements);
  }

  public enum SignElement {
    POSITIVE, NEGATIVE, ZERO
  }

  @Override
  public DomainValue join(DomainValue other) {
    if (other instanceof Sign otherSign) {
      var joinedSet = new HashSet<>(elements);
      joinedSet.addAll(otherSign.elements);
      return new Sign(joinedSet);
    }
    throw new DomainException("Cannot join domain values from different domains.");
  }

  @Override
  public DomainValue meet(DomainValue other) {
    if (other instanceof Sign otherSign) {
      var metSet = elements.stream()
              .filter(otherSign.elements::contains)
              .collect(Collectors.toSet());
      return new Sign(metSet);
    }
    throw new DomainException("Cannot meet domain values from different domains.");
  }

  @Override
  public DomainValue widen(DomainValue other) {
    // Finite lattice converges towards a fixpoint by itself. No widening necessary.
    return other;
  }

  @Override
  public DomainValue narrow(DomainValue other) {
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
