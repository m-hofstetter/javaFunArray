package analysis.expression.atom;

import analysis.expression.Expression;
import analysis.expression.NormaliseExpressionException;
import base.DomainValue;
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
          String arrayRef,
          Expression<ElementT, VariableT> index,
          VariableT unknown
  ) {
    super(constantToVariableValueConversion, arrayElementValueToVariableValueConversion, unknown);
    this.arrayRef = arrayRef;
    this.index = index;
  }

  @Override
  public NormalExpression normalise() throws NormaliseExpressionException {
    //TODO: might also be possible to normalise this, e.g.: if A[x] = [5, 5], this expression can be
    // normalised to "0" + 5
    throw new NormaliseExpressionException(this);
  }

  @Override
  public VariableT evaluate(EnvState<ElementT, VariableT> environment) {
    try {
      return arrayElementValueToVariableValueConversion.apply(
              environment.getArrayElement(arrayRef, index.normalise())
      );
    } catch (NormaliseExpressionException e) {
      return unknown;
    }
  }

  @Override
  public String toString() {
    return STRING_TEMPLATE.formatted(arrayRef, index.toString());
  }
}
