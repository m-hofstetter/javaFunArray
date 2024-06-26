package program.conditions;

import funarray.Environment;

public interface Condition {
  Environment satisfy(Environment input);

  Environment satisfyComplement(Environment input);

  boolean isMet(Environment input);
}
