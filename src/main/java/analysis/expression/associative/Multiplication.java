package analysis.expression.associative;

import analysis.expression.Expression;
import analysis.expression.NormaliseExpressionException;
import base.DomainValue;
import base.util.Concretization;
import base.util.ConcretizationException;
import funarray.EnvState;
import funarray.NormalExpression;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Multiplication<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> extends AssociativeOperation<ElementT, VariableT> {

  public Multiplication(
          Function<Integer, VariableT> constantToVariableValueConversion,
          Function<ElementT, VariableT> arrayElementValueToVariableValueConversion,
          VariableT unknown, Set<Expression<ElementT, VariableT>> factors) {
    super(constantToVariableValueConversion, arrayElementValueToVariableValueConversion, unknown, factors);
  }

  @Override
  public NormalExpression normalise(EnvState<ElementT, VariableT> environment) throws NormaliseExpressionException {
    var values = operands.stream()
            .map(e -> e.evaluate(environment))
            .map(e -> {
              try {
                return Concretization.concretize(e);
              } catch (ConcretizationException _) {
                return null;
              }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

    if (values.size() == operands.size()) {
      var product = values.stream().mapToInt(e -> e).reduce(1, (a, b) -> a * b);
      return new NormalExpression("0", product);
    }

    throw new NormaliseExpressionException(this);
  }

  @Override
  public VariableT evaluate(EnvState<ElementT, VariableT> environment) {
    return evaluate(environment, VariableT::multiply);
  }

  @Override
  public String toString() {
    return toString(" * ");
  }
}
