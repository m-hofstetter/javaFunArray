package program;

import base.interval.Interval;
import funarray.*;
import org.junit.jupiter.api.Test;
import program.conditions.ExpressionLessEqualThanExpression;
import java.util.List;

public class ProgramTest {
  @Test
  void lessEqualThanTest() {

    var x = new Variable(0, "x");
    var y = new Variable(0, "y");
    var z = new Variable(0, "z");

    var expX = new Expression(x);
    var expY = new Expression(y);
    var expZ = new Expression(z);

    var bounds = List.of(
            new Bound(expX),
            new Bound(expY),
            new Bound(expZ)
    );

    var values = List.of(
            Interval.unknown(),
            Interval.unknown()
    );

    var emptiness = List.of(true, true);

    var funArray = new FunArray(bounds, values, emptiness);
    var env = new Environment(funArray, List.of(x, y, z));

    var condition = new ExpressionLessEqualThanExpression(expY, expZ);

    var program = new Block(
            new While(
                    condition,
                    new Print()
            ),
            new Print()
    );

    program.run(env);

  }
}
