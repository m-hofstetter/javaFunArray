package analysis.expression.associative;

import analysis.expression.Expression;
import base.DomainValue;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Addition extends Expression {

  private final Set<Expression> summands;

  public Addition(Set<Expression> summands) {
    this.summands = summands;
  }

  public Addition(Expression... summands) {
    this(Set.of(summands));
  }

  @Override
  public <VariableT extends DomainValue<VariableT>> VariableT evaluate(Map<String, VariableT> variableValues, Function<Integer, VariableT> constantToVariableValueConversion) {
    return summands.stream().collect(
            () -> constantToVariableValueConversion.apply(0),
            (a, b) -> a.add(b.evaluate(variableValues, constantToVariableValueConversion)),
            DomainValue::add
    );
  }

  @Override
  public String toString() {
    return summands.stream()
            .map(Expression::toString)
            .sorted()
            .collect(Collectors.joining(" + "));
  }
}
