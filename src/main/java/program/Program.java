package program;

import funarray.Environment;

public interface Program {
  Environment run(Environment startingState);
}
