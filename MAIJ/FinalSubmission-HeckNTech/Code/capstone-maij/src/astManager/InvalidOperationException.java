package astManager;

/**For use with BaseAST - when one of the subclasses tries to call
 * a method that is not supported for that subclass - like getParent
 * for a ProjectAST.
 * @author Jacob Botha
 *
 */
public class InvalidOperationException extends Exception {


}
