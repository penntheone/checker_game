import javax.swing.JComponent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *  A representation of a <code>CheckerPiece</code> JComponent, which extends JComponent and implements Dimensions.
 *
 *  @author <b>Pendleton Pham</b> <br>
 *  phamsq <br>
 *  CSE 271 Section E <br>
 *  Mar 29th, 2021 <br>
 *  Project4 <br>
 *  CheckerPiece.java <br>
 */
public class CheckerPiece extends JComponent implements Dimensions {

    // ================================================= Variables

    int row;
    int col;
    char status;

    // ================================================= Constructors

    /**
     * <b>Workhorse</b>
     * <br><br>
     * Constructing a CheckerPieceComponent object using all provided parameters.
     *
     * @param status status of piece, uppercase if King, else lowercase.
     * @param row row on board.
     * @param col column on board.
     */
    public CheckerPiece(char status, int row, int col) {
        this.status = status;
        this.row = row;
        this.col = col;
    }

    // ================================================= Methods

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintComponent(Graphics g) {
        char holder = Character.toLowerCase(status); // Strip King Status.
        // Pick alternating white and green tile color.
        if ((row + col) % 2 == 1) {
            g.setColor(Color.green);
        } else {
            g.setColor(Color.white);
        }

        // Fill tiles.
        g.fillRect(0, 0, BLOCK_DIMENSION, BLOCK_DIMENSION);

        // Pick color of piece based on side.
        if (holder == 'b') {
            g.setColor(Color.black);
        } else if (holder == 'r') {
            g.setColor(Color.red);
        }

        // Fill pieces.
        g.fillOval(10, 10, PIECE_DIMENSION, PIECE_DIMENSION);

        // Fill crown if King.
        if (Character.isUpperCase(status)) {
            g.setColor(Color.yellow);
            g.fillOval(20, 20, CROWN_DIMENSION, CROWN_DIMENSION);
        }

        // Draw borders of tiles.
        g.setColor(Color.black);
        g.drawRect(0, 0, BLOCK_DIMENSION, BLOCK_DIMENSION);
    }

    /**
     * Set a piece at provided location with provided status.
     *
     * @param row row on board.
     * @param col column on board.
     * @param status desired status.
     */
    public void setPiece(int row, int col, char status) {
        this.status = status;
        this.row = row;
        this.col = col;
        repaint();
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
