package analysis.common.expression.atom;

import abstractdomain.DomainValue;
import abstractdomain.exception.ConcretizationException;
import analysis.common.AnalysisContext;
import analysis.common.expression.Assignable;
import analysis.common.expression.Expression;
import funarray.NormalExpression;
import funarray.state.State;
import funarray.varref.Reference;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import relation.Relation;

public record ArrayElement<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(String arrayRef,
                                                  Expression<ElementT, VariableT> index,
                                                  AnalysisContext<ElementT, VariableT> context)
        implements Assignable<ElementT, VariableT> {

  public static final String STRING_TEMPLATE = "%s[%s]";

  @Override
  public Set<NormalExpression> normalise(State<ElementT, VariableT> environment) {
    var values = getElements(environment);

    return values.stream()
            .map(e -> {
              try {
                return context.getElementDomain().concretize(e);
              } catch (ConcretizationException ex) {
                return null;
              }
            })
            // array access with index not in fun array will result in unknown value. Unknown cannot
            // be concretized and has to be filtered out.
            .filter(Objects::nonNull)
            .findAny()
            .map(e -> Set.of(new NormalExpression(Reference.zero(), e)))
            .orElse(Set.of());
  }

  @Override
  public VariableT evaluate(State<ElementT, VariableT> environment) {
    return getElements(environment).stream()
            // array access with index not in fun array will result in unknown value. By
            // intersecting all elements, only those array accesses with a non-trivial value remain.
            .reduce(ElementT::meet)
            .map(context::convertArrayElementValueToVariableValue)
            .orElse(context.getVariableDomain().getUnknown());
  }

  @Override
  public Set<State<ElementT, VariableT>> satisfy(Expression<ElementT, VariableT> comparand, Relation<VariableT> relation, State<ElementT, VariableT> state) {
    var comparandValue = comparand.evaluate(state);
    var currentValue = evaluate(state);
    var satisfiedValue = relation.satisfy(currentValue, comparandValue);
    return Set.of(state.assignArrayElement(arrayRef, index.normalise(state), context.convertVariableValueToArrayElementValue(satisfiedValue)));
  }

  @Override
  public String toString() {
    return STRING_TEMPLATE.formatted(arrayRef, index.toString());
  }

  private Set<ElementT> getElements(State<ElementT, VariableT> environment) {
    return index.normalise(environment).stream()
            .map(e -> environment.getArrayElement(arrayRef, e))
            .collect(Collectors.toSet());
  }

  @Override
  public State<ElementT, VariableT> assign(Expression<ElementT, VariableT> value, State<ElementT, VariableT> state) {
    return state.assignArrayElement(
            arrayRef,
            index.normalise(state),
            context.convertVariableValueToArrayElementValue(value.evaluate(state))
    );
  }
}
