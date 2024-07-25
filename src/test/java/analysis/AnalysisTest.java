package analysis;

import static org.assertj.core.api.Assertions.assertThat;
import static util.IntervalFunArrayParser.parseIntervalFunArray;
import static util.IntervalFunArrayParser.parseSignFunArray;

import analysis.common.condition.ArrayElementLessThanExpression;
import analysis.common.condition.ExpressionLessThanExpression;
import analysis.common.controlstructure.IfThenElse;
import analysis.common.controlstructure.While;
import analysis.common.statement.AssignArrayElementValueToArrayElement;
import analysis.common.statement.AssignArrayElementValueToVariable;
import analysis.common.statement.AssignVariableValueToArrayElement;
import analysis.common.statement.IncrementVariable;
import base.DomainValueConversion;
import base.interval.Interval;
import base.sign.Sign;
import funarray.EnvState;
import funarray.Expression;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class AnalysisTest {

  /**
   * From Cousot, Cousot and Logozzo (2011) Array in-situ rearrangement example
   */
  @Test
  void cousotExampleTest() {
    var expA = new Expression("a");
    var expB = new Expression("b");

    var funArray = parseIntervalFunArray("{0 a} [-100, 100] {A.length b}");

    var environment = new EnvState<>(Map.of("A", funArray), Map.of(
            "a", Interval.unknown(),
            "b", Interval.unknown(),
            "A.length", Interval.unknown(),
            "0", Interval.of(0),
            "temp", Interval.unknown()));

    var loopCondition = new ExpressionLessThanExpression<Interval, Interval>(new Expression("a"), new Expression("b"));
    var positiveIntCondition = new ArrayElementLessThanExpression<Interval, Interval>("A", new Expression("a"), new Expression("0"), value -> value);

    var program = new While<>(loopCondition,
            new IfThenElse<>(positiveIntCondition,
                    new IncrementVariable<>("a", 1),
                    List.of(
                            new IncrementVariable<>("b", -1),
                            new AssignArrayElementValueToVariable<>("A", expA, "temp", DomainValueConversion::identity),
                            new AssignArrayElementValueToArrayElement<>("A", expB, "A", expA),
                            new AssignVariableValueToArrayElement<>("A", expB, "temp", DomainValueConversion::identity)
                    ),
                    Interval.unreachable()),
            Interval.unreachable());

    var result = program.run(environment);
    var expected = Map.of("A", parseIntervalFunArray("{0} [-100, -1] {a b}? [0, 100] {A.length}?"));
    assertThat(result.resultState().funArray()).isEqualTo(expected);
  }

  @Test
  void cousotExampleWithSignDomainTest() {
    var expA = new Expression("a");
    var expB = new Expression("b");

    var funArray = parseSignFunArray("{0 a} ⊤ {A.length b}");

    var environment = new EnvState<>(Map.of("A", funArray), Map.of(
            "a", Interval.unknown(),
            "b", Interval.unknown(),
            "A.length", Interval.unknown(),
            "0", Interval.of(0),
            "temp", Interval.unknown()));


    var loopCondition = new ExpressionLessThanExpression<Sign, Interval>(new Expression("a"), new Expression("b"));
    var positiveIntCondition = new ArrayElementLessThanExpression<>("A", new Expression("a"), new Expression("0"), DomainValueConversion::convertIntervalToSign);


    var program = new While<>(loopCondition,
            new IfThenElse<>(positiveIntCondition,
                    new IncrementVariable<>("a", 1),
                    List.of(
                            new IncrementVariable<>("b", -1),
                            new AssignArrayElementValueToVariable<>("A", expA, "temp", DomainValueConversion::convertSignToInterval),
                            new AssignArrayElementValueToArrayElement<>("A", expB, "A", expA),
                            new AssignVariableValueToArrayElement<>("A", expB, "temp", DomainValueConversion::convertIntervalToSign)
                    ),
                    new Sign(Set.of())),
            new Sign(Set.of()));

    var result = program.run(environment);
    var expected = Map.of("A", parseSignFunArray("{0} <0 {a b}? ≥0 {A.length}?"));
    assertThat(result.resultState().funArray()).isEqualTo(expected);
  }

  @Test
  public void sortIntoArraysTest() {
    var arraySource = parseIntervalFunArray("{0 s} [-100, 100] {S.length}");
    var arrayPositive = parseIntervalFunArray("{0 p} ⊥ {P.length}");
    var arrayNegative = parseIntervalFunArray("{0 n} ⊥ {N.length}");
    var expS = new Expression("s");
    var expP = new Expression("p");
    var expN = new Expression("n");

    var loopCondition = new ExpressionLessThanExpression<Interval, Interval>(new Expression("s"), new Expression("S.length"));
    var negativeIntCondition = new ArrayElementLessThanExpression<Interval, Interval>("S", new Expression("s"), new Expression("0"), DomainValueConversion::identity);

    var environment = new EnvState<>(Map.of(
            "S", arraySource,
            "P", arrayPositive,
            "N", arrayNegative
    ), Map.of(
            "s", Interval.of(0),
            "p", Interval.of(0),
            "n", Interval.of(0),
            "S.length", Interval.unknown(),
            "0", Interval.of(0)
    ));


    var program = new While<>(loopCondition, List.of(
            new IfThenElse<>(negativeIntCondition, List.of(
                    new AssignArrayElementValueToArrayElement<>("S", expS, "N", expN),
                    new IncrementVariable<>("n", 1)
            ), List.of(
                    new AssignArrayElementValueToArrayElement<>("S", expS, "P", expP),
                    new IncrementVariable<>("p", 1)
            ), Interval.unreachable()),
            new IncrementVariable<>("s", 1)
    ), Interval.unreachable());

    var result = program.run(environment);

    var expected = Map.of("S", parseIntervalFunArray("{0} [-100, 100] {S.length s}?"),
            "P", parseIntervalFunArray("{0} [0, 100] {p}? ⊥ {P.length}?"),
            "N", parseIntervalFunArray("{0} [-100, -1] {n}? ⊥ {N.length}?")
    );
    assertThat(result.resultState().funArray()).isEqualTo(expected);

  }
}
