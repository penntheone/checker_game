import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

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
    boolean firstValidChoice;
    int[][] moveVar;

    CheckerBoard board = new CheckerBoard();
    JPanel panel;
    JPanel statusBoard;
    JLabel statusRed;
    JLabel statusBlack;
    JDialog rulesBox;
    JDialog aboutBox;
    JMenuBar menubar;
    JMenu fileMenu;
    JMenu helpMenu;

    final JLabel MEEEE = new JLabel("| Developed by Pendleton Pham");

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
        // Instantiate needed values.
        moveVar = new int[2][2]; // A blank move array.
        firstValidChoice = false; // Keeping track if the
                                  // initial position is valid.

        // Instantiate game board.
        panel = new JPanel();
        panel.add(board);
        panel.addMouseListener(new MouseClickListener());

        // Instantiate status board.
        statusBoard = new JPanel();
        statusBoard.setLayout(new FlowLayout());
        {
            statusRed = new JLabel();
            statusRed.setText(String.format("Red: %d", board.rCount));
            statusRed.setForeground(Color.red);

            statusBlack = new JLabel();
            statusBlack.setText(String.format("Black: %d", board.bCount));
            statusBlack.setForeground(Color.BLACK);

            statusBoard.add(statusRed);
            statusBoard.add(statusBlack);
            statusBoard.add(MEEEE);
        }

        // Instantiate menu bar.
        menubar = new JMenuBar();
        {
            // File menu.
            fileMenu = new JMenu("File");
            {
                // New Game menu item.
                JMenuItem newGameItem = new JMenuItem("New Game");
                newGameItem.addActionListener(new MenuActionListener());
                fileMenu.add(newGameItem);

                // Exit menu item.
                JMenuItem exitItem = new JMenuItem("Exit");
                exitItem.addActionListener(new MenuActionListener());
                fileMenu.add(exitItem);
            }

            // Help menu.
            helpMenu = new JMenu("Help");
            {
                // Checker Game Rules menu item.
                JMenuItem rulesItem = new JMenuItem("Checker Game Rules");
                rulesItem.addActionListener(new MenuActionListener());
                helpMenu.add(rulesItem);
                {
                    // Rule dialog.
                    rulesBox = new JDialog();
                    rulesBox.setSize(350, 110);
                    rulesBox.setResizable(false);
                    rulesBox.setLayout(new FlowLayout());
                    {
                        // First link
                        JLabel rulesFirst = new JLabel(
                                "https://www.wikihow.com/Play-Checkers");
                        rulesFirst.setForeground(Color.BLUE.darker());
                        rulesFirst.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        rulesBox.add(rulesFirst);
                        rulesFirst.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                try {
                                    Desktop.getDesktop().browse(new URI(
                                            "https://www.wikihow.com/Play-Checkers"));
                                } catch (IOException | URISyntaxException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });

                        // Second link
                        JLabel rulesSecond = new JLabel(
                                "https://www.youtube.com/watch?v=ScKIdStgAfU");
                        rulesSecond.setForeground(Color.BLUE.darker());
                        rulesSecond.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        rulesSecond.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                try {
                                    Desktop.getDesktop().browse(new URI(
                                            "https://www.youtube.com/watch?v=ScKIdStgAfU"));
                                } catch (IOException | URISyntaxException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });

                        JLabel rulesTitle = new JLabel(
                                "Helpful Resources");
                        rulesBox.add(rulesTitle);
                        rulesBox.add(rulesFirst);
                        rulesBox.add(rulesSecond);
                    }
                }

                // About Game Rules item.
                JMenuItem aboutItem = new JMenuItem("About");
                aboutItem.addActionListener(new MenuActionListener());
                helpMenu.add(aboutItem);
                {
                    // About dialog.
                    aboutBox = new JDialog();
                    aboutBox.setLayout(new BorderLayout());
                    aboutBox.setSize(515, 215);
                    aboutBox.setResizable(false);
                    JLabel about = new JLabel("<html>Created by Pendleton Pham<br>phamsq@miamioh.edu<br>CSE271 Project4<br>©2021<br><br>Please, Microsoft! Let me be your little Pog-champ! uwu</html>");
                    about.setHorizontalAlignment(JLabel.CENTER);
                    aboutBox.add(about, BorderLayout.CENTER);
                }
            }
            menubar.add(fileMenu);
            menubar.add(helpMenu);
        }

        setTitle("The Society for Putting Things Over Other Things");
        setJMenuBar(menubar);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(statusBoard, BorderLayout.SOUTH);
    }

    /**
     * A child class that implements the menu buttons.
     */
    class MenuActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "Exit" -> System.exit(0);
                case "New Game" -> {
                    board.setBoardStatus(
                            new char[][]{
                                {'_', 'b', '_', 'b', '_', 'b', '_', 'b'},
                                {'b', '_', 'b', '_', 'b', '_', 'b', '_'},
                                {'_', 'b', '_', 'b', '_', 'b', '_', 'b'},
                                {'_', '_', '_', '_', '_', '_', '_', '_'},
                                {'_', '_', '_', '_', '_', '_', '_', '_'},
                                {'r', '_', 'r', '_', 'r', '_', 'r', '_'},
                                {'_', 'r', '_', 'r', '_', 'r', '_', 'r'},
                                {'r', '_', 'r', '_', 'r', '_', 'r', '_'}
                            }, true); // Create a new board.

                    statusRed.setText(String.format("Red: %d", board.rCount));
                    statusBlack.setText(String.format("Black: %d", board.bCount));
                    // TODO Figuring out wtf happened to the interface!
                }
                case "Checker Game Rules" -> rulesBox.setVisible(true);
                case "About" -> aboutBox.setVisible(true);
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
            // Converting from pixel to row and column
            int clickRow = e.getY() / BLOCK_DIMENSION;
            int clickCol = e.getX() / BLOCK_DIMENSION;

            // If this is first click.
            if (!firstValidChoice) {
                // Only move on to second click if piece is on controlling player's turn.
                if (Character.toLowerCase(board.boardStatus[clickRow][clickCol])
                        == CheckerBoard.convert(board.isRedTurn)) {
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

                    // Check if there is a winner.
                    switch (board.isWinner()) {
                        case 'r' -> {
                            statusRed.setText("Red Wins!");
                            statusBlack.setText("");
                            board.setBoardStatus(new char[][]{
                                {'_', 'r', '_', 'r', '_', 'r', '_', 'r'},
                                {'r', '_', 'r', '_', 'r', '_', 'r', '_'},
                                {'_', 'r', '_', 'r', '_', 'r', '_', 'r'},
                                {'r', '_', 'r', '_', 'r', '_', 'r', '_'},
                                {'_', 'r', '_', 'r', '_', 'r', '_', 'r'},
                                {'r', '_', 'r', '_', 'r', '_', 'r', '_'},
                                {'_', 'r', '_', 'r', '_', 'r', '_', 'r'},
                                {'r', '_', 'r', '_', 'r', '_', 'r', '_'}
                                }, true);
                            return;
                        }
                        case 'b' -> {
                            statusRed.setText("");
                            statusBlack.setText("Black Wins!");
                            board.setBoardStatus(new char[][]{
                                {'_', 'b', '_', 'b', '_', 'b', '_', 'b'},
                                {'b', '_', 'b', '_', 'b', '_', 'b', '_'},
                                {'_', 'b', '_', 'b', '_', 'b', '_', 'b'},
                                {'b', '_', 'b', '_', 'b', '_', 'b', '_'},
                                {'_', 'b', '_', 'b', '_', 'b', '_', 'b'},
                                {'b', '_', 'b', '_', 'b', '_', 'b', '_'},
                                {'_', 'b', '_', 'b', '_', 'b', '_', 'b'},
                                {'b', '_', 'b', '_', 'b', '_', 'b', '_'}
                                }, false);
                            return;
                        }
                        default -> {
                            statusRed.setText(String.format("Red: %d", board.rCount));
                            statusBlack.setText(String.format("Black: %d", board.bCount));
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
     * I, Aphrodite The Mighty,
     * Henceforth Exterminate Everything Around Me That
     * Restricts Me from Being the Master.
     *
     * TREMBLE IN FEAR! GRACE UNDER MY BLINDING GLORY!
     *
     * @param args (graceful) arguments
     */
    public static void main(String[] args) {
        JFrame checkerFrame = new CheckerGame();
        checkerFrame.setVisible(true);
    }
}
