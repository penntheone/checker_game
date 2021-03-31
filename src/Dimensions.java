/**
 *  A centralized repository for all static values
 *
 *  @author Pendleton Pham
 *  phamsq
 *  CSE 271 Section E
 *  Mar 29th, 2021
 *  Project4
 *  Dimensions.java
 */
public interface Dimensions {
    int BOARD_SIZE = 8;
    int BLOCK_DIMENSION = 60;
    int PIECE_DIMENSION = 40;
    int CROWN_DIMENSION = 10;

    int FRAME_WIDTH = 505;
    int FRAME_HEIGHT = 585;

    // The default board when starting a game.
    char[][] BOARD_STATUS_DEFAULT = {
        {'_', 'b', '_', 'b', '_', 'b', '_', 'b'},
        {'b', '_', 'b', '_', 'b', '_', 'b', '_'},
        {'_', 'b', '_', 'b', '_', 'b', '_', 'b'},
        {'_', '_', '_', '_', '_', '_', '_', '_'},
        {'_', '_', '_', '_', '_', '_', '_', '_'},
        {'r', '_', 'r', '_', 'r', '_', 'r', '_'},
        {'_', 'r', '_', 'r', '_', 'r', '_', 'r'},
        {'r', '_', 'r', '_', 'r', '_', 'r', '_'}
    };

    // Board displayed when Red wins.
    char[][] BOARD_STATUS_RED_WINS = {
        {'_', 'r', '_', 'r', '_', 'r', '_', 'r'},
        {'r', '_', 'r', '_', 'r', '_', 'r', '_'},
        {'_', 'r', '_', 'r', '_', 'r', '_', 'r'},
        {'r', '_', 'r', '_', 'r', '_', 'r', '_'},
        {'_', 'r', '_', 'r', '_', 'r', '_', 'r'},
        {'r', '_', 'r', '_', 'r', '_', 'r', '_'},
        {'_', 'r', '_', 'r', '_', 'r', '_', 'r'},
        {'r', '_', 'r', '_', 'r', '_', 'r', '_'}
    };

    // Board displayed when Black wins
    char[][] BOARD_STATUS_BLACK_WINS = {
        {'_', 'b', '_', 'b', '_', 'b', '_', 'b'},
        {'b', '_', 'b', '_', 'b', '_', 'b', '_'},
        {'_', 'b', '_', 'b', '_', 'b', '_', 'b'},
        {'b', '_', 'b', '_', 'b', '_', 'b', '_'},
        {'_', 'b', '_', 'b', '_', 'b', '_', 'b'},
        {'b', '_', 'b', '_', 'b', '_', 'b', '_'},
        {'_', 'b', '_', 'b', '_', 'b', '_', 'b'},
        {'b', '_', 'b', '_', 'b', '_', 'b', '_'}
    };
}
