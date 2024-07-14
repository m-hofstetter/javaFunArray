package analysis;

import base.DomainValue;
import funarray.Environment;
import java.util.function.UnaryOperator;

public record Analysis<ELEMENT_TYPE extends DomainValue<ELEMENT_TYPE>, VARIABLE_TYPE extends DomainValue<VARIABLE_TYPE>>(
        Environment<ELEMENT_TYPE, VARIABLE_TYPE> state, String protocol) {

  public static final int WIDENING_LOOP_HARD_LIMIT = 1000;

  Analysis(Environment<ELEMENT_TYPE, VARIABLE_TYPE> state) {
    this(state, "");
  }

  public Analysis<ELEMENT_TYPE, VARIABLE_TYPE> whileLoop(Conditional<ELEMENT_TYPE, VARIABLE_TYPE> condition, UnaryOperator<Analysis<ELEMENT_TYPE, VARIABLE_TYPE>> body) {
    return null;
  }

}
