package program.conditions;

import funarray.Environment;
import funarray.Expression;
import funarray.FunArray;
import java.util.ArrayList;

public record ExpressionLessEqualThanExpression(Expression left,
                                                Expression right) implements Condition {
  @Override
  public Environment satisfy(Environment input) {

    var bounds = new ArrayList<>(input.funArray().bounds());

    Integer indexLeftExpression = null;
    Integer indexRightExpression = null;

    for (int i = 0; i < bounds.size(); i++) {
      if (bounds.get(i).expressionEquals(left)) {
        indexLeftExpression = i;
      }
      if (bounds.get(i).expressionEquals(right)) {
        indexRightExpression = i;
      }
    }

    if (indexLeftExpression == null || indexRightExpression == null) {
      return input;
    }

    if (indexLeftExpression <= indexRightExpression) {
      return input;
    }

    var leftBound = bounds.get(indexLeftExpression);
    var rightBound = bounds.get(indexRightExpression);

    bounds.subList(indexLeftExpression, indexRightExpression + 1).clear();

    bounds.add(indexLeftExpression, rightBound);
    bounds.add(indexRightExpression, leftBound);

    var values = new ArrayList<>(input.funArray().values());
    values.subList(indexLeftExpression, indexRightExpression - 1).clear();

    var emptiness = new ArrayList<>(input.funArray().emptiness());
    emptiness.subList(indexLeftExpression, indexRightExpression - 1).clear();

    var updatedFunArray = new FunArray(bounds, values, emptiness);

    return new Environment(updatedFunArray, input.variables());
  }

  @Override
  public Environment satisfyComplement(Environment input) {
    var bounds = new ArrayList<>(input.funArray().bounds());

    Integer indexLeftExpression = null;
    Integer indexRightExpression = null;

    for (int i = 0; i < bounds.size(); i++) {
      if (bounds.get(i).expressionEquals(left)) {
        indexLeftExpression = i;
      }
      if (bounds.get(i).expressionEquals(right)) {
        indexRightExpression = i;
      }
    }

    if (indexLeftExpression == null || indexRightExpression == null) {
      return input;
    }

    if (indexLeftExpression > indexRightExpression) {
      return input;
    }

    var leftBound = bounds.get(indexLeftExpression);
    var rightBound = bounds.get(indexRightExpression);

    bounds.subList(indexLeftExpression, indexRightExpression + 1).clear();

    bounds.add(indexLeftExpression, rightBound);
    bounds.add(indexRightExpression, leftBound);

    var values = new ArrayList<>(input.funArray().values());
    values.subList(indexLeftExpression, indexRightExpression - 1).clear();

    var emptiness = new ArrayList<>(input.funArray().emptiness());
    emptiness.subList(indexLeftExpression, indexRightExpression - 1).clear();

    var updatedFunArray = new FunArray(bounds, values, emptiness);

    return new Environment(updatedFunArray, input.variables());
  }

  @Override
  public boolean isMet(Environment input) {
    return false;
  }
}
