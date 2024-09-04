package analysis.common;

import abstractdomain.DomainValue;
import funarray.State;
import java.util.Arrays;
import java.util.Collection;

/**
 * The result of a {@link Analysis}.
 *
 * @param resultState the state at the very end of the program.
 * @param protocol    the protocol of every step of the analysis.
 * @param <ElementT>  the domain to abstract array element values with.
 * @param <VariableT> the domain to abstract variable values with.
 */
public record AnalysisResult<
        ElementT extends DomainValue<ElementT>,
        VariableT extends DomainValue<VariableT>>(
        State<ElementT, VariableT> resultState,
        String protocol,
        AssertionResult assertions) {

  public record AssertionResult(int positive, int negative, int indeterminable) {
    public AssertionResult positiveAssert() {
      return new AssertionResult(positive + 1, negative, indeterminable);
    }

    public AssertionResult negativeAssert() {
      return new AssertionResult(positive, negative + 1, indeterminable);
    }

    public AssertionResult indeterminableAssert() {
      return new AssertionResult(positive, negative, indeterminable + 1);
    }

    public static AssertionResult noAssert() {
      return new AssertionResult(0, 0, 0);
    }

    public static AssertionResult join(Collection<AssertionResult> assertionResults) {
      return new AssertionResult(
              assertionResults.stream().mapToInt(AssertionResult::positive).sum(),
              assertionResults.stream().mapToInt(AssertionResult::negative).sum(),
              assertionResults.stream().mapToInt(AssertionResult::indeterminable).sum()
      );
    }

    public static AssertionResult join(AssertionResult... assertionResults) {
      return join(Arrays.asList(assertionResults));
    }

    public AssertionResult join(AssertionResult other) {
      return new AssertionResult(
              this.positive + other.positive(),
              this.negative + other.negative(),
              this.indeterminable() + other.indeterminable());
    }
  }
}
