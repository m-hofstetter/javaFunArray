package analysis;

import static base.sign.Sign.SignElement.*;
import static org.assertj.core.api.Assertions.assertThat;
import static util.IntervalFunArrayParser.parseIntervalFunArray;

import analysis.common.condition.ArrayElementLessThanExpression;
import analysis.common.condition.ExpressionLessThanExpression;
import analysis.common.controlstructure.IfThenElse;
import analysis.common.controlstructure.While;
import analysis.common.statement.AssignArrayElementValueToArrayElement;
import analysis.common.statement.AssignArrayElementValueToVariable;
import analysis.common.statement.AssignVariableValueToArrayElement;
import analysis.common.statement.IncrementVariable;
import base.DomainValueConversion;
import base.infint.InfInt;
import base.interval.Interval;
import base.sign.Sign;
import funarray.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    var funArray = parseIntervalFunArray("{0 a} [-100, 100] {A.length b}");

    var environment = new Environment<>(funArray, Map.of(
            a, Interval.unknown(),
            b, Interval.unknown(),
            length, Interval.unknown(),
            zero, Interval.of(0),
            temp, Interval.unknown()));

    var loopCondition = new ExpressionLessThanExpression<Interval, Interval>(new Expression(a), new Expression(b));
    var positiveIntCondition = new ArrayElementLessThanExpression<Interval, Interval>(new Expression(a), new Expression(zero, 0), value -> value);
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

    var result = program.run(environment);
    var expected = parseIntervalFunArray("{0} [-100, -1] {a b}? [0, 100] {A.length}?");
    assertThat(result.resultState().funArray()).isEqualTo(expected);
  }

  @Test
  void cousotSignExampleTest() {
    var a = new VariableReference("a");
    var b = new VariableReference("b");
    var length = new VariableReference("A.length");
    var zero = new VariableReference("0");
    var temp = new VariableReference("temp");

    var expA = new Expression(a);
    var expB = new Expression(b);
    var expLength = new Expression(length);
    var expZero = new Expression(zero);

    var funArray = new FunArray<Sign>(List.of(
            new Bound(expZero, expA), new Bound(expB, expLength)
    ), List.of(
            new Sign(Set.of(NEGATIVE, ZERO, POSITIVE))
    ), List.of(
            false
    ));

    var environment = new Environment<>(funArray, Map.of(
            a, Interval.unknown(),
            b, Interval.unknown(),
            length, Interval.unknown(),
            zero, Interval.of(0),
            temp, Interval.unknown()));


    var loopCondition = new ExpressionLessThanExpression<Sign, Interval>(new Expression(a), new Expression(b));
    var positiveIntCondition = new ArrayElementLessThanExpression<Sign, Interval>(new Expression(a), new Expression(zero, 0), DomainValueConversion::convertIntervalToSign);


    var program = new While<Sign, Interval>(loopCondition,
            new IfThenElse<Sign, Interval>(positiveIntCondition,
                    new IncrementVariable<Sign, Interval>(a, InfInt.of(1)),
                    List.of(
                            new IncrementVariable<Sign, Interval>(b, InfInt.of(-1)),
                            new AssignArrayElementValueToVariable<Sign, Interval>(expA, temp, DomainValueConversion::convertSignToInterval),
                            new AssignArrayElementValueToArrayElement<Sign, Interval>(expB, expA),
                            new AssignVariableValueToArrayElement<Sign, Interval>(expB, temp, DomainValueConversion::convertIntervalToSign)
                    ),
                    new Sign(Set.of())),
            new Sign(Set.of()));

    var result = program.run(environment);
    var expected = new FunArray<Sign>(List.of(
            new Bound(expZero), new Bound(expA, expB), new Bound(expLength)
    ), List.of(
            new Sign(Set.of(NEGATIVE)), new Sign(Set.of(ZERO, POSITIVE))
    ), List.of(
            true, true
    ));
    assertThat(result.resultState().funArray()).isEqualTo(expected);
  }
}
