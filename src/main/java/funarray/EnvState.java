package funarray;

import abstractdomain.DomainValue;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The environment in which an abstract analysis of an Array can be done, consisting of the abstract
 * representation of the array in the form of Cousot et al.'s FunArray and a variable environment.
 *
 * @param funArray  the FunArray
 * @param variables the variable environment
 */
public record EnvState<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        Map<String, FunArray<ElementT>> funArray,
        Map<String, VariableT> variables) {

  public EnvState {
    variables = Map.copyOf(variables);
  }

  /**
   * Adds to a variable. See Cousot et al. 2011, Chapter 11.6.
   *
   * @param varRef the variable
   * @param value  the amount by which to increase it
   * @return the altered FunArray
   */
  public EnvState<ElementT, VariableT> addToVariable(String varRef, int value) {
    var newVariables = new HashMap<>(variables);
    var newVariableValue = variables.get(varRef).addConstant(value);
    newVariables.put(varRef, newVariableValue);
    var modifiedFunArrays = funArray.entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().addToVariable(varRef, value)
            ));
    return new EnvState<>(modifiedFunArrays, newVariables);
  }

  public EnvState<ElementT, VariableT> assignVariable(String varRef, Set<NormalExpression> expressions) {
    var modifiedFunArrays = funArray.entrySet().stream()
            .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            funArrayEntry -> {
                              var funArray = funArrayEntry.getValue();
                              for (NormalExpression expression : expressions) {
                                funArray = funArray
                                        .removeVariableOccurrences(varRef)
                                        .insertExpression(varRef, expression);
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
    var modifiedFunArrays = funArray.entrySet().stream()
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
    var modifiedFunArrays = new HashMap<>(funArray);
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
    return funArray.get(arrRef).get(index);
  }

  @Override
  public String toString() {
    var variablesString = variables.entrySet().stream()
            .map(v -> "%s: %s".formatted(v.getKey(), v.getValue()))
            .collect(Collectors.joining(" "));
    var funArraysString = funArray.entrySet().stream()
            .map(v -> "%s: %s".formatted(v.getKey(), v.getValue()))
            .collect(Collectors.joining("\n"));

    return funArraysString + "\n" + variablesString;
  }

  public EnvState<ElementT, VariableT> join(EnvState<ElementT, VariableT> other,
                                            ElementT unreachable) {
    var modifiedFunArrays = funArray.entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().join(other.funArray().get(e.getKey()), unreachable)
            ));
    return new EnvState<>(modifiedFunArrays, variables);
    //TODO: join variables
  }

  public EnvState<ElementT, VariableT> widen(EnvState<ElementT, VariableT> other,
                                             ElementT unreachable) {
    var modifiedFunArrays = funArray.entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().widen(other.funArray().get(e.getKey()), unreachable)
            ));
    return new EnvState<>(modifiedFunArrays, variables);
    //TODO: proper widening
  }

  public VariableT getVariableValue(String varRef) {
    return variables.get(varRef);
  }

  public EnvState<ElementT, VariableT> satisfyExpressionLessEqualThan(NormalExpression left,
                                                                      NormalExpression right) {
    var modifiedFunArrays = funArray.entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().satisfyBoundExpressionLessEqualThan(left, right)
            ));
    //TODO: Variables need to be modified to satisfy condition
    return new EnvState<>(modifiedFunArrays, variables());
  }

  public EnvState<ElementT, VariableT> satisfyExpressionLessThan(NormalExpression left,
                                                                 NormalExpression right) {
    var modifiedFunArrays = funArray.entrySet().stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().satisfyBoundExpressionLessThan(left, right)
            ));
    //TODO: Variables need to be modified to satisfy condition
    return new EnvState<>(modifiedFunArrays, variables());
  }

  public VariableT calculateExpression(NormalExpression expression) {
    var variableValue = variables.get(expression.varRef());
    return variableValue.addConstant(expression.constant());
  }

}
