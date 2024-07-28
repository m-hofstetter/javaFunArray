package analysis.common.expression.atom;

import abstractdomain.DomainValue;
import abstractdomain.exception.ConcretizationException;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import funarray.EnvState;
import funarray.NormalExpression;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ArrayElement<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> extends Expression<ElementT, VariableT> {

  public static final String STRING_TEMPLATE = "%s[%s]";

  private final String arrayRef;
  private final Expression<ElementT, VariableT> index;

  public ArrayElement(
          String arrayRef,
          Expression<ElementT, VariableT> index,
          AnalysisContext<ElementT, VariableT> context
  ) {
    super(context);
    this.arrayRef = arrayRef;
    this.index = index;
  }

  @Override
  public Set<NormalExpression> normalise(EnvState<ElementT, VariableT> environment) {
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
            .map(e -> Set.of(new NormalExpression("0", e)))
            .orElse(Set.of());
  }

  @Override
  public VariableT evaluate(EnvState<ElementT, VariableT> environment) {
    return getElements(environment).stream()
            // array access with index not in fun array will result in unknown value. By
            // intersecting all elements, only those array accesses with a non-trivial value remain.
            .reduce(ElementT::meet)
            .map(context::convertArrayElementValueToVariableValue)
            .orElse(context.getVariableDomain().getUnknown());
  }

  @Override
  public String toString() {
    return STRING_TEMPLATE.formatted(arrayRef, index.toString());
  }

  private Set<ElementT> getElements(EnvState<ElementT, VariableT> environment) {
    return index.normalise(environment).stream()
            .map(e -> environment.getArrayElement(arrayRef, e))
            .collect(Collectors.toSet());
  }
}
