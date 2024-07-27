package analysis.expression.associative;

import analysis.expression.Expression;
import analysis.expression.NormaliseExpressionException;
import analysis.expression.atom.Constant;
import analysis.expression.atom.Variable;
import base.DomainValue;
import funarray.EnvState;
import funarray.NormalExpression;
import java.util.ArrayList;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Multiplication<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> extends Expression<ElementT, VariableT> {

  private final Set<Expression<ElementT, VariableT>> factors;

  public Multiplication(
          Function<Integer, VariableT> constantToVariableValueConversion,
          Function<ElementT, VariableT> arrayElementValueToVariableValueConversion,
          VariableT unknown, Set<Expression<ElementT, VariableT>> factors) {
    super(constantToVariableValueConversion, arrayElementValueToVariableValueConversion, unknown);

    if (factors.size() < 2) {
      throw new IllegalArgumentException("Multiplication operation needs to have at least two operands.");
    }
    this.factors = factors;
  }

  @Override
  public NormalExpression normalise() throws NormaliseExpressionException {
    var variables = factors.stream().filter(e -> e instanceof Variable<ElementT, VariableT>).map(e -> (Variable<ElementT, VariableT>) e).collect(Collectors.toSet());
    var constants = factors.stream().filter(e -> e instanceof Constant<ElementT, VariableT>).map(e -> (Constant<ElementT, VariableT>) e).collect(Collectors.toSet());
    if (variables.size() == 1 && constants.size() == factors.size() - 1) {
      var constantSum = constants.stream().mapToInt(Constant::getConstant).sum();
      return new NormalExpression(variables.stream().toList().getFirst().toString(), constantSum);
    }
    throw new NormaliseExpressionException(this);
  }

  @Override
  public VariableT evaluate(EnvState<ElementT, VariableT> environment) {
    var factorList = new ArrayList<>(factors);
    var product = factorList.get(0).evaluate(environment);
    for (int i = 1; i < factorList.size(); i++) {
      product = product.multiply(factorList.get(i).evaluate(environment));
    }
    return product;
  }

  @Override
  public String toString() {
    return factors.stream()
            .sorted((a, b) -> {
              if (a instanceof Variable<?, ?> && b instanceof Constant<?, ?>) {
                return -1;
              }
              if (a instanceof Constant<?, ?> && b instanceof Variable<?, ?>) {
                return 1;
              }
              return a.toString().compareTo(b.toString());
            })
            .map(Expression::toString)
            .collect(Collectors.joining(" * "));
  }
}
