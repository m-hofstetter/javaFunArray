package analysis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static util.IntervalFunArrayParser.parseIntervalFunArray;

import abstractdomain.interval.value.Interval;
import analysis.common.AnalysisContext;
import analysis.common.expression.Expression;
import analysis.common.expression.NormaliseExpressionException;
import analysis.common.expression.associative.Addition;
import analysis.common.expression.associative.Multiplication;
import analysis.common.expression.atom.ArrayElement;
import analysis.common.expression.atom.Constant;
import analysis.common.expression.atom.Variable;
import analysis.interval.IntervalAnalysisContext;
import funarray.EnvState;
import funarray.NormalExpression;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class ExpressionTest {

  AnalysisContext<Interval, Interval> context = IntervalAnalysisContext.INSTANCE;


  EnvState<Interval, Interval> environment = new EnvState<>(
          Map.of(
                  "A", parseIntervalFunArray("{0 a} [-100, 100] {b} [-100, 100] {c A.length}")
          ),
          Map.of(
                  "a", Interval.unknown(),
                  "b", Interval.of(5, 10),
                  "c", Interval.unknown(),
                  "A.length", Interval.unknown(),
                  "0", Interval.of(0),
                  "temp", Interval.unknown()
          )
  );

  Expression<Interval, Interval> constant = new Constant<>(0, context);

  Expression<Interval, Interval> variable = new Variable<>("b", context);

  Expression<Interval, Interval> addition = new Addition<>(Set.of(constant, variable), context);

  Expression<Interval, Interval> arrayElement = new ArrayElement<>(
          "A",
          addition,
          context
  );

  Expression<Interval, Interval> multiplication = new Multiplication<>(
          Set.of(constant, variable),
          context
  );


  @Test
  public void testConstant() throws Exception {
    assertThat(constant.toString()).isEqualTo("0");
    assertThat(constant.evaluate(environment)).isEqualTo(Interval.of(0));
    assertThat(constant.normalise(environment)).isEqualTo(new NormalExpression("0", 0));
  }

  @Test
  public void testVariable() throws Exception {
    assertThat(variable.toString()).isEqualTo("b");
    assertThat(variable.evaluate(environment)).isEqualTo(Interval.of(5, 10));
    assertThat(variable.normalise(environment)).isEqualTo(new NormalExpression("b", 0));
  }

  @Test
  public void testArrayElement() throws Exception {
    assertThat(arrayElement.toString()).isEqualTo("A[b + 0]");
    assertThat(arrayElement.evaluate(environment)).isEqualTo(Interval.of(-100, 100));
    assertThatExceptionOfType(NormaliseExpressionException.class).isThrownBy(() -> arrayElement.normalise(environment));
  }

  @Test
  public void testAddition() throws Exception {
    assertThat(addition.toString()).isEqualTo("b + 0");
    assertThat(addition.evaluate(environment)).isEqualTo(Interval.of(5, 10));
    assertThat(addition.normalise(environment)).isEqualTo(new NormalExpression("b", 0));
  }

  @Test
  public void testMultiplication() throws Exception {
    assertThat(multiplication.toString()).isEqualTo("b * 0");
    assertThat(multiplication.evaluate(environment)).isEqualTo(Interval.of(0, 0));
    assertThat(multiplication.normalise(environment)).isEqualTo(new NormalExpression("0", 0));
  }
}
