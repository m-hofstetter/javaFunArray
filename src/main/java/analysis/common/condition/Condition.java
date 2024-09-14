package analysis.common.condition;

import static abstractdomain.TriBool.FALSE;
import static abstractdomain.TriBool.TRUE;
import static abstractdomain.TriBool.UNKNOWN;

import abstractdomain.DomainValue;
import abstractdomain.TriBool;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import funarray.NormalExpression;
import funarray.state.State;
import funarray.state.UnreachableState;
import funarray.varref.ZeroReference;
import relation.Relation;

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
  protected final Relation<VariableT> relation;
  protected final String operatorSymbol;

  public Condition(Expression<ElementT, VariableT> left, Expression<ElementT, VariableT> right, AnalysisContext<ElementT, VariableT> context, Relation<VariableT> relation, String operatorSymbol) {
    this.left = left;
    this.right = right;
    this.context = context;
    this.relation = relation;
    this.operatorSymbol = operatorSymbol;
  }


  State<ElementT, VariableT> satisfy(
          State<ElementT, VariableT> state,
          Relation<VariableT> relation
  ) {
    if (state instanceof UnreachableState<ElementT, VariableT>) {
      return state;
    }

    for (NormalExpression l : left.normalise(state)) {
      for (NormalExpression r : right.normalise(state)) {
        if (l.varRef() instanceof ZeroReference && r.varRef() instanceof ZeroReference) {
          continue;
        }
        state = state.forAllArrays(a -> relation.satisfy(a, l, r));
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
    return satisfy(state, relation);
  }

  /**
   * Ensures the condition is not satisfied.
   *
   * @param state the state.
   * @return the state with the condition not satisfied.
   */
  public State<ElementT, VariableT> satisfyComplement(State<ElementT, VariableT> state) {
    return satisfy(state, relation.complementaryOrder());
  }

  public TriBool isSatisfied(State<ElementT, VariableT> state) {
    var normalLeft = left.normalise(state);
    var normalRight = right.normalise(state);

    if (normalLeft.stream().flatMap(l -> normalRight.stream().map(r -> anyArrayBoundOrderSatisfies(state, relation, l, r))).anyMatch(r -> r == TRUE)) {
      return TRUE;
    }

    if (normalLeft.stream().flatMap(l -> normalRight.stream().map(r -> anyArrayBoundOrderSatisfies(state, relation, l, r))).anyMatch(r -> r == FALSE)) {
      return FALSE;
    }

    var evaluatedLeft = left.evaluate(state);
    var evaluatedRight = right.evaluate(state);

    return relation.isSatisfied(evaluatedLeft, evaluatedRight);
  }

  private TriBool anyArrayBoundOrderSatisfies(State<ElementT, VariableT> state, Relation<VariableT> relation, NormalExpression left, NormalExpression right) {
    if (state.arrays().values().stream().anyMatch(a -> relation.isSatisfied(a, left, right) == TRUE)) {
      return TRUE;
    }
    if (state.arrays().values().stream().anyMatch(a -> relation.isSatisfied(a, left, right) == TRUE)) {
      return FALSE;
    }
    return UNKNOWN;
  }

  @Override
  public String toString() {
    return "%s %s %s".formatted(left, operatorSymbol, right);
  }

}
