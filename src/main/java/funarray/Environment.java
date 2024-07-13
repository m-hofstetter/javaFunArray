package funarray;

import base.DomainValue;
import base.infint.InfInt;
import base.interval.Interval;
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
public record Environment<T extends DomainValue<T>>(FunArray<T> funArray,
                                                    List<Variable> variables) {

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
  public Environment<T> addToVariable(Variable variable, InfInt value) {
    var newVariable = new Variable(variable.value().addConstant(value), variable.name());

    var newVariables = new ArrayList<>(variables);

    newVariables.remove(variable);
    newVariables.add(newVariable);
    var newSegmentation = funArray.addToVariable(variable, value);
    return new Environment<>(newSegmentation, newVariables);
  }

  public Environment<T> assignVariable(Variable variable, Expression expression) {
    var modifiedFunArray = funArray.removeVariableOccurrences(variable);
    modifiedFunArray = modifiedFunArray.insertExpression(variable, expression);

    var newVariables = new ArrayList<>(variables);

    newVariables.remove(variable);
    newVariables.add(new Variable(expression.calculate(), variable.name()));

    return new Environment<>(modifiedFunArray, newVariables);
  }

  public Environment<T> assignVariable(Variable variable, Interval interval) {
    var modifiedFunArray = funArray.removeVariableOccurrences(variable);

    var newVariables = new ArrayList<>(variables);
    newVariables.remove(variable);
    newVariables.add(new Variable(interval, variable.name()));

    return new Environment<>(modifiedFunArray, newVariables);
  }

  /**
   * Assigns a value to the FunArray at a given index.
   *
   * @param index the index.
   * @param value the value.
   * @return the altered FunArray
   */
  public Environment<T> assignArrayElement(Expression index, T value) {
    var modified = funArray.insert(index, value);
    return new Environment<>(modified, variables());
  }

  /**
   * Returns the value of the element at a given index.
   *
   * @param index the index.
   * @return the value.
   */
  public T getArrayElement(Expression index) {
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

  public Environment<T> join(Environment<T> other, T unreachable) {
    var joinedFunArray = funArray.join(other.funArray, unreachable);
    return new Environment<>(joinedFunArray, variables);
    //TODO: join variables
  }

  public Environment<T> widen(Environment<T> other, T unreachable) {
    var widenedFunArray = funArray.widen(other.funArray, unreachable);
    return new Environment<>(widenedFunArray, variables);
    //TODO: proper widening
  }

  public Variable getVariable(String variableName) {
    return variables.stream().filter(variable -> variable.name().equals(variableName)).findFirst().orElseThrow();
  }
}
