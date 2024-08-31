package benchmarks;

import java.util.List;

public interface Benchmark {
  <ExpressionT, ConditionT, StatementT, AssignableT extends ExpressionT> StatementT statement(BenchmarkProgram<ExpressionT, ConditionT, StatementT, AssignableT> program);

  List<String> integerVariables();

  List<String> arrayVariables();
}
