package analysis.common.statement;

import static analysis.common.AnalysisResult.AssertionResult.noAssert;

import abstractdomain.DomainValue;
import analysis.common.Analysis;
import analysis.common.AnalysisContext;
import analysis.common.AnalysisResult;
import analysis.common.expression.Expression;
import funarray.Bound;
import funarray.FunArray;
import funarray.NormalExpression;
import funarray.state.ReachableState;
import funarray.state.State;
import java.util.HashMap;
import java.util.HashSet;

public record InitArray<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(String array,
                                                  Expression<ElementT, VariableT> length,
                                                  AnalysisContext<ElementT, VariableT> context) implements Analysis<ElementT, VariableT> {

  public static final String PROTOCOL_TEMPLATE = "INIT %s[%s]";

  @Override
  public AnalysisResult<ElementT, VariableT> run(State<ElementT, VariableT> state) {
    if (state instanceof ReachableState<ElementT, VariableT> reachableState) {
      var modifiedFunArrays = new HashMap<>(reachableState.arrays());
      var lengthExpressions = new HashSet<>(length.normalise(reachableState));
      lengthExpressions.add(new NormalExpression(array + ".length"));

      var newArray = new FunArray<>(
              new Bound(lengthExpressions),
              context.getElementDomain().getUnknown()
      );

      modifiedFunArrays.put(array, newArray);
      var modifiedState = new ReachableState<>(modifiedFunArrays, state.variables(), context);


      var protocol = PROTOCOL_TEMPLATE.formatted(array, length);
      return new AnalysisResult<>(modifiedState, protocol, noAssert());
    }
    return new AnalysisResult<>(state, "", noAssert());
  }
}
