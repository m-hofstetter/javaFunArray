package funarray;

import base.infint.InfInt;
import base.interval.Interval;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * The environment in which an abstract analysis of an Array can be done, consisting of the abstract
 * representation of the array in the form of Cousot et al's FunArray and a variable environment.
 *
 * @param funArray  the FunArray
 * @param variables the variable environment
 */
public record Environment(FunArray funArray, List<Variable> variables) {

  private static final String CONSOLE_COLOR_CYAN = "\033[0;36m";
  private static final String CONSOLE_COLOR_RESET = "\033[0m";

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
    return assignVariable(variable, expression.calculate());
  }

  public Environment assignVariable(Variable variable, Interval interval) {
    var modifiedFunArray = funArray.assignVariable(variable, interval);
    var newVariables = new ArrayList<>(variables);

    newVariables.remove(variable);
    newVariables.add(new Variable(interval, variable.name()));

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
    var modified = funArray.insert(index, value);
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

  public void consolePrintOut() {
    System.out.printf("%s%s%s%n", CONSOLE_COLOR_CYAN, toString(), CONSOLE_COLOR_RESET);
  }

  public Environment assume(UnaryOperator<Environment> assumption, UnaryOperator<Environment> reverseAssumption, UnaryOperator<Environment> modification) {
    var thisUnderAssumption = assumption.apply(this);
    var thisUnderAssumptionAndModified = modification.apply(thisUnderAssumption);
    var thisUnderReverseAssumption = reverseAssumption.apply(this);

    return thisUnderAssumptionAndModified.join(thisUnderReverseAssumption);
  }

  public Environment join(Environment other) {
    var joinedFunArray = funArray.join(other.funArray);
    return new Environment(joinedFunArray, variables);
    //TODO: join variables
  }

  public Environment widen(Environment other) {
    var widenedFunArray = funArray.widen(other.funArray);
    return new Environment(widenedFunArray, variables);
    //TODO: proper widening
  }
}
