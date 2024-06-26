package program;

import base.infint.InfInt;
import funarray.Environment;
import funarray.Expression;
import org.junit.jupiter.api.Test;
import program.conditions.ArrayElementLessEqualThan;

public class ProgramTest {
  @Test
  void lessEqualThanTest() {
    var env = new Environment(new Expression(5));
    var cond = new ArrayElementLessEqualThan(InfInt.of(3), new Expression(4));


    var test = new Block(
            new IfThenElse(
                    cond,
                    new Print(),
                    new Print()
            ),
            new Print()
    );

    test.run(env);

  }
}
