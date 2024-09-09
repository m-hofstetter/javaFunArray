package funarray.state;

import abstractdomain.DomainValue;
import analysis.common.AnalysisContext;
import funarray.FunArray;
import funarray.NormalExpression;
import funarray.varref.Reference;
import funarray.varref.VariableReference;
import funarray.varref.ZeroReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * The environment in which an abstract analysis of an Array can be done, consisting of the abstract
 * representation of the array in the form of Cousot et al.'s FunArray and a variable environment.
 *
 * @param arrays    the FunArray
 * @param variables the variable environment
 */
public record ReachableState<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        Map<String, FunArray<ElementT>> arrays,
        Map<Reference, VariableT> variables,
        AnalysisContext<ElementT, VariableT> context) implements State<ElementT, VariableT> {

  public ReachableState {
    variables = Map.copyOf(variables);
  }

  public ReachableState(Collection<String> variables,
                        Collection<String> arrays,
                        AnalysisContext<ElementT, VariableT> context) {
    this(
            arrays.stream().collect(Collectors.toMap(
                    arrayRef -> arrayRef,
                    arrayRef -> new FunArray<>(arrayRef + ".length", context.getElementDomain().getUnknown())
            )),
            variables.stream().collect(Collectors.toMap(
                    Reference::of,
                    _ -> context.getVariableDomain().getUnknown()
            )),
            context
    );

  }

  @Override
  public ReachableState<ElementT, VariableT> assignVariable(Reference varRef, Set<NormalExpression> expressions) {
    var modifiedFunArrays = arrays.entrySet().stream()
            .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            funArrayEntry -> funArrayEntry.getValue().insertExpression(varRef, expressions)
                    )
            );

    var modifiedVariables = new HashMap<>(variables);
    modifiedVariables.put(varRef, calculateExpression(expressions.stream().findAny().orElseThrow(IllegalArgumentException::new)));

    return new ReachableState<>(modifiedFunArrays, modifiedVariables, context);
  }

  @Override
  public ReachableState<ElementT, VariableT> assignVariable(Reference varRef, VariableT value) {
    var modifiedFunArrays = arrays.entrySet().stream()
            .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            funArrayEntry -> funArrayEntry.getValue().removeVariableOccurrences(varRef)
                    )
            );

    var modifiedVariables = new HashMap<>(variables);
    modifiedVariables.put(varRef, value);

    return new ReachableState<>(modifiedFunArrays, modifiedVariables, context);
  }

  /**
   * Assigns a value to the FunArray at a given index.
   *
   * @param indeces the indeces.
   * @param value   the value.
   * @return the altered FunArray
   */
  @Override
  public ReachableState<ElementT, VariableT> assignArrayElement(String arrRef,
                                                                Set<NormalExpression> indeces,
                                                                ElementT value) {
    var modifiedFunArrays = new HashMap<>(arrays);
    modifiedFunArrays.put(arrRef,
            modifiedFunArrays.get(arrRef)
                    .insert(indeces, value)
    );
    return new ReachableState<>(modifiedFunArrays, variables(), context);
  }

  @Override
  public ReachableState<ElementT, VariableT> assignArrayElement(String arrRef,
                                                                NormalExpression index,
                                                                ElementT value) {
    return assignArrayElement(arrRef, Set.of(index), value);
  }

  /**
   * Returns the value of the element at a given index.
   *
   * @param index the index.
   * @return the value.
   */
  @Override
  public ElementT getArrayElement(String arrRef, NormalExpression index) {
    return arrays.get(arrRef).get(index);
  }

  @Override
  public String toString() {
    var variablesString = variables.entrySet().stream()
            .map(v -> "%s: %s".formatted(v.getKey(), v.getValue()))
            .collect(Collectors.joining(" "));
    var funArraysString = arrays.entrySet().stream()
            .map(v -> "%s: %s".formatted(v.getKey(), v.getValue()))
            .collect(Collectors.joining("\n"));

    return funArraysString + "\n" + variablesString;
  }

  @Override
  public ReachableState<ElementT, VariableT> join(State<ElementT, VariableT> other) {
    if (other instanceof ReachableState<ElementT, VariableT> otherReachable) {

      if (!this.isReachable()) {
        return otherReachable;
      }

      if (!otherReachable.isReachable()) {
        return this;
      }

      var modifiedFunArrays = arrays.entrySet().stream()
              .collect(Collectors.toMap(
                      Map.Entry::getKey,
                      e -> e.getValue().join(otherReachable.arrays().get(e.getKey()), context.getElementDomain().getUnreachable())
              ));

      var modifiedVariables = variables.entrySet().stream()
              .collect(Collectors.toMap(
                      Map.Entry::getKey,
                      e -> e.getValue().join(otherReachable.variables.get(e.getKey()))
              ));

      return new ReachableState<>(modifiedFunArrays, modifiedVariables, context);
    } else {
      return this;
    }
  }

  @Override
  public State<ElementT, VariableT> widen(State<ElementT, VariableT> other) {
    if (other instanceof ReachableState<ElementT, VariableT> otherReachable) {
      var modifiedFunArrays = arrays.entrySet().stream()
              .collect(Collectors.toMap(
                      Map.Entry::getKey,
                      e -> e.getValue().widen(otherReachable.arrays().get(e.getKey()), context.getElementDomain().getUnreachable())
              ));

      var modifiedVariables = variables.entrySet().stream()
              .collect(Collectors.toMap(
                      Map.Entry::getKey,
                      e -> e.getValue().widen(otherReachable.variables.get(e.getKey()))
              ));

      return new ReachableState<>(modifiedFunArrays, modifiedVariables, context);
    } else {
      return other;
    }
  }

  @Override
  public VariableT getVariableValue(Reference varRef) {
    return switch (varRef) {
      case VariableReference variableReference -> variables.get(variableReference);
      case ZeroReference _ -> context.getVariableDomain().getZeroValue();
    };
  }

  @Override
  public ReachableState<ElementT, VariableT> satisfyExpressionLessEqualThanInBoundOrder(NormalExpression left,
                                                                                        NormalExpression right) {
    var modifiedFunArrays = arrays.entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().satisfyBoundExpressionLessEqualThan(left, right)
            ));
    return new ReachableState<>(modifiedFunArrays, variables(), context);
  }

  @Override
  public ReachableState<ElementT, VariableT> satisfyExpressionLessThanInBoundOrder(NormalExpression left,
                                                                                   NormalExpression right) {
    var modifiedFunArrays = arrays.entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().satisfyBoundExpressionLessThan(left, right)
            ));
    return new ReachableState<>(modifiedFunArrays, variables(), context);
  }

  @Override
  public ReachableState<ElementT, VariableT> satisfyExpressionEqualToInBoundOrder(NormalExpression left,
                                                                                  NormalExpression right) {
    var modifiedFunArrays = arrays.entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().satisfyBoundExpressionEqualTo(left, right)
            ));
    return new ReachableState<>(modifiedFunArrays, variables(), context);
  }

  @Override
  public ReachableState<ElementT, VariableT> satisfyExpressionUnequalToInBoundOrder(NormalExpression left,
                                                                                    NormalExpression right) {
    var modifiedFunArrays = arrays.entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().satisfyBoundExpressionUnequalTo(left, right)
            ));
    return new ReachableState<>(modifiedFunArrays, variables(), context);
  }

  @Override
  public ReachableState<ElementT, VariableT> satisfyForValues(NormalExpression comparandum,
                                                              VariableT comparand,
                                                              BinaryOperator<VariableT> operator) {
    var modifiedVariables = new HashMap<>(variables);
    var comparandAsValue = comparand.subtractConstant(comparandum.constant());
    var satisfiedComparandumValue = operator.apply(getVariableValue(comparandum.varRef()), comparandAsValue);
    modifiedVariables.put(comparandum.varRef(), satisfiedComparandumValue);
    return new ReachableState<>(arrays, modifiedVariables, context);
  }

  @Override
  public ReachableState<ElementT, VariableT> satisfyForValues(String arrRefComparandum,
                                                              NormalExpression indexComparandum,
                                                              ElementT comparand,
                                                              BinaryOperator<ElementT> operator) {
    var valueAtIndex = getArrayElement(arrRefComparandum, indexComparandum);
    var modifiedValue = operator.apply(valueAtIndex, comparand);
    return assignArrayElement(arrRefComparandum, indexComparandum, modifiedValue);
  }

  @Override
  public VariableT calculateExpression(NormalExpression expression) {
    var variableValue = getVariableValue(expression.varRef());
    return variableValue.addConstant(expression.constant());
  }

  @Override
  public boolean isReachable() {
    return variables.values().stream().allMatch(DomainValue::isReachable)
            && arrays.values().stream().allMatch(FunArray::isReachable);
  }

}
