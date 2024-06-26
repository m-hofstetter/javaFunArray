package funarray;

import base.infint.InfInt;
import base.interval.Interval;
import funarray.util.FunArrayBuilder;
import org.junit.jupiter.api.Test;
import java.util.List;

public class EnvironmentTest {

  @Test
  void dutchNationalFlagProblemTest() {
    var length = new Variable(Interval.unknown(), "A.length");
    var r = new Variable(0, 0, "r");
    var w = new Variable(Interval.unknown(), "w");
    var b = new Variable(Interval.unknown(), "b");

    var funArray = FunArrayBuilder.buildFunArray()
            .bound(r, Variable.ZERO_VALUE)
            .value(Interval.unknown())
            .bound(w, b)
            .value(Interval.unknown())
            .bound(length)
            .build();

    var env = new Environment(funArray, List.of(r, w, b));

    System.out.println(env);

    env = env.assume(environment -> {
              var element = environment.funArray().get(new Expression(w));
              var negative = Interval.of(InfInt.negInf(), -1);
              var met = element.meet(negative);
              return environment.assignArrayElement(new Expression(w), met);
            },
            environment -> {
              var element = environment.funArray().get(new Expression(w));
              var notNegative = Interval.of(0, InfInt.posInf());
              var met = element.meet(notNegative);
              return environment.assignArrayElement(new Expression(w), met);
            },
            environment -> {
              var temp = environment.funArray().get(new Expression(r));
              environment = environment
                      .assignArrayElement(new Expression(r), environment.getArrayElement(new Expression(w)))
                      .assignArrayElement(new Expression(w), temp)
                      .addToVariable(r, InfInt.of(1));
              System.out.println(environment);
              return environment;
            });


    System.out.println(env);
  }
}
