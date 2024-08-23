package analysis.common.builder;

import java.util.List;
import java.util.Set;

public interface BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> {

  // Statements
  StatementT while_(ConditionT condition, StatementT body);

  StatementT if_(ConditionT condition, StatementT ifBody, StatementT elseBody);

  StatementT block(List<StatementT> statements);

  StatementT assign(AssignableT assignee, ExpressionT value);

  // Expressions
  ExpressionT arrayElement(String arrayRef, ExpressionT index);

  ExpressionT constant(int value);

  ExpressionT variable(String varName);

  ExpressionT addition(Set<ExpressionT> summands);

  ExpressionT multiplication(Set<ExpressionT> factors);

  ExpressionT subtraction(ExpressionT minuend, ExpressionT subtrahend);

  ExpressionT division(ExpressionT dividend, ExpressionT divisor);

  ExpressionT modulo(ExpressionT dividend, ExpressionT divisor);

  // Conditions
  ConditionT lessThan(ExpressionT left, ExpressionT right);

  ConditionT lessEqualThan(ExpressionT left, ExpressionT right);

  ConditionT greaterThan(ExpressionT left, ExpressionT right);

  ConditionT greaterEqualThan(ExpressionT left, ExpressionT right);

  ConditionT equalTo(ExpressionT left, ExpressionT right);

  ConditionT unequalTo(ExpressionT left, ExpressionT right);
}
