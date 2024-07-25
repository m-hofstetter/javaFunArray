package exception;

/**
 * The runtime exception for when an operation on a FunArray is semantically not possible. For
 * example when there is a try to satisfy an expression inequality that cannot be satisfied because
 * of the FunArray's bound order.
 */
public class FunArrayLogicException extends RuntimeException {
  public FunArrayLogicException(String msg) {
    super(msg);
  }
}
