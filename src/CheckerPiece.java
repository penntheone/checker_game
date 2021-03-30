import javax.swing.JComponent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *  A representation of a CheckerPiece JComponent, which extends JComponent and implements Dimensions.
 *
 *  @author Pendleton Pham
 *  phamsq
 *  CSE 271 Section E
 *  Mar 29th, 2021
 *  Project4
 *  CheckerPiece.java
 */
public class CheckerPiece extends JComponent implements Dimensions {
    int row;
    int col;
    char status;

    /**
     * Workhorse
     * Constructing a CheckerPieceComponent object using all provided parameters.
     *
     * @param status status of piece. Uppercase if King, else lowercase.
     * @param row row on board.
     * @param col column on board.
     */
    public CheckerPiece(char status, int row, int col) {
        this.status = status;
        this.row = row;
        this.col = col;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paintComponent(Graphics g) {
        char holder = Character.toLowerCase(status); // Strip King Status

        // Draw borders of tiles.
        g.setColor(Color.black);
        g.drawRect(row, col, BLOCK_DIMENSION, BLOCK_DIMENSION);

        // Pick alternating white and green tile color.
        if ((row + col) % 2 == 1) {
            g.setColor(Color.green);
        } else {
            g.setColor(Color.white);
        }

        // Fill tiles.
        g.fillRect(row, col, BLOCK_DIMENSION, BLOCK_DIMENSION);

        // Fill crown if King
        if (Character.isUpperCase(status)) {
            g.setColor(Color.yellow);
            g.fillOval(row + 20, col + 20, CROWN_DIMENSION, CROWN_DIMENSION);
        }

        // Pick color of piece based on side.
        if (holder == 'b') {
            g.setColor(Color.BLACK);
        } else if (holder == 'r') {
            g.setColor(Color.red);
        }

        // Fill pieces
        g.fillOval(row + 10, col + 10, PIECE_DIMENSION, PIECE_DIMENSION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize() {
        // Prevent rendering to 0 px.
        return new Dimension(BLOCK_DIMENSION, BLOCK_DIMENSION);
    }
}
