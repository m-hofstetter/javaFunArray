package analysis.expression.atom;

import analysis.expression.Expression;
import analysis.expression.NormaliseExpressionException;
import base.DomainValue;
import base.util.Concretization;
import base.util.ConcretizationException;
import funarray.EnvState;
import funarray.NormalExpression;
import java.util.function.Function;

public class ArrayElement<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> extends Expression<ElementT, VariableT> {

  public static final String STRING_TEMPLATE = "%s[%s]";

  private final String arrayRef;
  private final Expression<ElementT, VariableT> index;

  public ArrayElement(
          Function<Integer, VariableT> constantToVariableValueConversion,
          Function<ElementT, VariableT> arrayElementValueToVariableValueConversion,
          VariableT unknown,
          String arrayRef,
          Expression<ElementT, VariableT> index
  ) {
    super(constantToVariableValueConversion, arrayElementValueToVariableValueConversion, unknown);
    this.arrayRef = arrayRef;
    this.index = index;
  }

  @Override
  public NormalExpression normalise(EnvState<ElementT, VariableT> environment) throws NormaliseExpressionException {
    try {
      var normalisedIndex = index.normalise(environment);
      var elementValue = environment.getArrayElement(arrayRef, normalisedIndex);
      var concretisedValue = Concretization.concretize(arrayElementValueToVariableValueConversion.apply(elementValue));
      return new NormalExpression("0", concretisedValue);
    } catch (ConcretizationException _) {
      throw new NormaliseExpressionException(this);
    }
  }

  @Override
  public VariableT evaluate(EnvState<ElementT, VariableT> environment) {
    try {
      return arrayElementValueToVariableValueConversion.apply(
              environment.getArrayElement(arrayRef, index.normalise(environment))
      );
    } catch (NormaliseExpressionException e) {
      return unknown;
    }
  }

  @Override
  public String toString() {
    return STRING_TEMPLATE.formatted(arrayRef, index.toString());
  }

  public static void temp() {

  }
}
