package analysis.common.condition;

import abstractdomain.DomainValue;
import abstractdomain.Relation;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import funarray.BoundRelation;
import funarray.NormalExpression;
import funarray.state.State;

/**
 * A condition for branching control structures in an {@link analysis.common.Analysis}.
 *
 * @param <ElementT>  the domain to abstract array element values with.
 * @param <VariableT> the domain to abstract variable values with.
 */
public abstract class Condition<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> {

  protected final Expression<ElementT, VariableT> left;
  protected final Expression<ElementT, VariableT> right;
  protected final AnalysisContext<ElementT, VariableT> context;
  protected final BoundRelation boundRelation;
  protected final Relation<VariableT> relation;
  protected final String operatorSymbol;

  public Condition(Expression<ElementT, VariableT> left, Expression<ElementT, VariableT> right, AnalysisContext<ElementT, VariableT> context, BoundRelation boundRelation, Relation<VariableT> relation, String operatorSymbol) {
    this.left = left;
    this.right = right;
    this.context = context;
    this.boundRelation = boundRelation;
    this.relation = relation;
    this.operatorSymbol = operatorSymbol;
  }


  State<ElementT, VariableT> satisfy(
          State<ElementT, VariableT> state,
          Relation<VariableT> relation,
          BoundRelation boundRelation
  ) {

    for (NormalExpression l : left.normalise(state)) {
      for (NormalExpression r : right.normalise(state)) {
        state = state.forAllArrays(a -> a.satisfyBoundExpressionRelation(boundRelation, l, r));
      }
    }

    var stateLeftPriority = left.satisfy(right, relation, state).stream()
            .flatMap(s -> right.satisfy(left, relation.inverseOrder(), s).stream())
            .reduce(State.unreachable(context), State::join);

    var stateRightPriority = right.satisfy(left, relation.inverseOrder(), state).stream()
            .flatMap(s -> left.satisfy(right, relation, s).stream())
            .reduce(State.unreachable(context), State::join);

    return stateLeftPriority.join(stateRightPriority);
  }

  /**
   * Satisfies the condition.
   *
   * @param state the state.
   * @return the state with the condition satisfied.
   */
  public State<ElementT, VariableT> satisfy(State<ElementT, VariableT> state) {
    return satisfy(state, relation, boundRelation);
  }

  /**
   * Ensures the condition is not satisfied.
   *
   * @param state the state.
   * @return the state with the condition not satisfied.
   */
  public State<ElementT, VariableT> satisfyComplement(State<ElementT, VariableT> state) {
    return satisfy(state, relation.complementaryOrder(), boundRelation.complementRelation());
  }

  @Override
  public String toString() {
    return "%s %s %s".formatted(left, operatorSymbol, right);
  }

}
