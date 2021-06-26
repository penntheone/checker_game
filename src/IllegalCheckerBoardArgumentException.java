/**
 *  Custom <code>IllegalCheckerBoardArgumentException</code> error object.
 *
 *  @author <b>Pendleton Pham</b> <br>
 *  phamsq <br>
 *  CSE 271 Section E <br>
 *  Mar 29th, 2021 <br>
 *  Project4 <br>
 *  IllegalCheckerBoardArgumentException.java <br>
 */
public class IllegalCheckerBoardArgumentException extends Exception {

    /**
     * <b>Default</b>
     * <br><br>
     * Simply throws an error.
     */
    public IllegalCheckerBoardArgumentException() {
        super();
    }

    /**
     * <b>Workhorse</b>
     * <br><br>
     * Throws an error with a message
     * @param message accompanied message.
     */
    public IllegalCheckerBoardArgumentException(String message) {
        super(message);
    }
}
