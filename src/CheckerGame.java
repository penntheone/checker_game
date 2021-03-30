import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *  The GUI constructors and game file's driver. CheckerGame extends JFrame
 *  and implements Dimensions
 *
 *  @author Pendleton Pham
 *  phamsq
 *  CSE 271 Section E
 *  Mar 29th, 2021
 *  Project4
 *  CheckerGame.java
 */
public class CheckerGame extends JFrame implements Dimensions {
    CheckerBoard board;
    boolean firstValidChoice;
    int[][] moveVar;

    /**
     * Default
     * Empty constructor which simply calls a method. The called method constructDisplay()
     * will instantiate each GUI instance property and add every element in the correct
     * order to the JPanel, and add the JPanel to the JFrame.
     */
    public CheckerGame() {
        constructDisplay();
    }

    /**
     * Create the GUI by instantiating all GUI elements, adding them
     * to a JPanel object, and adding the JPanel object to the JFrame.
     */
    private void constructDisplay() {
        // Instantiate the default board.
        board = new CheckerBoard();
        moveVar = new int[2][2]; // A blank move array.
        firstValidChoice = false; // Keeping track if the
                                  // initial position is valid.


//        CheckerBoard.arrayToString(board.boardStatus);
        JPanel panel = new JPanel();
        JLabel status = new JLabel();
        JLabel meeeee = new JLabel();

        // TODO UI non-functional
        // TODO Non-responsive clicking
        panel.add(board);  // TODO Fix the UI drifting
        panel.add(status); // TODO Write Status
        panel.add(meeeee); // TODO Write my fucking Name
        panel.addMouseListener(new MouseClickListener());

        JMenuBar menubar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu helpMenu = new JMenu("Help");
        menubar.add(fileMenu);
        menubar.add(helpMenu);

        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(new MenuActionListener());
        fileMenu.add(newGameItem);

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new MenuActionListener());
        fileMenu.add(exitItem);

        add(panel);
        setJMenuBar(menubar);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setTitle("The Society for Putting Things Over Other Things");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * A child class that implements the menu buttons.
     */
    class MenuActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String item = e.getActionCommand();
            System.out.println("Action Item = " + item);

            if (item.equals("Exit")) {
                System.out.println("Exit called.");
                System.exit(0);
            } else if (item.equals("New Game")) {
                System.out.println("New Game button pressed.");
                board = new CheckerBoard(); // Create a new board.
            }
        }
    }

    /**
     * A child class that drives mouse interactions.
     */
    class MouseClickListener implements MouseListener {
        /**
         * Large mouseClicked() method that drive interactions on click.
         *
         * @param e mouse click.
         */
        @Override
        public void mouseClicked(MouseEvent e) {
//            System.out.println(e.getX() + " " + e.getY());

            // Converting from pixel to row and column
            int clickRow = e.getY() / BLOCK_DIMENSION;
            int clickCol = e.getX() / BLOCK_DIMENSION;

//            System.out.println(clickRow + " " + clickCol);

            // If this is first click.
            if (!firstValidChoice) {
                // Only move on to second click if piece is on controlling player's turn.
                if (board.boardStatus[clickRow][clickCol] == CheckerBoard.convert(board.isRedTurn)) {
                    firstValidChoice = true;
                    // Save initial location info into move array.
                    moveVar[0][0] = clickRow;
                    moveVar[0][1] = clickCol;
                    System.out.println("First Click");
                }
            }
            // If this is second click.
            else {
                // Desired location is always valid for the purpose of verification.
                moveVar[1][0] = clickRow;
                moveVar[1][1] = clickCol;

                System.out.println("Second Click");

                // Attempt to move the piece.
                if (board.move(moveVar)) {
                    System.out.println("Success");
                    CheckerBoard.arrayToString(board.boardStatus);

                    board.repaint();

                    // Check if there is a winner.
                    switch (board.isWinner()) {
                        case 'r' -> {
                            System.out.println("Red Wins!");
                            board = new CheckerBoard(
                                    Dimensions.BOARD_STATUS_RED_WINS, true, 32, 0);
                        }
                        case 'b' -> {
                            System.out.println("Black Wins!");
                            board = new CheckerBoard(
                                    Dimensions.BOARD_STATUS_BLACK_WINS, false, 0, 32);
                        }
                    }

                    // Flush moveVar.
                    moveVar = new int[2][2];
                } else {
                    System.out.println("Failed");
                }

                // Return to first click.
                firstValidChoice = false;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    /**
     * I Hereby Exterminate Everything Around Me
     * That Restricts Me from Being the Master.
     *
     * TREMBLE IN FEAR! GRACE UNDER MY BLINDING GLORY!
     * @param args arguments
     */
    public static void main(String[] args) {
        JFrame checkerFrame = new CheckerGame();
        checkerFrame.setVisible(true);
    }
}
