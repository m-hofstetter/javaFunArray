package base;

import static org.assertj.core.api.Assertions.assertThat;

import analysis.expression.atom.AtomExpressionConstant;
import analysis.expression.atom.AtomExpressionVariable;
import base.interval.Interval;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class ExpressionTest {

  @Test
  public void testConstantAtom() {
    var constant = new AtomExpressionConstant(3);
    assertThat(constant.toString()).isEqualTo("3");
    assertThat(constant.evaluate(Map.of(), DomainValueConversion::constantIntervalValue)).isEqualTo(Interval.of(3));
  }

  @Test
  public void testVariableAtom() {
    var constant = new AtomExpressionVariable("a");
    assertThat(constant.toString()).isEqualTo("a");
    assertThat(constant.evaluate(Map.of("a", Interval.of(0, 10)), DomainValueConversion::constantIntervalValue)).isEqualTo(Interval.of(0, 10));
  }

  @Test
  public void additionTest() {
  }
}
