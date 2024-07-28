package analysis.common;

import abstractdomain.Domain;
import abstractdomain.DomainValue;

public interface AnalysisContext<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> {

  Domain<ElementT> getElementDomain();

  Domain<VariableT> getVariableDomain();

  ElementT convertVariableValueToArrayElementValue(VariableT variableValue);

  VariableT convertArrayElementValueToVariableValue(ElementT arrayElementValue);
}
