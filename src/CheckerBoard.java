import javax.swing.JPanel;
import java.awt.GridLayout;

/**
 *  A representation of a CheckerBoard JPanel, which extends JPanel and implements Dimensions.
 *
 *  @author <b>Pendleton Pham</b> <br>
 *  phamsq <br>
 *  CSE 271 Section E <br>
 *  Mar 29th, 2021 <br>
 *  Project4 <br>
 *  CheckerBoard.java <br>
 */
public class CheckerBoard extends JPanel implements Dimensions {

    // ================================================= Variables

    char[][] boardStatus;
    CheckerPiece[] pieceHolder;
    boolean isRedTurn;
    boolean capture;
    int bCount;
    int rCount;

    // ================================================= Constructors

    /**
     * <b>Workhorse</b> <br>
     * <br>
     * Constructing a CheckerBoard JPanel object using all provided parameters.
     *
     * @param boardStatus 2D array holding the game's current state.
     * @param isRedTurn turn information. <b>true</b> for red, and <b>false</b> for black.
     */
    public CheckerBoard(char[][] boardStatus, boolean isRedTurn) {
        try {
            // Instantiating all game elements
            this.boardStatus = boardStatus;
            pieceHolder = new CheckerPiece[64];
            capture = false;

            int rCount = 0;
            int bCount = 0;

            setLayout(new GridLayout(8,8));
            for (int row = 0; row < boardStatus.length; row++) {
                for (int col = 0; col < boardStatus[row].length; col++) {
                    int holder = isLegal(row, col, boardStatus[row][col]);
                    switch (holder / 10) {
                        case 0 -> throw new IllegalCheckerBoardArgumentException();
                        case 1 -> rCount++;
                        case 2 -> bCount++;
                    }
                    if (holder % 10 == 1) {
                        capture = true;
                    }
                    pieceHolder[row * 8 + col] = new CheckerPiece(boardStatus[row][col], row, col);
                    add(pieceHolder[row * 8 + col], row * 8 + col);
                }
            }
            this.rCount = rCount;
            this.bCount = bCount;
        } catch (IllegalCheckerBoardArgumentException e) {
            System.out.println(e.getMessage());
        }

        setSize(480, 480);
        this.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        this.isRedTurn = isRedTurn;
    }

    /**
     * <b>Default</b> <br>
     * <br>
     * Constructing a default CheckerBoard JPanel.
     */
    public CheckerBoard() {
        this(BOARD_STATUS_DEFAULT, false);
    }

    // ================================================= Methods

    /**
     * The large <code>move()</code> method takes in a pre-encoded 2D <code>int[2][2]</code>.
     * <p>
     *     The method checks if the move is a valid one, only
     *     commits the move if valid, and King the piece if needed.
     * </p>
     *
     *
     * @param move pre-encoded 2D array.
     *    <ul>
     *             <li> <code>move[0]</code> initial position </li>
     *             <li> <code>move[1]</code> desired position </li>
     *             <li> <code>move[ ][0]</code> row </li>
     *             <li> <code>move[ ][1]</code> col </li>
     *    </ul>
     * @return This is gonna be a mouthful!
     *      <br>
     *      <br>
     *
     *      ***** <br>
     *      <b><i> ONE (1) DIGIT Failure codes: </i></b>
     *      <ul>
     *          <li> 0: Nothing to capture in the middle.</li>
     *          <li> <i> 1: [RESERVED FOR SUCCESS CODES] </i> </li>
     *          <li> 2: Not a diagonal move. </li>
     *          <li> 3: Occupied destination. </li>
     *          <li> 4: Non-King cannot move backward. </li>
     *          <li> 5: Moving a piece without capturing. </li>
     *          <li> 6: Cannot capture a friendly piece. </li>
     *          <li> 9: A move larger than three blocks. </li>
     *          <small>nice</small>.
     *      </ul>
     *
     *      ***** <br>
     *      <b><i> TWO (2) DIGITS Success codes start with 1: </i></b>
     *      <ul>
     *          <li> 11: Successful 1 space normal move. </li>
     *          <li> 12: Successful 2 space capture move. </li>
     *      </ul>
     *
     */
    public int move(int[][] move) {
        // Current position's status
        char currentStatus = boardStatus[move[0][0]][move[0][1]];

        // Desired position's status
        char desiredStatus = boardStatus[move[1][0]][move[1][1]];

        // Middle position's status if a capture move.
        char middleStatus = 'r';
        if (isRedTurn) {
            middleStatus = 'b';
        }

        // Number of columns and rows if moved.
        int difRow = move[1][0] - move[0][0];
        int difCol = move[1][1] - move[0][1];

        // The move is not a diagonal one (difRow does not match difCol).
        if (Math.abs(difRow) != Math.abs(difCol)) {
            return 2;
        }

        // The desired space is occupied by another piece.
        if (desiredStatus != '_') {
            return 3;
        }

        // Only King can move backwards.
        if (!Character.isUpperCase(currentStatus)) { // If not King
            if ((currentStatus == 'r' && difRow > 0) ||
                    (currentStatus == 'b' && difRow < 0)) {
                return 4;
            }
        }

        // King if red piece at top or black piece at bottom.
        // Keep in mind that currentStatus is merely a holder
        //      and should not be committed yet.
        if ((move[1][0] == 0 && isRedTurn) ||
                (move[1][0] == BOARD_SIZE - 1 && !isRedTurn)) {
            currentStatus = Character.toUpperCase(currentStatus);
        }

        // Two valid move cases.
        switch (Math.abs(difRow)) {
            // A normal 1 space move.
            case 1 -> {
                // Must capture is available.
                if (capture) {
                    return 5;
                }

                // Clear the initial space,
                setCheckerPiece(move[0][0], move[0][1], '_');
                // Set the final space,
                setCheckerPiece(move[1][0], move[1][1], currentStatus);

                // And return successful code.
                return 11;
            }

            // A capture 2 space move.
            case 2 -> {
                // Get location of the captured piece.
                int middleRow = (move[1][0] + move[0][0]) / 2;
                int middleCol = (move[1][1] + move[0][1]) / 2;

                // If the middle is not an enemy piece.
                if (boardStatus[middleRow][middleCol] != middleStatus) {
                    if (boardStatus[middleRow][middleCol] == '_'){
                        // There is nothing to capture in the middle.
                        return 0;
                    } else {
                        // Captured piece cannot be a friendly one.
                        return 6;
                    }
                }

                // Clear the initial space,
                setCheckerPiece(move[0][0], move[0][1], '_');
                // Set the final space,
                setCheckerPiece(move[1][0], move[1][1], currentStatus);
                // And capture the middle piece.
                setCheckerPiece(middleRow, middleCol, '_');

                // Decrement the piece count.
                if (middleStatus == 'r') {
                    rCount--;
                } else {
                    bCount--;
                }
            }

            // Nitwit wanker who couldn't even bother to check the damn rulebook!
            default -> {
                return 9;
            }
        }

        // Return successful code.
        return 12;
    }

    /**
     * Return whether the position of a piece is legal or not.
     * Return metadata about whether that piece can capture.
     *
     * @param row row of piece.
     * @param col column of piece.
     * @param status status of piece
     *
     * @return
     *      ***** <br>
     *      <b><i>0 if illegal.</i></b> <br>
     *      <br>
     *      ***** <br>
     *      <b><i>First digit</i></b>
     *      <ul>
     *          <li>1 : Red  </li>   
     *          <li>2 : Black</li>       
     *          <li>3 : Empty</li>
     *      </ul>
     *      ***** <br>
     *      <b><i>Second digit</i></b>
     *      <ul>
     *          <li>0: No capture  </li>   
     *          <li>1: Has capture</li>       
     *      </ul>
     */
    public int isLegal(int row, int col, char status) {
        if ((row + col) % 2 == 0 && status != '_') {
                return 0;
        } else {
            switch (status) {
                // Piece status cannot be other than '_', 'b', 'B', 'r', and 'R'.
                case 'r' -> {
                    if (redCapture(row, col)) {
                        return 11;
                    } else {
                        return 10;
                    }
                }
                case 'b' -> {
                    if (blackCapture(row, col)) {
                        return 21;
                    }
                    return 20;
                }
                case '_' -> {
                    return 30;
                }
            }
        }
        return 0;
    }

    /**
     * Check to see if there is a winner.
     *
     * @return
     *          <li>'r' for red</li>
     *          <li>'b' for blue</li>
     *          <li>'_' for neither.</li>
     *
     */
    public char isWinner() {
        // If there is no red or no black piece.
        if (bCount == 0) {
            return 'r';
        } else if (rCount == 0) {
            return 'b';
        }

        // Return blank if neither.
        return '_';
    }

    /**
     * Check if there exists a red piece on a given board that
     * can make a capture move.
     *
     * @return <b>true</b> if exists, and <b>false</b> otherwise.
     */
    public boolean checkRed() {
        for (int row = 0; row < boardStatus.length; row++) {
            for (int col = 0; col < boardStatus[row].length; col++) {
                switch(boardStatus[row][col]) {
                    case 'r':
                    case 'R':
                        if (redCapture(row, col)) {
                            return true;
                        }
                }
            }
        }
        return false;
    }

    /**
     * Check if a specified red piece on a given board
     * can make a capture move.
     *
     * @return <b>true</b> if able, and <b>false</b> otherwise.
     */
    private boolean redCapture(int row, int col) {
        boolean holder = false;
        // Check moving forward.
        if (row - 2 >= 0) {
            holder = (col - 2 >= 0 && boardStatus[row - 2][col - 2] == '_' && boardStatus[row - 1][col - 1] == 'b')
                    || (col + 2 <= 7 && boardStatus[row - 2][col + 2] == '_' && boardStatus[row - 1][col + 1] == 'b');
        }

        if (holder) {
            return true;
        } else if ((Character.isUpperCase(boardStatus[row][col]) && row + 2 <= 7)) {
            // Check moving backwards for king only.
            return (col - 2 >= 0 && boardStatus[row + 2][col - 2] == '_' && boardStatus[row + 1][col - 1] == 'b')
                    || (col + 2 <= 7 && boardStatus[row + 2][col + 2] == '_' && boardStatus[row + 1][col + 1] == 'b');
        }
        return false;
    }

    /**
     * Check if there exists a black piece on a given board that
     * can make a capture move.
     *
     * @return <b>true</b> if exists, and <b>false</b> otherwise.
     */
    public boolean checkBlack() {
        for (int row = 0; row < boardStatus.length; row++) {
            for (int col = 0; col < boardStatus[row].length; col++) {
                switch(boardStatus[row][col]) {
                    case 'b':
                    case 'B':
                        if (blackCapture(row, col)) {
                            return true;
                        }
                }
            }
        }
        return false;
    }

    /**
     * Check if a specified black piece on a given board
     * can make a capture move.
     *
     * @return <b>true</b> if able, and <b>false</b> otherwise.
     */
    private boolean blackCapture(int row, int col) {
        boolean holder = false;

        // Check moving forward.
        if (row + 2 <= 7) {
            holder = (col - 2 >= 0 && boardStatus[row + 2][col - 2] == '_' && boardStatus[row + 1][col - 1] == 'r')
                    || (col + 2 <= 7 && boardStatus[row + 2][col + 2] == '_' && boardStatus[row + 1][col + 1] == 'r');
        }

        if (holder) {
            return true;
        } else if ((Character.isUpperCase(boardStatus[row][col]) && row - 2 >= 2)) {
            // Check moving backwards for king only.
            return (col - 2 >= 0 && boardStatus[row - 2][col - 2] == '_' && boardStatus[row - 1][col - 1] == 'r')
                || (col + 2 <= 7 && boardStatus[row - 2][col + 2] == '_' && boardStatus[row - 1][col + 1] == 'r');
        }
        return false;
    }

    // ================================================= Setters

    /**
     * Change <code>boardStatus</code> to the provided parameter and immediately restart the game.
     * @param boardNew the desired board.
     */
    public void setBoardStatus(char[][] boardNew, boolean isRedTurn) {
        this.boardStatus = boardNew;
        int rCount = 0;
        int bCount = 0;
        try {
            for (int row = 0; row < boardStatus.length; row++) {
                for (int col = 0; col < boardStatus[row].length; col++) {
                    int holder = isLegal(row, col, boardStatus[row][col]);
                    switch (holder / 10) {
                        case 0 -> throw new IllegalCheckerBoardArgumentException();
                        case 1 -> rCount++;
                        case 2 -> bCount++;
                    }
                    if (holder % 10 == 1) {
                        capture = true;
                    }
                    pieceHolder[row * 8 + col].setPiece(
                            row, col, boardStatus[row][col]);
                }
            }
        } catch (IllegalCheckerBoardArgumentException e) {
            System.out.println("Not valid!");
        }

        this.isRedTurn = isRedTurn;
        this.rCount = rCount;
        this.bCount = bCount;
    }

    /**
     * Set the status of a specified piece.
     * @param row row on board.
     * @param col column on board.
     * @param status desired status.
     */
    public void setCheckerPiece(int row, int col, char status) {
        boardStatus[row][col] = status;
        pieceHolder[row * 8 + col].setPiece(row, col, status);
    }
}