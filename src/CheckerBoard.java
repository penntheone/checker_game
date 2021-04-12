import javax.swing.JPanel;
import java.awt.GridLayout;

/**
 *  A representation of a CheckerBoard JPanel, which extends JPanel and implements Dimensions.
 *
 *  @author Pendleton Pham
 *  phamsq
 *  CSE 271 Section E
 *  Mar 29th, 2021
 *  Project4
 *  CheckerBoard.java
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
     * Workhorse
     * Constructing a CheckerBoard JPanel object using all provided parameters.
     *
     * @param boardStatus 2D array holding the game's current state.
     * @param isRedTurn turn information. true for red, and false for black.
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
                    if ((row + col) % 2 == 0) {
                        if (boardStatus[row][col] != '_') { // White tile cannot have a piece.
                            throw new IllegalCheckerBoardArgumentException("Illegal piece location!");
                        }
                    } else {
                        switch (boardStatus[row][col]) {
                            // Piece status cannot be other than '_', 'b', 'B', 'r', and 'R'.
                            case 'r':
                            case 'R':
                                rCount++;
                                if (redCapture(row, col)) {
                                    capture = true;
                                }
                                break;
                            case 'b':
                            case 'B':
                                bCount++;
                                if (blackCapture(row, col)) {
                                    capture = true;
                                }
                                break;
                            case '_':
                                break;
                            default:
                                throw new IllegalCheckerBoardArgumentException("Illegal piece location!");
                        }
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
     * Default
     * Constructing a default CheckerBoard JPanel.
     */
    public CheckerBoard() {
        this(BOARD_STATUS_DEFAULT, false);
    }

    // ================================================= Methods

    /**
     * The large move() method takes in a pre-encoded 2D
     * int[2][2] array using the following scheme:
     *
     *    move[0] initial position    *   move[][0] row
     *    move[1] desired position    *   move[][1] col
     *
     * The method checks if the move is a valid one, only
     * commits the move if valid, and King the piece if needed.
     *
     * @param move pre-encoded 2D array.
     * @return 1* if successful, and error codes otherwise.
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
     * Check to see if there is a winner.
     *
     * @return 'r' for red
     *         'b' for blue
     *         '_' for neither.
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
     * @return true if exists, and false otherwise.
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
     * @return true if able, and false otherwise.
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
     * @return true if exists, and false otherwise.
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
     * @return true if able, and false otherwise.
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
     * Change boardStatus to the provided parameter and immediately restart the game.
     * @param boardNew the desired board.
     */
    public void setBoardStatus(char[][] boardNew, boolean isRedTurn) {
        this.boardStatus = boardNew;
        int rCount = 0;
        int bCount = 0;

        for (int row = 0; row < boardStatus.length; row++) {
            for (int col = 0; col < boardStatus[row].length; col++) {
                pieceHolder[row * 8 + col].setPiece(
                        row, col, boardStatus[row][col]);
                switch (Character.toLowerCase(boardStatus[row][col])) {
                    case 'r' -> rCount++;
                    case 'b' -> bCount++;
                }
            }
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