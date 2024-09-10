package analysis.common.expression.nonassociative;

import abstractdomain.DomainValue;
import abstractdomain.ValueRelation;
import abstractdomain.exception.ConcretizationException;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import analysis.common.expression.associative.Addition;
import funarray.NormalExpression;
import funarray.state.State;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Subtraction<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> implements Expression<ElementT, VariableT> {

  private final Expression<ElementT, VariableT> minuend;
  private final Expression<ElementT, VariableT> subtrahend;
  private final AnalysisContext<ElementT, VariableT> context;

  public Subtraction(Expression<ElementT, VariableT> minuend, Expression<ElementT, VariableT> subtrahend, AnalysisContext<ElementT, VariableT> context) {
    this.minuend = minuend;
    this.subtrahend = subtrahend;
    this.context = context;
  }

  @Override
  public Set<NormalExpression> normalise(State<ElementT, VariableT> environment) {
    try {
      var concreteSubtrahend = context.getVariableDomain().concretize(subtrahend.evaluate(environment));
      return minuend.normalise(environment).stream()
              .map(e -> new NormalExpression(e.varRef(), e.constant() - concreteSubtrahend))
              .collect(Collectors.toSet());
    } catch (ConcretizationException e) {
      return Set.of();
    }
  }

  @Override
  public VariableT evaluate(State<ElementT, VariableT> environment) {
    return minuend.evaluate(environment).subtract(subtrahend.evaluate(environment));
  }

  @Override
  public Set<State<ElementT, VariableT>> satisfy(
          Expression<ElementT, VariableT> comparand,
          ValueRelation<VariableT> relation,
          State<ElementT, VariableT> state
  ) {
    var satisfiedMinuend = minuend.satisfy(
            new Addition<>(List.of(comparand, subtrahend), context),
            relation,
            state
    );
    var satisfiedSubtrahend = subtrahend.satisfy(
            new Subtraction<>(minuend, comparand, context),
            relation.inverseOrder(),
            state
    );

    return Stream.concat(
            satisfiedMinuend.stream(),
            satisfiedSubtrahend.stream()
    ).collect(Collectors.toSet()
    );
  }

  @Override
  public String toString() {
    return minuend.toString() + " - " + subtrahend.toString();
  }
}
