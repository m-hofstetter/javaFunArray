package funarray.state;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import funarray.FunArray;
import funarray.NormalExpression;
import funarray.varref.Reference;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;

public class UnreachableState<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>> implements State<ElementT, VariableT> {

  private final AnalysisContext<ElementT, VariableT> context;

  public UnreachableState(AnalysisContext<ElementT, VariableT> context) {
    this.context = context;
  }

  @Override
  public State<ElementT, VariableT> assignVariable(Reference varRef, Set<NormalExpression> expressions) {
    return this;
  }

  @Override
  public State<ElementT, VariableT> assignVariable(Reference varRef, VariableT value) {
    return this;
  }

  @Override
  public State<ElementT, VariableT> assignArrayElement(String arrRef, Set<NormalExpression> indeces, ElementT value) {
    return this;
  }

  @Override
  public State<ElementT, VariableT> assignArrayElement(String arrRef, NormalExpression index, ElementT value) {
    return this;
  }

  @Override
  public ElementT getArrayElement(String arrRef, NormalExpression index) {
    return context().getElementDomain().getUnreachable();
  }

  @Override
  public State<ElementT, VariableT> join(State<ElementT, VariableT> other) {
    return other;
  }

  @Override
  public State<ElementT, VariableT> widen(State<ElementT, VariableT> other) {
    return other;
  }

  @Override
  public VariableT getVariableValue(Reference varRef) {
    return context().getVariableDomain().getUnreachable();
  }

  @Override
  public VariableT calculateExpression(NormalExpression expression) {
    return context().getVariableDomain().getUnreachable();
  }

  @Override
  public boolean isReachable() {
    return false;
  }

  @Override
  public Map<String, FunArray<ElementT>> arrays() {
    return Map.of();
  }

  @Override
  public Map<Reference, VariableT> variables() {
    return Map.of();
  }

  @Override
  public AnalysisContext<ElementT, VariableT> context() {
    return context;
  }

  @Override
  public State<ElementT, VariableT> forAllArrays(UnaryOperator<FunArray<ElementT>> op) {
    return this;
  }

  @Override
  public String toString() {
    return "⊥";
  }
}
