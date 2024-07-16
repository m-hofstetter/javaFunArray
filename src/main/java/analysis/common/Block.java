package analysis.common;

import base.DomainValue;
import funarray.Environment;
import java.util.List;

public record Block<ELEMENT extends DomainValue<ELEMENT>, VARIABLE extends DomainValue<VARIABLE>>(
        List<Program<VARIABLE, ELEMENT>> statements) implements Program<VARIABLE, ELEMENT> {

  public Block {
    statements = List.copyOf(statements);
  }

  @Override
  public Environment<VARIABLE, ELEMENT> run(Environment<VARIABLE, ELEMENT> startingState) {
    for (Program<VARIABLE, ELEMENT> s : statements) {
      startingState = s.run(startingState);
    }
    return startingState;
  }
}
