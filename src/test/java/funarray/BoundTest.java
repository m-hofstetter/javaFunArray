package funarray;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.Test;

public class BoundTest {

  static final Expression A = new Expression(new VariableReference("A"));
  static final Expression B = new Expression(new VariableReference("B"));

  static final Set<Expression> SET_A = Set.of(A);
  static final Set<Expression> SET_AB = Set.of(A, B);
  static final Set<Expression> SET_B = Set.of(B);

  @SuppressWarnings("EqualsWithItself")
  @Test
  void equalsTest() {
    assertThat(
            new Bound(SET_A).equals(new Bound(SET_A))
    ).isTrue();

    assertThat(
            new Bound(SET_A).equals(new Bound(SET_B))
    ).isFalse();

    assertThat(
            new Bound(SET_A).equals(new Bound(SET_AB))
    ).isFalse();
  }

  @Test
  void differenceTest() {
    assertThat(
            new Bound(SET_AB).difference(new Bound(SET_A))
    ).isEqualTo(
            new Bound(SET_B)
    );
  }

  @Test
  void relativeComplementTest() {
    assertThat(
            new Bound(SET_A).relativeComplement(new Bound(SET_AB))
    ).isEqualTo(
            new Bound(SET_B)
    );
  }
}
