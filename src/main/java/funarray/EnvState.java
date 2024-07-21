package funarray;

import base.DomainValue;
import java.util.HashMap;
import java.util.Map;
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
        FunArray<ElementT> funArray,
        Map<VariableReference, VariableT> variables) {

  public EnvState {
    variables = Map.copyOf(variables);
  }

  /**
   * Adds to a variable. See Cousot et al. 2011, Chapter 11.6.
   *
   * @param variable the variable
   * @param value    the amount by which to increase it
   * @return the altered FunArray
   */
  public EnvState<ElementT, VariableT> addToVariable(VariableReference variable, int value) {
    var newVariables = new HashMap<>(variables);
    var newVariableValue = variables.get(variable).addConstant(value);
    newVariables.put(variable, newVariableValue);
    var newSegmentation = funArray.addToVariable(variable, value);
    return new EnvState<>(newSegmentation, newVariables);
  }

  public EnvState<ElementT, VariableT> assignVariable(VariableReference variable,
                                                      Expression expression) {
    var modifiedFunArray = funArray.removeVariableOccurrences(variable);
    modifiedFunArray = modifiedFunArray.insertExpression(variable, expression);

    var modifiedVariables = new HashMap<>(variables);
    modifiedVariables.put(variable, calculateExpression(expression));

    return new EnvState<>(modifiedFunArray, modifiedVariables);
  }

  public EnvState<ElementT, VariableT> assignVariable(VariableReference variable,
                                                      VariableT interval) {
    var modifiedFunArray = funArray.removeVariableOccurrences(variable);

    var modified = new HashMap<>(variables);
    modified.put(variable, interval);

    return new EnvState<>(modifiedFunArray, modified);
  }

  /**
   * Assigns a value to the FunArray at a given index.
   *
   * @param index the index.
   * @param value the value.
   * @return the altered FunArray
   */
  public EnvState<ElementT, VariableT> assignArrayElement(Expression index, ElementT value) {
    var modified = funArray.insert(index, value);
    return new EnvState<>(modified, variables());
  }

  /**
   * Returns the value of the element at a given index.
   *
   * @param index the index.
   * @return the value.
   */
  public ElementT getArrayElement(Expression index) {
    return funArray.get(index);
  }

  @Override
  public String toString() {
    var variablesString = variables.entrySet().stream()
            .map(v -> "%s: %s".formatted(v.getKey(), v.getValue()))
            .collect(Collectors.joining(" "));

    return "A: %s\n%s".formatted(funArray, variablesString);
  }

  public EnvState<ElementT, VariableT> join(EnvState<ElementT, VariableT> other,
                                            ElementT unreachable) {
    var joinedFunArray = funArray.join(other.funArray, unreachable);
    return new EnvState<>(joinedFunArray, variables);
    //TODO: join variables
  }

  public EnvState<ElementT, VariableT> widen(EnvState<ElementT, VariableT> other,
                                             ElementT unreachable) {
    var widenedFunArray = funArray.widen(other.funArray, unreachable);
    return new EnvState<>(widenedFunArray, variables);
    //TODO: proper widening
  }

  public VariableT getVariableValue(VariableReference variable) {
    return variables.get(variable);
  }

  public VariableT getVariableValue(String variableName) {
    return variables.get(new VariableReference(variableName));
  }

  public EnvState<ElementT, VariableT> satisfyExpressionLessEqualThan(Expression left,
                                                                      Expression right) {
    //TODO: Variables need to be modified to satisfy condition
    return new EnvState<>(funArray.satisfyBoundExpressionLessEqualThan(left, right), variables());
  }

  public EnvState<ElementT, VariableT> satisfyExpressionLessThan(Expression left,
                                                                 Expression right) {
    //TODO: Variables need to be modified to satisfy condition
    return new EnvState<>(funArray.satisfyBoundExpressionLessThan(left, right), variables());
  }

  public VariableT calculateExpression(Expression expression) {
    var variableValue = variables.get(expression.variable());
    return variableValue.addConstant(expression.constant());
  }
}