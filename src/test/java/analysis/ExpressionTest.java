package analysis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static util.IntervalFunArrayParser.parseIntervalFunArray;

import analysis.expression.Expression;
import analysis.expression.NormaliseExpressionException;
import analysis.expression.associative.Addition;
import analysis.expression.associative.Multiplication;
import analysis.expression.atom.ArrayElement;
import analysis.expression.atom.Constant;
import analysis.expression.atom.Variable;
import base.interval.Interval;
import base.util.Abstraction;
import base.util.DomainValueConversion;
import funarray.EnvState;
import funarray.NormalExpression;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class ExpressionTest {


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

  Expression<Interval, Interval> constant = new Constant<>(
          Abstraction::abstractInterval,
          DomainValueConversion::identity,
          Interval.unknown(),
          0
  );

  Expression<Interval, Interval> variable = new Variable<>(
          Abstraction::abstractInterval,
          DomainValueConversion::identity,
          Interval.unknown(),
          "b"
  );

  Expression<Interval, Interval> addition = new Addition<>(
          Abstraction::abstractInterval,
          DomainValueConversion::identity,
          Interval.unknown(),
          Set.of(constant, variable)
  );

  Expression<Interval, Interval> arrayElement = new ArrayElement<>(
          Abstraction::abstractInterval,
          DomainValueConversion::identity,
          Interval.unknown(),
          "A",
          addition
  );

  Expression<Interval, Interval> multiplication = new Multiplication<>(
          Abstraction::abstractInterval,
          DomainValueConversion::identity,
          Interval.unknown(),
          Set.of(constant, variable)
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
