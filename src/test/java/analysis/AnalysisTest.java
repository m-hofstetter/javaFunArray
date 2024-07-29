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
import analysis.common.expression.atom.ArrayElement;
import analysis.common.expression.atom.Variable;
import analysis.common.statement.Assign;
import analysis.common.statement.Decrement;
import analysis.common.statement.Increment;
import analysis.interval.IntervalAnalysisContext;
import analysis.signinterval.SignIntervalAnalysisContext;
import funarray.EnvState;
import funarray.NormalExpression;
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

    var funArray = parseIntervalFunArray("{0 a} [-100, 100] {A.length b}");

    var environment = new EnvState<>(Map.of("A", funArray), Map.of(
            "a", Interval.unknown(),
            "b", Interval.unknown(),
            "A.length", Interval.unknown(),
            "0", Interval.of(0),
            "temp", Interval.unknown()));

    var loopCondition = new ExpressionLessThanExpression<Interval, Interval>(new NormalExpression("a"), new NormalExpression("b"));
    var positiveIntCondition = new ArrayElementLessThanExpression<>("A", new NormalExpression("a"), new NormalExpression("0"), context);

    var program = new While<>(loopCondition,
            new IfThenElse<>(positiveIntCondition,
                    new Increment<>(new Variable<>("a"), context),
                    List.of(
                            new Decrement<>(new Variable<>("b"), context),
                            new Assign<>(
                                    new ArrayElement<>("A", new Variable<>("a"), context),
                                    new Variable<>("temp")
                            ),
                            new Assign<>(
                                    new ArrayElement<>("A", new Variable<>("b"), context),
                                    new ArrayElement<>("A", new Variable<>("a"), context)
                            ),
                            new Assign<>(
                                    new Variable<>("temp"),
                                    new ArrayElement<>("A", new Variable<>("b"), context)
                            )
                    ),
                    context),
            context);

    var result = program.run(environment);
    var expected = Map.of("A", parseIntervalFunArray("{0} [-100, -1] {a b}? [0, 100] {A.length}?"));
    System.out.println(result.protocol());
    assertThat(result.resultState().funArray()).isEqualTo(expected);
  }

  @Test
  void cousotExampleWithSignDomainTest() {

    final var context = SignIntervalAnalysisContext.INSTANCE;

    var expA = new NormalExpression("a");
    var expB = new NormalExpression("b");

    var funArray = parseSignFunArray("{0 a} ⊤ {A.length b}");

    var environment = new EnvState<>(Map.of("A", funArray), Map.of(
            "a", Interval.unknown(),
            "b", Interval.unknown(),
            "A.length", Interval.unknown(),
            "0", Interval.of(0),
            "temp", Interval.unknown()));


    var loopCondition = new ExpressionLessThanExpression<Sign, Interval>(new NormalExpression("a"), new NormalExpression("b"));
    var positiveIntCondition = new ArrayElementLessThanExpression<>("A", new NormalExpression("a"), new NormalExpression("0"), context);


    var program = new While<>(loopCondition,
            new IfThenElse<>(positiveIntCondition,
                    new Increment<>(new Variable<>("a"), context),
                    List.of(
                            new Decrement<>(new Variable<>("b"), context),
                            new Assign<>(
                                    new ArrayElement<>("A", new Variable<>("a"), context),
                                    new Variable<>("temp")
                            ),
                            new Assign<>(
                                    new ArrayElement<>("A", new Variable<>("b"), context),
                                    new ArrayElement<>("A", new Variable<>("a"), context)
                            ),
                            new Assign<>(
                                    new Variable<>("temp"),
                                    new ArrayElement<>("A", new Variable<>("b"), context)
                            )
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
    var expS = new NormalExpression("s");
    var expP = new NormalExpression("p");
    var expN = new NormalExpression("n");

    var loopCondition = new ExpressionLessThanExpression<Interval, Interval>(new NormalExpression("s"), new NormalExpression("S.length"));
    var negativeIntCondition = new ArrayElementLessThanExpression<>("S", new NormalExpression("s"), new NormalExpression("0"), context);

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
                    new Assign<>(
                            new ArrayElement<>("S", new Variable<>("s"), context),
                            new ArrayElement<>("N", new Variable<>("n"), context)
                    ),
                    new Increment<>(new Variable<>("n"), context)
            ), List.of(
                    new Assign<>(
                            new ArrayElement<>("S", new Variable<>("s"), context),
                            new ArrayElement<>("P", new Variable<>("p"), context)
                    ),
                    new Increment<>(new Variable<>("p"), context)
            ), context),
            new Increment<>(new Variable<>("s"), context)
    ), context);

    var result = program.run(environment);

    var expected = Map.of("S", parseIntervalFunArray("{0} [-100, 100] {S.length s}?"),
            "P", parseIntervalFunArray("{0} [0, 100] {p}? ⊥ {P.length}?"),
            "N", parseIntervalFunArray("{0} [-100, -1] {n}? ⊥ {N.length}?")
    );
    System.out.println(result.protocol());
    assertThat(result.resultState().funArray()).isEqualTo(expected);

  }
}
