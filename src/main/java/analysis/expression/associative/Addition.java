package analysis.expression.associative;

import analysis.expression.Expression;
import analysis.expression.NormaliseExpressionException;
import analysis.expression.atom.Constant;
import analysis.expression.atom.Variable;
import base.DomainValue;
import funarray.EnvState;
import funarray.NormalExpression;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Addition<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> extends AssociativeOperation<ElementT, VariableT> {

  public Addition(
          Function<Integer, VariableT> constantToVariableValueConversion,
          Function<ElementT, VariableT> arrayElementValueToVariableValueConversion,
          VariableT unknown, Set<Expression<ElementT, VariableT>> summands) {
    super(constantToVariableValueConversion, arrayElementValueToVariableValueConversion, unknown, summands);
  }

  @Override
  public NormalExpression normalise(EnvState<ElementT, VariableT> environment) throws NormaliseExpressionException {
    var variables = operands.stream().filter(e -> e instanceof Variable<ElementT, VariableT>).map(e -> (Variable<ElementT, VariableT>) e).collect(Collectors.toSet());
    var constants = operands.stream().filter(e -> e instanceof Constant<ElementT, VariableT>).map(e -> (Constant<ElementT, VariableT>) e).collect(Collectors.toSet());

    var concreteValues = concretizeOperands(environment);

    var normalizable = variables.size() == 1
            && constants.size() + concreteValues.size() == operands.size() - 1;

    if (normalizable) {
      var constantSum = constants.stream().mapToInt(Constant::getConstant).sum();
      var concreteSum = concreteValues.stream().mapToInt(e -> e).sum();
      return new NormalExpression(variables.stream().toList().getFirst().toString(), constantSum + concreteSum);
    }
    throw new NormaliseExpressionException(this);
  }

  @Override
  public VariableT evaluate(EnvState<ElementT, VariableT> environment) {
    return evaluate(environment, VariableT::add);
  }

  @Override
  public String toString() {
    return toString(" + ");
  }
}
