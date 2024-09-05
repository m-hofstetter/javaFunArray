package funarray;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A segment bound in a {@link FunArray}.
 *
 * @param expressions the expressions contained within.
 */
public record Bound(Set<NormalExpression> expressions) {
  public Bound {
    expressions = Set.copyOf(expressions);
  }

  public Bound(NormalExpression expression) {
    this(Set.of(expression));
  }


  public Bound adaptForChangedVariableValue(
          String changedVariableRef,
          NormalExpression newValue
  ) {
    var modifiedExpressions = expressions.stream().flatMap(
            expression -> {
              if (expression.containsVariable(newValue.varRef())) {
                if (expression.containsVariable(changedVariableRef)) {
                  return Stream.of(expression.increase(-newValue.constant()));
                } else {
                  return Stream.of(
                          expression,
                          new NormalExpression(changedVariableRef, expression.constant() - newValue.constant())
                  );
                }
              } else {
                if (expression.containsVariable(changedVariableRef)) {
                  return Stream.of();
                } else {
                  return Stream.of(expression);
                }
              }
            }
    ).collect(Collectors.toSet());

    return new Bound(modifiedExpressions);
  }

  public Bound adaptForChangedVariableValues(
          String changedVariableRef,
          Set<NormalExpression> newValues
  ) {
    var modifiedExpressions = expressions.stream().flatMap(
            expression -> newValues.stream().flatMap(newValue -> {
              if (expression.containsVariable(newValue.varRef())) {
                if (expression.containsVariable(changedVariableRef)) {
                  return Stream.of(expression.increase(-newValue.constant()));
                } else {
                  return Stream.of(
                          expression,
                          new NormalExpression(changedVariableRef, expression.constant() - newValue.constant())
                  );
                }
              } else {
                if (expression.containsVariable(changedVariableRef)) {
                  return Stream.of();
                } else {
                  return Stream.of(expression);
                }
              }
            })
    ).collect(Collectors.toSet());

    return new Bound(modifiedExpressions);
  }

  /**
   * Removes all expressions containing the specified variable.
   *
   * @param varRef the variable.
   * @return the modified bound.
   */
  public Bound removeVariableOccurrences(String varRef) {
    var modifiedExpressions = expressions.stream()
            .filter(e -> !e.containsVariable(varRef))
            .collect(Collectors.toSet());
    return new Bound(modifiedExpressions);
  }

  public boolean contains(NormalExpression expression) {
    return expressions().stream().anyMatch(e -> e.equals(expression));
  }

  public boolean contains(Predicate<NormalExpression> predicate) {
    return expressions().stream().anyMatch(predicate);
  }

  public boolean isEmpty() {
    return expressions.isEmpty();
  }

  public Bound increase(int amount) {
    return new Bound(expressions.stream()
            .map(e -> e.increase(amount))
            .collect(Collectors.toSet()));
  }

  public static Bound union(Collection<Bound> bounds) {
    return new Bound(
            bounds.stream()
                    .flatMap(e -> e.expressions().stream())
                    .collect(Collectors.toSet())
    );
  }

  public Bound union(Bound other) {
    return union(other.expressions);
  }

  public Bound union(Set<NormalExpression> otherExpressions) {
    var newExpressions = Stream.concat(
            this.expressions.stream(),
            otherExpressions.stream()
    ).collect(Collectors.toSet());
    return new Bound(newExpressions);
  }

  public Bound intersection(Bound other) {
    return intersection(other.expressions);
  }

  public Bound intersection(Set<NormalExpression> otherExpressions) {
    var newExpressions = this.expressions.stream()
            .filter(otherExpressions::contains)
            .collect(Collectors.toSet());
    return new Bound(newExpressions);
  }

  public Bound difference(Bound other) {
    return difference(other.expressions);
  }

  public Bound difference(Set<NormalExpression> otherExpressions) {
    var newExpressions = this.expressions.stream()
            .filter(o -> !otherExpressions.contains(o))
            .collect(Collectors.toSet());
    return new Bound(newExpressions);
  }

  public Bound relativeComplement(Bound other) {
    return relativeComplement(other.expressions);
  }

  public Bound relativeComplement(Set<NormalExpression> otherExpressions) {
    var newExpressions = otherExpressions.stream()
            .filter(o -> !this.expressions.contains(o))
            .collect(Collectors.toSet());
    return new Bound(newExpressions);
  }

  @Override
  public String toString() {
    return "{%s}".formatted(
            String.join(" ", expressions.stream().map(NormalExpression::toString).sorted().toList()));
  }
}
