package funarray;

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
public record Environment(FunArray funArray, List<Variable> variables) {

  public Environment {
    variables = List.copyOf(variables);
  }

  public Environment(Expression length) {
    this(new FunArray(length, false), List.of(length.variable()));
  }

  /**
   * Adds to a variable. See Cousot et al. 2011, Chapter 11.6.
   *
   * @param variable the variable
   * @param value    the amount by which to increase it
   * @return the altered FunArray
   */
  public Environment addToVariable(Variable variable, InfInt value) {
    var newVariable = new Variable(variable.value().add(value), variable.name());

    var newVariables = new ArrayList<>(variables);

    newVariables.remove(variable);
    newVariables.add(newVariable);
    var newSegmentation = funArray.addToVariable(variable, value);
    return new Environment(newSegmentation, newVariables);
  }

  public Environment assignVariable(Variable variable, Expression expression) {
    var modifiedFunArray = funArray.assignVariable(variable, expression);
    var newVariables = new ArrayList<>(variables);

    newVariables.remove(variable);
    newVariables.add(new Variable(expression.calculate(), variable.name()));

    return new Environment(modifiedFunArray, newVariables);
  }

  /**
   * Assigns a value to the FunArray at a given index.
   *
   * @param index the index.
   * @param value the value.
   * @return the altered FunArray
   */
  public Environment assignArrayElement(Expression index, Interval value) {
    var modified = funArray.insert(index, index.increase(InfInt.of(1)), value);
    return new Environment(modified, variables());
  }

  /**
   * Returns the value of the element at a given index.
   *
   * @param index the index.
   * @return the value.
   */
  public Interval getArrayElement(Expression index) {
    return funArray.get(index);
  }

  @Override
  public String toString() {
    var variablesString = variables.stream()
            .map(Variable::toStringWithValue)
            .collect(Collectors.joining(" "));

    return "A: %s\n%s".formatted(funArray, variablesString);
  }
}
