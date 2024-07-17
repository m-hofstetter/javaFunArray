package analysis;

import analysis.common.condition.ArrayElementLessThanExpression;
import analysis.common.condition.ExpressionLessThanExpression;
import analysis.common.controlstructure.IfThenElse;
import analysis.common.controlstructure.While;
import analysis.common.statement.AssignArrayElementValueToArrayElement;
import analysis.common.statement.AssignArrayElementValueToVariable;
import analysis.common.statement.AssignVariableValueToArrayElement;
import analysis.common.statement.IncrementVariable;
import base.infint.InfInt;
import base.interval.Interval;
import funarray.Environment;
import funarray.Expression;
import funarray.VariableReference;
import funarray.util.FunArrayBuilder;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ProgramTest {

  /**
   * From Cousot, Cousot and Logozzo (2011) Array in-situ rearrangement example
   */
  @Test
  void cousotExampleTest() {
    var a = new VariableReference("a");
    var b = new VariableReference("b");
    var length = new VariableReference("A.length");
    var zero = new VariableReference("0");
    var temp = new VariableReference("temp");

    var expA = new Expression(a);
    var expB = new Expression(b);
    var expLength = new Expression(length);
    var expZero = new Expression(zero);

    var funArray = FunArrayBuilder.buildFunArray()
            .bound(expZero, expA)
            .value(Interval.of(-100, 100))
            .bound(expB, expLength)
            .build();

    var environment = new Environment<Interval, Interval>(funArray, Map.of(
            a, Interval.unknown(),
            b, Interval.unknown(),
            length, Interval.unknown(),
            zero, Interval.of(0),
            temp, Interval.unknown()));

    var loopCondition = new ExpressionLessThanExpression<Interval, Interval>(new Expression(a), new Expression(b));
    var positiveIntCondition = new ArrayElementLessThanExpression(new Expression(a), new Expression(zero, 0), value -> value);
    Function<Interval, Interval> intervalToIntervalConversion = e -> e;

    var program = new While<>(loopCondition,
            new IfThenElse<>(positiveIntCondition,
                    new IncrementVariable<>(a, InfInt.of(1)),
                    List.of(
                            new IncrementVariable<>(b, InfInt.of(-1)),
                            new AssignArrayElementValueToVariable<>(expA, temp, intervalToIntervalConversion),
                            new AssignArrayElementValueToArrayElement<>(expB, expA),
                            new AssignVariableValueToArrayElement<>(expB, temp, intervalToIntervalConversion)
                    ),
                    Interval.unreachable()),
            Interval.unreachable());

    System.out.println(program.run(environment).protocol());
  }
}
