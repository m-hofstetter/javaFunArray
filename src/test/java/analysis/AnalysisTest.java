package analysis;

import static org.assertj.core.api.Assertions.assertThat;
import static util.IntervalFunArrayParser.parseIntervalFunArray;
import static util.IntervalFunArrayParser.parseSignFunArray;

import abstractdomain.interval.value.Interval;
import abstractdomain.sign.value.Sign;
import analysis.common.condition.ArrayElementLessThanExpression;
import analysis.common.condition.ExpressionLessThanExpression;
import analysis.common.controlstructure.IfThenElse;
import analysis.common.controlstructure.While;
import analysis.common.statement.AssignArrayElementValueToArrayElement;
import analysis.common.statement.AssignArrayElementValueToVariable;
import analysis.common.statement.AssignVariableValueToArrayElement;
import analysis.common.statement.IncrementVariable;
import analysis.interval.IntervalAnalysisContext;
import analysis.signinterval.SignIntervalAnalysisContext;
import funarray.EnvState;
import funarray.Expression;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class AnalysisTest {

  /**
   * From Cousot, Cousot and Logozzo (2011) Array in-situ rearrangement example
   */
  @Test
  void cousotExampleTest() {
    final var context = IntervalAnalysisContext.INSTANCE;

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
    var positiveIntCondition = new ArrayElementLessThanExpression<Interval, Interval>("A", new Expression("a"), new Expression("0"), context);

    var program = new While<>(loopCondition,
            new IfThenElse<>(positiveIntCondition,
                    new IncrementVariable<>("a", 1),
                    List.of(
                            new IncrementVariable<>("b", -1),
                            new AssignArrayElementValueToVariable<>("A", expA, "temp", context),
                            new AssignArrayElementValueToArrayElement<>("A", expB, "A", expA),
                            new AssignVariableValueToArrayElement<>("A", expB, "temp", context)
                    ),
                    context),
            context);

    var result = program.run(environment);
    var expected = Map.of("A", parseIntervalFunArray("{0} [-100, -1] {a b}? [0, 100] {A.length}?"));
    assertThat(result.resultState().funArray()).isEqualTo(expected);
  }

  @Test
  void cousotExampleWithSignDomainTest() {

    final var context = SignIntervalAnalysisContext.INSTANCE;

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
    var positiveIntCondition = new ArrayElementLessThanExpression<>("A", new Expression("a"), new Expression("0"), context);


    var program = new While<>(loopCondition,
            new IfThenElse<>(positiveIntCondition,
                    new IncrementVariable<>("a", 1),
                    List.of(
                            new IncrementVariable<>("b", -1),
                            new AssignArrayElementValueToVariable<>("A", expA, "temp", context),
                            new AssignArrayElementValueToArrayElement<>("A", expB, "A", expA),
                            new AssignVariableValueToArrayElement<>("A", expB, "temp", context)
                    ),
                    context),
            context);

    var result = program.run(environment);
    var expected = Map.of("A", parseSignFunArray("{0} <0 {a b}? ≥0 {A.length}?"));
    assertThat(result.resultState().funArray()).isEqualTo(expected);
  }

  @Test
  public void sortIntoArraysTest() {

    final var context = IntervalAnalysisContext.INSTANCE;

    var arraySource = parseIntervalFunArray("{0 s} [-100, 100] {S.length}");
    var arrayPositive = parseIntervalFunArray("{0 p} ⊥ {P.length}");
    var arrayNegative = parseIntervalFunArray("{0 n} ⊥ {N.length}");
    var expS = new Expression("s");
    var expP = new Expression("p");
    var expN = new Expression("n");

    var loopCondition = new ExpressionLessThanExpression<Interval, Interval>(new Expression("s"), new Expression("S.length"));
    var negativeIntCondition = new ArrayElementLessThanExpression<>("S", new Expression("s"), new Expression("0"), context);

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
            ), context),
            new IncrementVariable<>("s", 1)
    ), context);

    var result = program.run(environment);

    var expected = Map.of("S", parseIntervalFunArray("{0} [-100, 100] {S.length s}?"),
            "P", parseIntervalFunArray("{0} [0, 100] {p}? ⊥ {P.length}?"),
            "N", parseIntervalFunArray("{0} [-100, -1] {n}? ⊥ {N.length}?")
    );
    assertThat(result.resultState().funArray()).isEqualTo(expected);

  }
}
