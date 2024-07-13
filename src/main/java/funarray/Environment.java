package funarray;

import base.DomainValue;
import base.infint.InfInt;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The environment in which an abstract analysis of an Array can be done, consisting of the abstract
 * representation of the array in the form of Cousot et al's FunArray and a variable environment.
 *
 * @param funArray  the FunArray
 * @param variables the variable environment
 */
public record Environment<ELEMENT_TYPE extends DomainValue<ELEMENT_TYPE>, VARIABLE_TYPE extends DomainValue<VARIABLE_TYPE>>(
        FunArray<ELEMENT_TYPE, VARIABLE_TYPE> funArray,
        List<Variable<VARIABLE_TYPE>> variables) {

  private static final String CONSOLE_COLOR_CYAN = "\033[0;36m";
  private static final String CONSOLE_COLOR_RESET = "\033[0m";

  public Environment {
    variables = List.copyOf(variables);
  }

  /**
   * Adds to a variable. See Cousot et al. 2011, Chapter 11.6.
   *
   * @param variable the variable
   * @param value    the amount by which to increase it
   * @return the altered FunArray
   */
  public Environment<ELEMENT_TYPE, VARIABLE_TYPE> addToVariable(Variable<VARIABLE_TYPE> variable, InfInt value) {
    var newVariable = new Variable<>(variable.value().addConstant(value), variable.name());

    var newVariables = new ArrayList<>(variables);

    newVariables.remove(variable);
    newVariables.add(newVariable);
    var newSegmentation = funArray.addToVariable(variable, value);
    return new Environment<>(newSegmentation, newVariables);
  }

  public Environment<ELEMENT_TYPE, VARIABLE_TYPE> assignVariable(Variable<VARIABLE_TYPE> variable, Expression<VARIABLE_TYPE> expression) {
    var modifiedFunArray = funArray.removeVariableOccurrences(variable);
    modifiedFunArray = modifiedFunArray.insertExpression(variable, expression);

    var newVariables = new ArrayList<>(variables);

    newVariables.remove(variable);
    newVariables.add(new Variable<>(expression.calculate(), variable.name()));

    return new Environment<>(modifiedFunArray, newVariables);
  }

  public Environment<ELEMENT_TYPE, VARIABLE_TYPE> assignVariable(Variable<VARIABLE_TYPE> variable, VARIABLE_TYPE interval) {
    var modifiedFunArray = funArray.removeVariableOccurrences(variable);

    var newVariables = new ArrayList<>(variables);
    newVariables.remove(variable);
    newVariables.add(new Variable<>(interval, variable.name()));

    return new Environment<>(modifiedFunArray, newVariables);
  }

  /**
   * Assigns a value to the FunArray at a given index.
   *
   * @param index the index.
   * @param value the value.
   * @return the altered FunArray
   */
  public Environment<ELEMENT_TYPE, VARIABLE_TYPE> assignArrayElement(Expression<VARIABLE_TYPE> index, ELEMENT_TYPE value) {
    var modified = funArray.insert(index, value);
    return new Environment<>(modified, variables());
  }

  /**
   * Returns the value of the element at a given index.
   *
   * @param index the index.
   * @return the value.
   */
  public ELEMENT_TYPE getArrayElement(Expression<VARIABLE_TYPE> index) {
    return funArray.get(index);
  }

  @Override
  public String toString() {
    var variablesString = variables.stream()
            .map(v -> "%s: %s".formatted(v.name(), v.value()))
            .collect(Collectors.joining(" "));

    return "A: %s\n%s".formatted(funArray, variablesString);
  }

  public void consolePrintOut() {
    System.out.printf("%s%s%s%n", CONSOLE_COLOR_CYAN, this, CONSOLE_COLOR_RESET);
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

  public Variable<VARIABLE_TYPE> getVariable(String variableName) {
    return variables.stream().filter(variable -> variable.name().equals(variableName)).findFirst().orElseThrow();
  }
}
