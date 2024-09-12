package funarray.state;

import abstractdomain.DomainValue;
import abstractdomain.TriBool;
import analysis.common.AnalysisContext;
import funarray.BoundRelation;
import funarray.FunArray;
import funarray.NormalExpression;
import funarray.varref.Reference;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;

public interface State<ElementT extends DomainValue<ElementT>, VariableT extends DomainValue<VariableT>> {
  State<ElementT, VariableT> assignVariable(Reference varRef, Set<NormalExpression> expressions);

  State<ElementT, VariableT> assignVariable(Reference varRef, VariableT value);

  State<ElementT, VariableT> assignArrayElement(String arrRef,
                                                Set<NormalExpression> indeces,
                                                ElementT value);

  State<ElementT, VariableT> assignArrayElement(String arrRef,
                                                NormalExpression index,
                                                ElementT value);

  ElementT getArrayElement(String arrRef, NormalExpression index);

  @Override
  String toString();

  State<ElementT, VariableT> join(State<ElementT, VariableT> other);

  State<ElementT, VariableT> widen(State<ElementT, VariableT> other);

  VariableT getVariableValue(Reference varRef);

  VariableT calculateExpression(NormalExpression expression);

  boolean isReachable();

  Map<String, FunArray<ElementT>> arrays();

  Map<Reference, VariableT> variables();

  AnalysisContext<ElementT, VariableT> context();

  static <ElementT extends DomainValue<ElementT>, VariableT extends DomainValue<VariableT>>
  UnreachableState<ElementT, VariableT> unreachable(AnalysisContext<ElementT, VariableT> context) {
    return new UnreachableState<>(context);
  }

  State<ElementT, VariableT> forAllArrays(UnaryOperator<FunArray<ElementT>> op);

  TriBool isSatisifed(NormalExpression left, BoundRelation relation, NormalExpression right);
}
