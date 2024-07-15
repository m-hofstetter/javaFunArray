package analysis;

import analysis.common.Block;
import analysis.common.IfThenElse;
import analysis.common.While;
import analysis.common.condition.ArrayElementLessThanExpression;
import analysis.common.condition.ExpressionLessThanExpression;
import analysis.common.statement.AssignArrayElementValueToArrayElement;
import analysis.common.statement.AssignArrayElementValueToVariable;
import analysis.common.statement.AssignVariableValueToArrayElement;
import analysis.common.statement.IncrementVariable;
import base.infint.InfInt;
import base.interval.Interval;
import funarray.Environment;
import funarray.Expression;
import funarray.Variable;
import funarray.util.FunArrayBuilder;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.function.Function;

public class ProgramTest {

  /**
   * From Cousot, Cousot and Logozzo (2011) Array in-situ rearrangement example
   */
  @Test
  void cousotExampleTest() {
    var a = new Variable<>(Interval.of(0), "a");
    var b = new Variable<>(Interval.of(0), "b");
    var length = new Variable<>(Interval.of(1, InfInt.posInf()), "A.length");
    var zero = new Variable<>(Interval.of(0), "0");
    var temp = new Variable<>(Interval.unknown(), "temp");

    var expA = new Expression<>(a);
    var expB = new Expression<>(b);
    var expLength = new Expression<>(length);
    var expZero = new Expression<>(zero);

    var funArray = FunArrayBuilder.buildFunArray()
            .bound(expZero, expA)
            .value(Interval.of(-100, 100))
            .bound(expB, expLength)
            .build();

    var environment = new Environment<Interval, Interval>(funArray, List.of(a, b, length, zero, temp));

    var loopCondition = new ExpressionLessThanExpression<Interval, Interval>(new Expression<>(a), new Expression<>(b));
    var positiveIntCondition = new ArrayElementLessThanExpression<>(new Expression<>(a), new Expression<>(zero, 0), value -> value);
    Function<Interval, Interval> intervalToIntervalConversion = e -> e;

    var program = new While<>(loopCondition,
            new IfThenElse<>(positiveIntCondition,
                    new IncrementVariable<>(a, InfInt.of(1)),
                    new Block<>(
                            new IncrementVariable<>(b, InfInt.of(-1)),
                            new AssignArrayElementValueToVariable<>(expA, temp, intervalToIntervalConversion),
                            new AssignArrayElementValueToArrayElement<>(expB, expA),
                            new AssignVariableValueToArrayElement<>(expB, temp, intervalToIntervalConversion)
                    ),
                    Interval.unreachable()),
            Interval.unreachable());

    program.run(environment);
  }
}
