package analysis;

import static org.assertj.core.api.Assertions.assertThat;
import static util.IntervalFunArrayParser.parseIntervalFunArray;

import abstractdomain.interval.value.Interval;
import analysis.common.AnalysisContext;
import analysis.common.expression.associative.Addition;
import analysis.common.expression.associative.Multiplication;
import analysis.common.expression.atom.ArrayElement;
import analysis.common.expression.atom.Constant;
import analysis.common.expression.atom.Variable;
import analysis.common.expression.nonassociative.Division;
import analysis.common.expression.nonassociative.Modulo;
import analysis.common.expression.nonassociative.Subtraction;
import analysis.interval.IntervalAnalysisContext;
import funarray.NormalExpression;
import funarray.State;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class ExpressionTest {

  AnalysisContext<Interval, Interval> context = IntervalAnalysisContext.INSTANCE;


  State<Interval, Interval> environment = new State<>(
          Map.of(
                  "A", parseIntervalFunArray("{0 a} [-100, 100] {b} [-100, 100] {c A.length}")
          ),
          Map.of(
                  "a", Interval.unknown(),
                  "b", Interval.of(5, 10),
                  "c", Interval.of(20, 20),
                  "A.length", Interval.unknown(),
                  "0", Interval.of(0),
                  "temp", Interval.unknown()
          ), context
  );

  @Test
  public void testConstant() {
    var constant = new Constant<>(0, context);

    assertThat(constant.toString()).isEqualTo("0");
    assertThat(constant.evaluate(environment)).isEqualTo(Interval.of(0));
    assertThat(constant.normalise(environment)).isEmpty();
  }

  @Test
  public void testVariable() {
    var variable = new Variable<Interval, Interval>("b", context);

    assertThat(variable.toString()).isEqualTo("b");
    assertThat(variable.evaluate(environment)).isEqualTo(Interval.of(5, 10));
    assertThat(variable.normalise(environment)).containsExactly(new NormalExpression("b", 0));
  }

  @Test
  public void testArrayElement() {
    var arrayElement = new ArrayElement<>(
            "A",
            new Addition<>(Set.of(
                    new Variable<>("b", context),
                    new Constant<>(0, context)
            ), context),
            context
    );

    assertThat(arrayElement.toString()).isEqualTo("A[b + 0]");
    assertThat(arrayElement.evaluate(environment)).isEqualTo(Interval.of(-100, 100));
    assertThat(arrayElement.normalise(environment)).isEmpty();
  }

  @Test
  public void testAddition() {
    var addition = new Addition<>(Set.of(
            new Variable<>("b", context),
            new Constant<>(0, context)
    ), context);

    assertThat(addition.toString()).isEqualTo("b + 0");
    assertThat(addition.evaluate(environment)).isEqualTo(Interval.of(5, 10));
    assertThat(addition.normalise(environment)).containsExactly(new NormalExpression("b", 0));
  }

  @Test
  public void testMultiplication() {
    var multiplication = new Multiplication<>(Set.of(
            new Variable<>("b", context),
            new Constant<>(3, context)
    ), context);

    assertThat(multiplication.toString()).isEqualTo("b * 3");
    assertThat(multiplication.evaluate(environment)).isEqualTo(Interval.of(15, 30));
    assertThat(multiplication.normalise(environment)).isEmpty();

    var normalisableMultiplication = new Multiplication<>(Set.of(
            new Variable<>("c", context),
            new Constant<>(5, context)
    ), context);

    assertThat(normalisableMultiplication.toString()).isEqualTo("c * 5");
    assertThat(normalisableMultiplication.evaluate(environment)).isEqualTo(Interval.of(100));
    assertThat(normalisableMultiplication.normalise(environment)).containsExactly(new NormalExpression("0", 100));
  }

  @Test
  public void testSubtraction() {
    var subtraction = new Subtraction<>(
            new Variable<>("b", context),
            new Constant<>(3, context),
            context
    );

    assertThat(subtraction.toString()).isEqualTo("b - 3");
    assertThat(subtraction.evaluate(environment)).isEqualTo(Interval.of(2, 7));
    assertThat(subtraction.normalise(environment)).containsExactly(new NormalExpression("b", -3));
  }

  @Test
  public void testModulo() {
    var modulo = new Modulo<>(
            new Variable<>("b", context),
            new Constant<>(3, context),
            context
    );

    assertThat(modulo.toString()).isEqualTo("b % 3");
    assertThat(modulo.evaluate(environment)).isEqualTo(Interval.of(0, 2));
    assertThat(modulo.normalise(environment)).isEmpty();
  }

  @Test
  public void testDivision() {
    var division = new Division<>(
            new Variable<>("b", context),
            new Constant<>(3, context),
            context
    );

    assertThat(division.toString()).isEqualTo("b / 3");
    assertThat(division.evaluate(environment)).isEqualTo(Interval.of(1, 3));
    assertThat(division.normalise(environment)).isEmpty();
  }
}
