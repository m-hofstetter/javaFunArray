package analysis.common.statement;

import abstractdomain.DomainValue;
import analysis.common.Analysis;
import analysis.common.AnalysisContext;
import analysis.common.AnalysisResult;
import analysis.common.expression.Assignable;
import analysis.common.expression.atom.ArrayElement;
import analysis.common.expression.atom.Variable;
import funarray.State;

public record Havoc<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        Assignable<ElementT, VariableT> assignee,
        AnalysisContext<ElementT, VariableT> context) implements Analysis<ElementT, VariableT> {

  public static final String PROTOCOL_TEMPLATE = """
          HAVOC %s
          \033[0;36m%s\033[0m""";

  @Override
  public AnalysisResult<ElementT, VariableT> run(State<ElementT, VariableT> startingState) {
    var modified = switch (assignee) {
      case ArrayElement<ElementT, VariableT> arrayElement -> startingState.assignArrayElement(
              arrayElement.arrayRef(),
              arrayElement.index().normalise(startingState),
              context.getElementDomain().getUnknown()
      );
      case Variable<ElementT, VariableT> variable -> startingState.assignVariable(
              variable.variableRef(),
              context.getVariableDomain().getUnknown()
      );
      case null, default -> throw new IllegalStateException();
    };

    var protocol = PROTOCOL_TEMPLATE.formatted(assignee, modified);
    return new AnalysisResult<>(modified, protocol);
  }
}
