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

public class Addition<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> extends Expression<ElementT, VariableT> {

  private final Set<Expression<ElementT, VariableT>> summands;

  public Addition(
          Function<Integer, VariableT> constantToVariableValueConversion,
          Function<ElementT, VariableT> arrayElementValueToVariableValueConversion,
          VariableT unknown, Set<Expression<ElementT, VariableT>> summands) {
    super(constantToVariableValueConversion, arrayElementValueToVariableValueConversion, unknown);

    if (summands.size() < 2) {
      throw new IllegalArgumentException("Addition operation needs to have at least two operands.");
    }
    this.summands = summands;
  }

  @Override
  public NormalExpression normalise() throws NormaliseExpressionException {
    var variables = summands.stream().filter(e -> e instanceof Variable<ElementT, VariableT>).map(e -> (Variable<ElementT, VariableT>) e).collect(Collectors.toSet());
    var constants = summands.stream().filter(e -> e instanceof Constant<ElementT, VariableT>).map(e -> (Constant<ElementT, VariableT>) e).collect(Collectors.toSet());
    if (variables.size() == 1 && constants.size() == summands.size() - 1) {
      var constantSum = constants.stream().mapToInt(Constant::getConstant).sum();
      return new NormalExpression(variables.stream().toList().getFirst().toString(), constantSum);
    }
    throw new NormaliseExpressionException(this);
  }

  @Override
  public VariableT evaluate(EnvState<ElementT, VariableT> environment) {
    var summandList = new ArrayList<>(summands);
    var sum = summandList.get(0).evaluate(environment);
    for (int i = 1; i < summandList.size(); i++) {
      sum = sum.add(summandList.get(i).evaluate(environment));
    }
    return sum;
  }

  @Override
  public String toString() {
    return summands.stream()
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
            .collect(Collectors.joining(" + "));
  }
}
