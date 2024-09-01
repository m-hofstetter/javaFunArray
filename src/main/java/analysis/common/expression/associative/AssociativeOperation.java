package analysis.common.expression.associative;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import analysis.common.expression.atom.Constant;
import analysis.common.expression.atom.Variable;
import funarray.EnvState;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public abstract class AssociativeOperation<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> implements Expression<ElementT, VariableT> {

  protected final AnalysisContext<ElementT, VariableT> context;
  protected final Collection<Expression<ElementT, VariableT>> operands;

  public AssociativeOperation(Collection<Expression<ElementT, VariableT>> operands,
                              AnalysisContext<ElementT, VariableT> context) {
    this.context = context;
    if (operands.size() < 2) {
      throw new IllegalArgumentException("Associative operation needs to have at least two operands.");
    }
    this.operands = operands;
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
