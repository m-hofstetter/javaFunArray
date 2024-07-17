package funarray;

import base.DomainValue;
import base.infint.InfInt;
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
public record Environment<ELEMENT_TYPE extends DomainValue<ELEMENT_TYPE>, VARIABLE_TYPE extends DomainValue<VARIABLE_TYPE>>(
        FunArray<ELEMENT_TYPE> funArray,
        Map<VariableReference, VARIABLE_TYPE> variables) {

  public Environment {
    variables = Map.copyOf(variables);
  }

  /**
   * Adds to a variable. See Cousot et al. 2011, Chapter 11.6.
   *
   * @param variable the variable
   * @param value    the amount by which to increase it
   * @return the altered FunArray
   */
  public Environment<ELEMENT_TYPE, VARIABLE_TYPE> addToVariable(VariableReference variable, InfInt value) {
    var newVariables = new HashMap<>(variables);
    var newVariableValue = variables.get(variable).addConstant(value);
    newVariables.put(variable, newVariableValue);
    var newSegmentation = funArray.addToVariable(variable, value);
    return new Environment<>(newSegmentation, newVariables);
  }

  public Environment<ELEMENT_TYPE, VARIABLE_TYPE> assignVariable(VariableReference variable, Expression expression) {
    var modifiedFunArray = funArray.removeVariableOccurrences(variable);
    modifiedFunArray = modifiedFunArray.insertExpression(variable, expression);

    var modifiedVariables = new HashMap<>(variables);
    modifiedVariables.put(variable, calculateExpression(expression));

    return new Environment<>(modifiedFunArray, modifiedVariables);
  }

  public Environment<ELEMENT_TYPE, VARIABLE_TYPE> assignVariable(VariableReference variable, VARIABLE_TYPE interval) {
    var modifiedFunArray = funArray.removeVariableOccurrences(variable);

    var modified = new HashMap<>(variables);
    modified.put(variable, interval);

    return new Environment<>(modifiedFunArray, modified);
  }

  /**
   * Assigns a value to the FunArray at a given index.
   *
   * @param index the index.
   * @param value the value.
   * @return the altered FunArray
   */
  public Environment<ELEMENT_TYPE, VARIABLE_TYPE> assignArrayElement(Expression index, ELEMENT_TYPE value) {
    var modified = funArray.insert(index, value);
    return new Environment<>(modified, variables());
  }

  /**
   * Returns the value of the element at a given index.
   *
   * @param index the index.
   * @return the value.
   */
  public ELEMENT_TYPE getArrayElement(Expression index) {
    return funArray.get(index);
  }

  @Override
  public String toString() {
    var variablesString = variables.entrySet().stream()
            .map(v -> "%s: %s".formatted(v.getKey(), v.getValue()))
            .collect(Collectors.joining(" "));

    return "A: %s\n%s".formatted(funArray, variablesString);
  }

  public Environment<ELEMENT_TYPE, VARIABLE_TYPE> join(Environment<ELEMENT_TYPE, VARIABLE_TYPE> other, ELEMENT_TYPE unreachable) {
    var joinedFunArray = funArray.join(other.funArray, unreachable);
    return new Environment<>(joinedFunArray, variables);
    //TODO: join variables
  }

  public Environment<ELEMENT_TYPE, VARIABLE_TYPE> widen(Environment<ELEMENT_TYPE, VARIABLE_TYPE> other, ELEMENT_TYPE unreachable) {
    var widenedFunArray = funArray.widen(other.funArray, unreachable);
    return new Environment<>(widenedFunArray, variables);
    //TODO: proper widening
  }

  public VARIABLE_TYPE getVariableValue(VariableReference variable) {
    return variables.get(variable);
  }

  public VARIABLE_TYPE getVariableValue(String variableName) {
    return variables.get(new VariableReference(variableName));
  }

  public Environment<ELEMENT_TYPE, VARIABLE_TYPE> satisfyExpressionLessEqualThan(Expression left, Expression right) {
    //TODO: Variables need to be modified to satisfy condition
    return new Environment<>(funArray.satisfyBoundExpressionLessEqualThan(left, right), variables());
  }

  public Environment<ELEMENT_TYPE, VARIABLE_TYPE> satisfyExpressionLessThan(Expression left, Expression right) {
    //TODO: Variables need to be modified to satisfy condition
    return new Environment<>(funArray.satisfyBoundExpressionLessThan(left, right), variables());
  }

  public VARIABLE_TYPE calculateExpression(Expression expression) {
    var variableValue = variables.get(expression.variable());
    return variableValue.addConstant(expression.constant());
  }
}
