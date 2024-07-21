package funarray;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A segment bound in a {@link FunArray}.
 *
 * @param expressions the expressions contained within.
 */
public record Bound(Set<Expression> expressions) {
  public Bound {
    expressions = Set.copyOf(expressions);
  }

  public Bound(Expression expression) {
    this(Set.of(expression));
  }

  /**
   * If a variable v changes reversibly to f(v), all occurrences of v in an FunArray have to be
   * replaced by f^-1(v), so that it still represents the Array correctly. See Cousot et al. 2011.
   * For adding to a variable v by an amount i, this means replacing alle occurrences of v by v-1.
   *
   * @param varRef the variable v.
   * @param value    the value by which it is being increased.
   * @return the altered bound.
   */
  public Bound addToVariableInFunArray(String varRef, int value) {
    return new Bound(
            expressions.stream()
                    .map(e -> e.addToVariableInFunArray(varRef, value))
                    .collect(Collectors.toSet())
    );
  }

  /**
   * Inserts a new expression if a specified variable is present in its expressions. Is needed for
   * assigning a value to a variable that might be contained in a {@link FunArray}.
   *
   * @param varRef   the variable.
   * @param expression the expression
   * @return the modified bound.
   */
  public Bound insertExpressionIfVariablePresent(
          String varRef,
          Expression expression
  ) {
    var modifiedExpressions = new HashSet<>(removeVariableOccurrences(varRef).expressions);

    modifiedExpressions.stream()
            .filter(e -> e.containsVariable(expression.varRef()))
            .findAny()
            .ifPresent(e -> modifiedExpressions.add(
                    new Expression(varRef, e.constant() - expression.constant())
            ));

    return new Bound(modifiedExpressions);
  }

  /**
   * Removes all expressions containing the specified variable.
   *
   * @param variable the variable.
   * @return the modified bound.
   */
  public Bound removeVariableOccurrences(String varRef) {
    var modifiedExpressions = expressions.stream()
            .filter(e -> !e.containsVariable(varRef))
            .collect(Collectors.toSet());
    return new Bound(modifiedExpressions);
  }

  public boolean contains(Expression expression) {
    return expressions().stream().anyMatch(e -> e.equals(expression));
  }

  public boolean contains(Predicate<Expression> predicate) {
    return expressions().stream().anyMatch(predicate);
  }

  public boolean isEmpty() {
    return expressions.isEmpty();
  }

  public static Bound union(Collection<Bound> bounds) {
    return new Bound(
            bounds.stream()
                    .flatMap(e -> e.expressions().stream())
                    .collect(Collectors.toSet())
    );
  }

  public Bound increase(int amount) {
    return new Bound(expressions.stream()
            .map(e -> e.increase(amount))
            .collect(Collectors.toSet()));
  }

  public Bound union(Bound other) {
    return union(other.expressions);
  }

  public Bound union(Set<Expression> otherExpressions) {
    var newExpressions = Stream.concat(
            this.expressions.stream(),
            otherExpressions.stream()
    ).collect(Collectors.toSet());
    return new Bound(newExpressions);
  }

  public Bound intersection(Bound other) {
    return intersection(other.expressions);
  }

  public Bound intersection(Set<Expression> otherExpressions) {
    var newExpressions = this.expressions.stream()
            .filter(otherExpressions::contains)
            .collect(Collectors.toSet());
    return new Bound(newExpressions);
  }

  public Bound difference(Bound other) {
    return difference(other.expressions);
  }

  public Bound difference(Set<Expression> otherExpressions) {
    var newExpressions = this.expressions.stream()
            .filter(o -> !otherExpressions.contains(o))
            .collect(Collectors.toSet());
    return new Bound(newExpressions);
  }

  public Bound relativeComplement(Bound other) {
    return relativeComplement(other.expressions);
  }

  public Bound relativeComplement(Set<Expression> otherExpressions) {
    var newExpressions = otherExpressions.stream()
            .filter(o -> !this.expressions.contains(o))
            .collect(Collectors.toSet());
    return new Bound(newExpressions);
  }

  @Override
  public String toString() {
    return "{%s}".formatted(
            String.join(" ", expressions.stream().map(Expression::toString).sorted().toList()));
  }
}
