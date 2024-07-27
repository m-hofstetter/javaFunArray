package analysis.expression.associative;

import analysis.expression.Expression;
import analysis.expression.atom.Constant;
import analysis.expression.atom.Variable;
import base.DomainValue;
import base.util.Concretization;
import base.util.ConcretizationException;
import funarray.EnvState;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AssociativeOperation<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> extends Expression<ElementT, VariableT> {

  protected final Set<Expression<ElementT, VariableT>> operands;

  public AssociativeOperation(Function<Integer, VariableT> constantToVariableValueConversion,
                              Function<ElementT, VariableT> arrayElementValueToVariableValueConversion,
                              VariableT unknown,
                              Set<Expression<ElementT, VariableT>> operands) {
    super(constantToVariableValueConversion, arrayElementValueToVariableValueConversion, unknown);
    if (operands.size() < 2) {
      throw new IllegalArgumentException("Associative operation needs to have at least two operands.");
    }
    this.operands = operands;
  }

  protected Set<Integer> concretizeOperands(EnvState<ElementT, VariableT> environment) {
    return operands.stream()
            .filter(e -> !(e instanceof Variable<ElementT, VariableT>))
            .filter(e -> !(e instanceof Constant<ElementT, VariableT>))
            .map(value -> {
              try {
                return Concretization.concretize(value.evaluate(environment));
              } catch (ConcretizationException _) {
                return null;
              }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
  }

  protected String toString(String operatorSymbol) {
    return operands.stream()
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
            .collect(Collectors.joining(operatorSymbol));
  }

  protected VariableT evaluate(EnvState<ElementT, VariableT> environment, BinaryOperator<VariableT> operation) {
    var operandList = new ArrayList<>(operands);
    var result = operandList.get(0).evaluate(environment);
    for (int i = 1; i < operandList.size(); i++) {
      result = operation.apply(result, operandList.get(i).evaluate(environment));
    }
    return result;
  }
}
