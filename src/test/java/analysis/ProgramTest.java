package analysis;

import analysis.common.Block;
import analysis.common.IfThenElse;
import analysis.common.While;
import analysis.interval.AssignArrayElementValueToArrayElement;
import analysis.interval.AssignArrayElementValueToVariable;
import analysis.interval.AssignVariableValueToArrayElement;
import analysis.interval.IncrementVariable;
import analysis.interval.condition.ArrayElementLessEqualThanConstant;
import analysis.interval.condition.ExpressionLessThanExpression;
import base.infint.InfInt;
import base.interval.Interval;
import funarray.Environment;
import funarray.Expression;
import funarray.Variable;
import funarray.util.FunArrayBuilder;
import org.junit.jupiter.api.Test;
import java.util.List;

public class ProgramTest {

  /**
   * From Cousot, Cousot and Logozzo (2011) Array in-situ rearrangement example
   */
  @Test
  void cousotExampleTest() {
    var a = new Variable(Interval.of(0), "a");
    var b = new Variable(Interval.of(0), "b");
    var length = new Variable(Interval.of(1, InfInt.posInf()), "A.length");
    var zero = new Variable(Interval.of(0), "0");
    var temp = new Variable(Interval.unknown(), "temp");

    var expA = new Expression(a);
    var expB = new Expression(b);
    var expLength = new Expression(length);
    var expZero = new Expression(zero);

    var funArray = FunArrayBuilder.buildFunArray()
            .bound(expZero, expA)
            .value(Interval.of(-100, 100))
            .bound(expB, expLength)
            .build();

    var environment = new Environment(funArray, List.of(a, b, length, zero, temp));

    var loopCondition = new ExpressionLessThanExpression(new Expression(a), new Expression(b));
    var positiveIntCondition = new ArrayElementLessEqualThanConstant(InfInt.of(0), new Expression(a));

    var program = new While(loopCondition,
            new IfThenElse(positiveIntCondition,
                    new IncrementVariable(a, InfInt.of(1)),
                    new Block(
                            new IncrementVariable(b, InfInt.of(-1)),
                            new AssignArrayElementValueToVariable(expA, temp),
                            new AssignArrayElementValueToArrayElement(expB, expA),
                            new AssignVariableValueToArrayElement(expB, temp)

                    )
            )
    );

    program.run(environment);
  }
}
