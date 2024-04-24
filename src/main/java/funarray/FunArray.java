package funarray;

import base.Interval;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A FunArray to represent an Array in abstract interpretation static analysis. See Patrick Cousot,
 * Radhia Cousot, and Francesco Logozzo. 2011. A parametric segmentation functor for fully automatic
 * and scalable array content analysis. SIGPLAN Not. 46, 1 (January 2011), 105–118. <a
 * href="https://doi.org/10.1145/1925844.1926399">https://doi.org/10.1145/1925844.1926399</a>
 *
 * @param segmentation the FunArray's segmentation
 * @param variables    the FunArray's variables
 */
public record FunArray(Segmentation segmentation, List<Variable> variables) {

  public FunArray {
    variables = List.copyOf(variables);
  }

  public FunArray(Expression length) {
    this(new Segmentation(length, false), List.of(length.variable()));
  }

  /**
   * Adds to a variable. See Cousot et al. 2011, Chapter 11.6.
   *
   * @param variable the variable
   * @param value    the amount by which to increase it
   * @return the altered FunArray
   */
  public FunArray addToVariable(Variable variable, int value) {
    var newVariable = new Variable(variable.value().add(value), variable.name());
    var newVariables = new ArrayList<>(variables);
    newVariables.remove(variable);
    newVariables.add(newVariable);
    var newSegmentation = segmentation.addToVariable(variable, value);
    return new FunArray(newSegmentation, newVariables);
  }

  public FunArray insert(Expression index, Interval value) {
    var modified = segmentation.insert(index, index.increase(1), value);
    return new FunArray(modified, variables());
  }

  @Override
  public String toString() {
    var variablesString = variables.stream()
        .map(Variable::toStringWithValue)
        .collect(Collectors.joining(" "));

    return "A: %s\n%s".formatted(segmentation, variablesString);
  }
}
