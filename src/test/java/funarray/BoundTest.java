package funarray;

import static org.assertj.core.api.Assertions.assertThat;

import base.infint.InfInt;
import org.junit.jupiter.api.Test;
import java.util.Set;

public class BoundTest {

  static final Expression A = new Expression(new Variable(0, "A"), InfInt.of(0));
  static final Expression B = new Expression(new Variable(0, "B"), InfInt.of(0));

  static final Set<Expression> SET_A = Set.of(A);
  static final Set<Expression> SET_AB = Set.of(A, B);
  static final Set<Expression> SET_B = Set.of(B);

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
  void containsSubsetTest() {
    assertThat(
            new Bound(SET_A).containsSubset(new Bound(SET_A))
    ).isTrue();

    assertThat(
            new Bound(SET_A).containsSubset(new Bound(SET_B))
    ).isFalse();

    assertThat(
            new Bound(SET_AB).containsSubset(new Bound(SET_A))
    ).isTrue();

    assertThat(
            new Bound(SET_A).containsSubset(new Bound(SET_AB))
    ).isFalse();
  }

  @Test
  void complementTest() {
    assertThat(
            new Bound(SET_AB).getComplementBound(new Bound(SET_A))
    ).isEqualTo(
            new Bound(SET_B)
    );
  }
}
