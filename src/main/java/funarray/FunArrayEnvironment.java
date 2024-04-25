package funarray;

import base.*;
import java.util.*;
import java.util.stream.*;

/**
 * The environment in which an abstract analysis of an Array can be done, consisting of the abstract
 * representation of the array in the form of Cousot et al's FunArray and a variable environment.
 *
 * @param funArray  the FunArray
 * @param variables the variable environment
 */
public record FunArrayEnvironment(FunArray funArray, List<Variable> variables) {

  public FunArrayEnvironment {
    variables = List.copyOf(variables);
  }

  public FunArrayEnvironment(Expression length) {
    this(new FunArray(length, false), List.of(length.variable()));
  }

  /**
   * Adds to a variable. See Cousot et al. 2011, Chapter 11.6.
   *
   * @param variable the variable
   * @param value    the amount by which to increase it
   * @return the altered FunArray
   */
  public FunArrayEnvironment addToVariable(Variable variable, int value) {
    var newVariable = new Variable(variable.value().add(value), variable.name());

    var newVariables = new ArrayList<>(variables);

    newVariables.remove(variable);
    newVariables.add(newVariable);
    var newSegmentation = funArray.addToVariable(variable, value);
    return new FunArrayEnvironment(newSegmentation, newVariables);
  }

  /**
   * Assigns a value to the FunArray at a given index.
   *
   * @param index the index.
   * @param value the value.
   * @return the altered FunArray
   */
  public FunArrayEnvironment assignArrayElement(Expression index, Interval value) {
    var modified = funArray.insert(index, index.increase(1), value);
    return new FunArrayEnvironment(modified, variables());
  }

  @Override
  public String toString() {
    var variablesString = variables.stream()
            .map(Variable::toStringWithValue)
            .collect(Collectors.joining(" "));

    return "A: %s\n%s".formatted(funArray, variablesString);
  }
}
