package analysis.expression.associative;

import analysis.expression.Expression;
import base.DomainValue;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Multiplication extends Expression {

  private final Set<Expression> factors;

  public Multiplication(Set<Expression> factors) {
    this.factors = factors;
  }

  public Multiplication(Expression... factors) {
    this(Set.of(factors));
  }

  @Override
  public <VariableT extends DomainValue<VariableT>> VariableT evaluate(Map<String, VariableT> variableValues, Function<Integer, VariableT> constantToVariableValueConversion) {
    return factors.stream().collect(
            () -> constantToVariableValueConversion.apply(0),
            (a, b) -> a.multiply(b.evaluate(variableValues, constantToVariableValueConversion)),
            DomainValue::multiply
    );
  }

  @Override
  public String toString() {
    return factors.stream()
            .map(Expression::toString)
            .sorted()
            .collect(Collectors.joining(" * "));
  }
}
