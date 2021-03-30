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
    char[][] boardStatus;
    boolean isRedTurn;
    int bCount;
    int rCount;

    /**
     * Workhorse
     * Constructing a CheckerBoard JPanel object using all provided parameters.
     *
     * @param boardStatus 2D array holding the game's current state.
     * @param isRedTurn turn information. true for red, and false for black.
     */
    public CheckerBoard(char[][] boardStatus, boolean isRedTurn, int bCount, int rCount) {
        try {
            // Instantiating all game elements
            this.boardStatus = boardStatus;
            for (int row = 0; row < boardStatus.length; row++) {
                for (int col = 0; col < boardStatus[row].length; col++) {
                    if ((row + col) % 2 == 1) {
                        if (boardStatus[row][col] != '_') { // White tile cannot have a piece.
                            throw new IllegalCheckerBoardArgumentException("Illegal piece location!");
                        }
                    } else {
                        switch(boardStatus[row][col]) { // Piece status cannot be other than '_', 'b', and 'r'
                            case 'r':
                            case 'b':
                            case '_':
                                break;
                            default:
                                throw new IllegalCheckerBoardArgumentException("Illegal piece location!");
                        }
                    }

                    add(new CheckerPiece(boardStatus[row][col], row, col));
                }
            }
        } catch (IllegalCheckerBoardArgumentException e) {
            System.out.println(e.getMessage());
        }

        setSize(480, 480);
        this.setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        this.isRedTurn = isRedTurn;
        this.bCount = bCount;
        this.rCount = rCount;
    }

    /**
     * Default
     * Constructing a default CheckerBoard JPanel.
     */
    public CheckerBoard() {
        this(BOARD_STATUS_DEFAULT, true, 12, 12);
    }

    /**
     * Change boardStatus to the provided parameter and immediately restart the game.
     * @param boardStatus the desired board.
     */
    public void setBoardStatus(char[][] boardStatus) {
        this.boardStatus = boardStatus;
        repaint();
    }

    /**
     * Set the status of a specified piece.
     * @param row row on board.
     * @param col column on board.
     * @param status desired status.
     */
    public void setCheckerPiece(int row, int col, char status) {
        boardStatus[row][col] = status;
        repaint();
    }

    public static void arrayToString(char[][] input) {
        for (char[] e : input) {
            for (char f : e) {
                System.out.print(f + " ");
            }
            System.out.println();
        }
    }

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
     * @param move pre-encoded 2D array
     * @return true if successful and false otherwise.
     */
    public boolean move(int[][] move) {
        // Current position's status
        char currentStatus = boardStatus[move[0][0]][move[0][1]];

        // Desired position's status
        char desiredStatus = boardStatus[move[1][0]][move[1][1]];

        // Middle position's status if moved by 2 spaces.
        // Must be an enemy piece in the middle.
        char middleStatus = 'r';
        if (isRedTurn) {
            middleStatus = 'b';
        }

        // Number of columns and rows if moved.
        int difRow = move[1][0] - move[0][0];
        int difCol = move[1][1] - move[0][1];

        // Either
        // The move is not a diagonal one (difRow does not match difCol).
        // Or
        // The desired space is occupied by another piece.
        if (Math.abs(difRow) != Math.abs(difCol) ||
            desiredStatus != '_') {
            return false;
        }

        // Only King can move backwards.
        if (!Character.isUpperCase(currentStatus)) { // If not King
            if ((currentStatus == 'r' && difRow > 0) ||
                    (currentStatus == 'b' && difRow < 0)) {
                return false;
            }
        }

        // Two valid move cases.
        switch (Math.abs(difRow)) {
            // A normal 1 space move.
            case 1 -> {
                // Clear the initial space,
                setCheckerPiece(move[0][0], move[0][1], '_');
                // and set the final space.
                setCheckerPiece(move[1][0], move[1][1], convert(isRedTurn));
            }

            // A capture 2 space move.
            case 2 -> {
                // Get location of the captured piece
                int middleRow = (move[1][0] + move[0][0]) / 2;
                int middleCol = (move[1][1] + move[0][1]) / 2;

                // Captured piece cannot be a friendly one.
                if (boardStatus[middleRow][middleCol] != middleStatus) {
                    return false;
                }

                // Clear the initial space,
                setCheckerPiece(move[0][0], move[0][1], '_');
                // set the final space,
                setCheckerPiece(move[1][0], move[1][1], convert(isRedTurn));
                // and capture the middle piece.
                setCheckerPiece(middleRow, middleCol, '_');

                // Decrement the piece count.
                if (middleStatus == 'r') {
                    rCount--;
                } else {
                    bCount--;
                }
            }

            // Nitwit wanker who couldn't even
            // bother to check the damn rulebook!
            default -> {
                return false;
            }
        }

        // King if red piece at top or black piece at bottom.
        if ((move[1][0] == 0 && isRedTurn) ||
                (move[1][0] == BOARD_SIZE - 1 && !isRedTurn)) {
            boardStatus[move[1][0]][move[1][1]] = Character.toUpperCase(currentStatus);
        }

        // Reverse turn.
        isRedTurn = !isRedTurn;
        repaint();
        return true;
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
     * Convert from boolean isRedTurn to its char boardStatus equivalent.
     *
     * @param currentRed input boolean.
     * @return its equivalent char status.
     */
    public static char convert(boolean currentRed) {
        if (currentRed) {
            return 'r';
        } else {
            return 'b';
        }
    }

    /**
     * Change the current turn holder.
     *
     * @param redTurn the new turn holder.
     *                true for red, false for black.
     */
    public void setRedTurn(boolean redTurn) {
        isRedTurn = redTurn;
    }
}