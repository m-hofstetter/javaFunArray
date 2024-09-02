package funarray;

import abstractdomain.DomainValue;
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
 * @param arrays  the FunArray
 * @param variables the variable environment
 */
public record EnvState<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        Map<String, FunArray<ElementT>> arrays,
        Map<String, VariableT> variables) {

  public EnvState {
    variables = Map.copyOf(variables);
  }

  public EnvState(Collection<String> variables, VariableT unknownVariableValue,
                  Collection<String> arrays, ElementT unknwonElementValue) {
    this(
            arrays.stream().collect(Collectors.toMap(
                    arrayRef -> arrayRef,
                    arrayRef -> new FunArray<>(arrayRef + ".length", unknwonElementValue)
            )),
            variables.stream().collect(Collectors.toMap(
                    varRef -> varRef,
                    _ -> unknownVariableValue
            ))
    );

  }

  public EnvState<ElementT, VariableT> assignVariable(String varRef, Set<NormalExpression> expressions) {
    var modifiedFunArrays = arrays.entrySet().stream()
            .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            funArrayEntry -> {
                              var funArray = funArrayEntry.getValue();
                              for (NormalExpression expression : expressions) {
                                funArray = funArray.insertExpression(varRef, expression);
                              }
                              return funArray;
                            }
                    )
            );

    var modifiedVariables = new HashMap<>(variables);
    modifiedVariables.put(varRef, calculateExpression(expressions.stream().findAny().orElseThrow(IllegalArgumentException::new)));

    return new EnvState<>(modifiedFunArrays, modifiedVariables);
  }

  public EnvState<ElementT, VariableT> assignVariable(String varRef, VariableT value) {
    var modifiedFunArrays = arrays.entrySet().stream()
            .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            funArrayEntry -> funArrayEntry.getValue().removeVariableOccurrences(varRef)
                    )
            );

    var modifiedVariables = new HashMap<>(variables);
    modifiedVariables.put(varRef, value);

    return new EnvState<>(modifiedFunArrays, modifiedVariables);
  }

  /**
   * Assigns a value to the FunArray at a given index.
   *
   * @param index the index.
   * @param value the value.
   * @return the altered FunArray
   */
  public EnvState<ElementT, VariableT> assignArrayElement(String arrRef,
                                                          Set<NormalExpression> indeces,
                                                          ElementT value) {
    var modifiedFunArrays = new HashMap<>(arrays);
    modifiedFunArrays.put(arrRef,
            modifiedFunArrays.get(arrRef)
                    .insert(indeces, value)
    );
    return new EnvState<>(modifiedFunArrays, variables());
  }

  public EnvState<ElementT, VariableT> assignArrayElement(String arrRef,
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

  public EnvState<ElementT, VariableT> join(EnvState<ElementT, VariableT> other,
                                            ElementT unreachable) {
    var modifiedFunArrays = arrays.entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().join(other.arrays().get(e.getKey()), unreachable)
            ));

    var modifiedVariables = variables.entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().join(other.variables.get(e.getKey()))
            ));

    return new EnvState<>(modifiedFunArrays, modifiedVariables);
  }

  public EnvState<ElementT, VariableT> widen(EnvState<ElementT, VariableT> other,
                                             ElementT unreachable) {
    var modifiedFunArrays = arrays.entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().widen(other.arrays().get(e.getKey()), unreachable)
            ));

    var modifiedVariables = variables.entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().widen(other.variables.get(e.getKey()))
            ));

    return new EnvState<>(modifiedFunArrays, modifiedVariables);
  }

  public VariableT getVariableValue(String varRef) {
    return variables.get(varRef);
  }

  public EnvState<ElementT, VariableT> satisfyExpressionLessEqualThanInBoundOrder(NormalExpression left,
                                                                                  NormalExpression right) {
    var modifiedFunArrays = arrays.entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().satisfyBoundExpressionLessEqualThan(left, right)
            ));
    return new EnvState<>(modifiedFunArrays, variables());
  }

  public EnvState<ElementT, VariableT> satisfyExpressionLessThanInBoundOrder(NormalExpression left,
                                                                             NormalExpression right) {
    var modifiedFunArrays = arrays.entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().satisfyBoundExpressionLessThan(left, right)
            ));
    return new EnvState<>(modifiedFunArrays, variables());
  }

  public EnvState<ElementT, VariableT> satisfyExpressionEqualToInBoundOrder(NormalExpression left,
                                                                            NormalExpression right) {
    var modifiedFunArrays = arrays.entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().satisfyBoundExpressionEqualTo(left, right)
            ));
    return new EnvState<>(modifiedFunArrays, variables());
  }

  public EnvState<ElementT, VariableT> satisfyExpressionUnequalToInBoundOrder(NormalExpression left,
                                                                              NormalExpression right) {
    var modifiedFunArrays = arrays.entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().satisfyBoundExpressionUnequalTo(left, right)
            ));
    return new EnvState<>(modifiedFunArrays, variables());
  }

  public EnvState<ElementT, VariableT> satisfyForValues(NormalExpression comparandum,
                                                        VariableT comparand,
                                                        BinaryOperator<VariableT> operator) {
    var modifiedVariables = new HashMap<>(variables);
    var comparandAsValue = comparand.subtractConstant(comparandum.constant());
    var satisfiedComparandumValue = operator.apply(getVariableValue(comparandum.varRef()), comparandAsValue);
    modifiedVariables.put(comparandum.varRef(), satisfiedComparandumValue);
    return new EnvState<>(arrays, modifiedVariables);
  }

  public EnvState<ElementT, VariableT> satisfyForValues(String arrRefComparandum,
                                                        NormalExpression indexComparandum,
                                                        ElementT comparand,
                                                        BinaryOperator<ElementT> operator) {
    var valueAtIndex = getArrayElement(arrRefComparandum, indexComparandum);
    var modifiedValue = operator.apply(valueAtIndex, comparand);
    return assignArrayElement(arrRefComparandum, indexComparandum, modifiedValue);
  }

  public VariableT calculateExpression(NormalExpression expression) {
    var variableValue = variables.get(expression.varRef());
    return variableValue.addConstant(expression.constant());
  }

}
