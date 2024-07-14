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

  @Test
  public void testAdd() {
    assertThat(NONE.add(ALL)).isEqualTo(NONE);

    assertThat(POS.add(NONE)).isEqualTo(NONE);
    assertThat(POS.add(POS)).isEqualTo(POS);
    assertThat(POS.add(ZER)).isEqualTo(POS);
    assertThat(POS.add(NEG)).isEqualTo(ALL);
    assertThat(POS.add(POS_ZER)).isEqualTo(POS);
    assertThat(POS.add(POS_NEG)).isEqualTo(ALL);
    assertThat(POS.add(NEG_ZER)).isEqualTo(ALL);
    assertThat(POS.add(ALL)).isEqualTo(ALL);

    assertThat(NEG.add(NONE)).isEqualTo(NONE);
    assertThat(NEG.add(POS)).isEqualTo(ALL);
    assertThat(NEG.add(ZER)).isEqualTo(NEG);
    assertThat(NEG.add(NEG)).isEqualTo(NEG);
    assertThat(NEG.add(POS_ZER)).isEqualTo(ALL);
    assertThat(NEG.add(POS_NEG)).isEqualTo(ALL);
    assertThat(NEG.add(NEG_ZER)).isEqualTo(NEG);
    assertThat(NEG.add(ALL)).isEqualTo(ALL);

    assertThat(ZER.add(NONE)).isEqualTo(NONE);
    assertThat(ZER.add(POS)).isEqualTo(POS);
    assertThat(ZER.add(ZER)).isEqualTo(ZER);
    assertThat(ZER.add(NEG)).isEqualTo(NEG);
    assertThat(ZER.add(POS_ZER)).isEqualTo(POS_ZER);
    assertThat(ZER.add(POS_NEG)).isEqualTo(POS_NEG);
    assertThat(ZER.add(NEG_ZER)).isEqualTo(NEG_ZER);
    assertThat(ZER.add(ALL)).isEqualTo(ALL);
  }

  @Test
  public void testInverse() {
    assertThat(NONE.inverse()).isEqualTo(NONE);
    assertThat(NEG.inverse()).isEqualTo(POS);
    assertThat(ZER.inverse()).isEqualTo(ZER);
    assertThat(POS.inverse()).isEqualTo(NEG);
    assertThat(NEG_ZER.inverse()).isEqualTo(POS_ZER);
    assertThat(POS_ZER.inverse()).isEqualTo(NEG_ZER);
    assertThat(POS_NEG.inverse()).isEqualTo(POS_NEG);
    assertThat(ALL.inverse()).isEqualTo(ALL);
  }

  @Test
  public void testInequalities() {
    assertThat(NONE.satisfyLessEqualThan(NONE)).isEqualTo(NONE);
    assertThat(NONE.satisfyLessEqualThan(NEG)).isEqualTo(NONE);
    assertThat(NONE.satisfyLessEqualThan(POS)).isEqualTo(NONE);
    assertThat(NONE.satisfyLessEqualThan(NEG_ZER)).isEqualTo(NONE);
    assertThat(NONE.satisfyLessEqualThan(ALL)).isEqualTo(NONE);
    assertThat(NEG_ZER.satisfyLessEqualThan(NONE)).isEqualTo(NONE);
    assertThat(NEG_ZER.satisfyLessEqualThan(NEG)).isEqualTo(NEG);
    assertThat(NEG_ZER.satisfyLessEqualThan(POS)).isEqualTo(NEG_ZER);
    assertThat(NEG_ZER.satisfyLessEqualThan(NEG_ZER)).isEqualTo(NEG_ZER);
    assertThat(NEG_ZER.satisfyLessEqualThan(ALL)).isEqualTo(NEG_ZER);

    assertThat(NONE.satisfyGreaterEqualThan(NONE)).isEqualTo(NONE);
    assertThat(NONE.satisfyGreaterEqualThan(NEG)).isEqualTo(NONE);
    assertThat(NONE.satisfyGreaterEqualThan(POS)).isEqualTo(NONE);
    assertThat(NONE.satisfyGreaterEqualThan(NEG_ZER)).isEqualTo(NONE);
    assertThat(NONE.satisfyGreaterEqualThan(ALL)).isEqualTo(NONE);
    assertThat(POS_ZER.satisfyGreaterEqualThan(NONE)).isEqualTo(NONE);
    assertThat(POS_ZER.satisfyGreaterEqualThan(NEG)).isEqualTo(POS_ZER);
    assertThat(POS_ZER.satisfyGreaterEqualThan(POS)).isEqualTo(POS);
    assertThat(POS_ZER.satisfyGreaterEqualThan(POS_ZER)).isEqualTo(POS_ZER);
    assertThat(POS_ZER.satisfyGreaterEqualThan(ALL)).isEqualTo(POS_ZER);

    assertThat(NONE.satisfyLessThan(NONE)).isEqualTo(NONE);
    assertThat(NONE.satisfyLessThan(NEG)).isEqualTo(NONE);
    assertThat(NONE.satisfyLessThan(POS)).isEqualTo(NONE);
    assertThat(NONE.satisfyLessThan(NEG_ZER)).isEqualTo(NONE);
    assertThat(NONE.satisfyLessThan(ALL)).isEqualTo(NONE);
    assertThat(NEG_ZER.satisfyLessThan(NONE)).isEqualTo(NONE);
    assertThat(NEG_ZER.satisfyLessThan(NEG)).isEqualTo(NONE);
    assertThat(NEG_ZER.satisfyLessThan(POS)).isEqualTo(NEG_ZER);
    assertThat(NEG_ZER.satisfyLessThan(POS_ZER)).isEqualTo(NEG);
    assertThat(NEG_ZER.satisfyLessThan(ALL)).isEqualTo(NONE);

    assertThat(NONE.satisfyGreaterThan(NONE)).isEqualTo(NONE);
    assertThat(NONE.satisfyGreaterThan(NEG)).isEqualTo(NONE);
    assertThat(NONE.satisfyGreaterThan(POS)).isEqualTo(NONE);
    assertThat(NONE.satisfyGreaterThan(NEG_ZER)).isEqualTo(NONE);
    assertThat(NONE.satisfyGreaterThan(ALL)).isEqualTo(NONE);
    assertThat(POS_ZER.satisfyGreaterThan(NONE)).isEqualTo(NONE);
    assertThat(POS_ZER.satisfyGreaterThan(NEG)).isEqualTo(POS_ZER);
    assertThat(POS_ZER.satisfyGreaterThan(ZER)).isEqualTo(POS);
    assertThat(POS_ZER.satisfyGreaterThan(POS_ZER)).isEqualTo(NONE);
    assertThat(POS_ZER.satisfyGreaterThan(ALL)).isEqualTo(NONE);
  }
}
