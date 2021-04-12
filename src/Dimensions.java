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

    // ================================================= Integers

    int BOARD_SIZE = 8;
    int BLOCK_DIMENSION = 60;
    int PIECE_DIMENSION = 40;
    int CROWN_DIMENSION = 20;

    int FRAME_WIDTH = 505;
    int FRAME_HEIGHT = 590;

    // ================================================= Arrays
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
}
