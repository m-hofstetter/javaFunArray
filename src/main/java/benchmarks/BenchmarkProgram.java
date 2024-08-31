package benchmarks;

import java.util.List;
import java.util.Set;

public interface BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> {

  // Statements
  StatementT while_(ConditionT condition, StatementT body);

  StatementT if_(ConditionT condition, StatementT ifBody, StatementT elseBody);

  StatementT block(List<StatementT> statements);

  StatementT assign(AssignableT assignee, ExpressionT value);

  // Analysis utility
  StatementT havoc(AssignableT assignee);

  StatementT assume(ConditionT condition);

  StatementT assert_(ConditionT condition);

  // Expressions
  ExpressionT constant(int value);

  ExpressionT addition(Set<ExpressionT> summands);

  ExpressionT multiplication(Set<ExpressionT> factors);

  ExpressionT subtraction(ExpressionT minuend, ExpressionT subtrahend);

  ExpressionT division(ExpressionT dividend, ExpressionT divisor);

  ExpressionT modulo(ExpressionT dividend, ExpressionT divisor);

  // Assignables
  AssignableT variable(String varName);

  AssignableT arrayElement(String arrayRef, ExpressionT index);

  // Conditions
  ConditionT lessThan(ExpressionT left, ExpressionT right);

  ConditionT lessEqualThan(ExpressionT left, ExpressionT right);

  ConditionT greaterThan(ExpressionT left, ExpressionT right);

  ConditionT greaterEqualThan(ExpressionT left, ExpressionT right);

  ConditionT equalTo(ExpressionT left, ExpressionT right);

  ConditionT unequalTo(ExpressionT left, ExpressionT right);

  ConditionT true_();

  ConditionT false_();
}
