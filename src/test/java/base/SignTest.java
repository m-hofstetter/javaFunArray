package base;

import static base.sign.Sign.SignElement.*;
import static org.assertj.core.api.Assertions.assertThat;

import base.sign.Sign;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Set;

public class SignTest {
  final Sign POS = new Sign(Set.of(POSITIVE));
  final Sign NEG = new Sign(Set.of(NEGATIVE));
  final Sign ZER = new Sign(Set.of(ZERO));
  final Sign POS_NEG = new Sign(Set.of(POSITIVE, NEGATIVE));
  final Sign POS_ZER = new Sign(Set.of(POSITIVE, ZERO));
  final Sign NEG_ZER = new Sign(Set.of(NEGATIVE, ZERO));
  final Sign ALL = new Sign(Set.of(NEGATIVE, ZERO, POSITIVE));
  final Sign NONE = new Sign(Set.of());

  final List<Sign> ALL_POSSIBLE_VALUES = List.of(POS, NEG, ZER, POS_NEG, POS_ZER, NEG_ZER, ALL, NONE);

  @Test
  public void testJoin() {
    assertThat(POS.join(NEG)).isEqualTo(POS_NEG);
    assertThat(POS.join(ZER)).isEqualTo(POS_ZER);
    assertThat(POS.join(POS)).isEqualTo(POS);
    assertThat(POS.join(NONE)).isEqualTo(POS);
    assertThat(POS.join(NEG_ZER)).isEqualTo(ALL);
  }

  @Test
  public void testMeet() {
    assertThat(POS.meet(NEG)).isEqualTo(NONE);
    assertThat(POS_NEG.meet(NEG)).isEqualTo(NEG);
    assertThat(ALL.meet(NEG)).isEqualTo(NEG);
    assertThat(ALL.meet(NEG_ZER)).isEqualTo(NEG_ZER);
    assertThat(POS.meet(NEG_ZER)).isEqualTo(NONE);
  }

  @Test
  public void testWiden() {
    for (Sign i : ALL_POSSIBLE_VALUES) {
      assertThat(NONE.widen(i)).isEqualTo(i);
      assertThat(POS.widen(i)).isEqualTo(i);
      assertThat(POS_ZER.widen(i)).isEqualTo(i);
      assertThat(ALL.widen(i)).isEqualTo(i);
    }
  }

  @Test
  public void testNarrow() {
    for (Sign i : ALL_POSSIBLE_VALUES) {
      assertThat(NONE.narrow(i)).isEqualTo(i);
      assertThat(POS.narrow(i)).isEqualTo(i);
      assertThat(POS_ZER.narrow(i)).isEqualTo(i);
      assertThat(ALL.narrow(i)).isEqualTo(i);
    }
  }
}
