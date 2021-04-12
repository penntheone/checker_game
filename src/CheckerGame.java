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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *  The GUI constructors and game file's driver. CheckerGame extends JFrame
 *  and implements Dimensions.
 *
 *  @author Pendleton Pham
 *  phamsq
 *  CSE 271 Section E
 *  Mar 29th, 2021
 *  Project4
 *  CheckerGame.java
 */
public class CheckerGame extends JFrame implements Dimensions {

    // ================================================= Variables

    boolean firstValidChoice;
    boolean endGame;
    int[][] moveVar;

    JPanel panel;
    CheckerBoard board = new CheckerBoard();
    JPanel statusArea;

    JPanel statusCurrent;
    JLabel current;

    JPanel statusBoard;
    JLabel statusRed;
    JLabel statusBlack;
    final JLabel separator = new JLabel(" | ");
    JLabel turn;

    JMenuBar menubar;
    JMenu fileMenu;
    JMenu helpMenu;
    JDialog rulesBox;
    JDialog aboutBox;

    // ================================================= Constructors

    /**
     * Default
     * Empty constructor which simply calls a method. The called method constructDisplay()
     * will instantiate each GUI instance property and add every element in the correct
     * order to the JPanel, and add the JPanel to the JFrame.
     */
    public CheckerGame() {
        constructDisplay();
    }

    // ================================================= Methods

    /**
     * Create the GUI by instantiating all GUI elements, adding them
     * to a JPanel object, and adding the JPanel object to the JFrame.
     */
    private void constructDisplay() {
        // Instantiate needed values.
        moveVar = new int[2][2]; // A blank move array.
        firstValidChoice = false; // Keeping track if the
                                  // initial position is valid.
        endGame = false; // Preventing input after winning.

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

            turn = new JLabel("Black turn.");
            turn.setForeground(Color.BLACK);

            statusBoard.add(statusRed);
            statusBoard.add(statusBlack);
            statusBoard.add(separator);
            statusBoard.add(turn);
        }

        // Instantiate current operation label.
        statusCurrent = new JPanel();
        statusCurrent.setLayout(new FlowLayout());
        {
            current = new JLabel();
            current.setText("New Game!");
            statusCurrent.add(current);
        }

        // Add statusCurrent and statusBoard to main statusArea
        statusArea = new JPanel();
        statusArea.setLayout(new BorderLayout());
        statusArea.add(statusCurrent, BorderLayout.CENTER);
        statusArea.add(statusBoard, BorderLayout.SOUTH);

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
                        rulesFirst.addMouseListener(new LinkActionListener());

                        // Second link
                        JLabel rulesSecond = new JLabel(
                                "https://www.youtube.com/watch?v=ScKIdStgAfU");
                        rulesSecond.setForeground(Color.BLUE.darker());
                        rulesSecond.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        rulesSecond.addMouseListener(new LinkActionListener());

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
                    JLabel about = new JLabel(
                            "<html>Created by Pendleton Pham<br>phamsq@miamioh.edu<br>CSE271 Project4<br>©2021<br><br>Please, Microsoft! Let me be your little Pog-champ! uwu</html>"
                    );
                    about.setHorizontalAlignment(JLabel.CENTER);
                    aboutBox.add(about, BorderLayout.CENTER);
                }
            }
            menubar.add(fileMenu);
            menubar.add(helpMenu);
        }

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(statusArea, BorderLayout.SOUTH);

        setTitle("The Society for Putting Things Over Other Things");
        setJMenuBar(menubar);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * A child static class that implements the link in the resources box.
     */
    static class LinkActionListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getComponent() instanceof JLabel) {
                JLabel label = (JLabel) e.getComponent();
                String text = label.getText();

                try {
                    Desktop.getDesktop().browse(new URI(text));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    // ================================================= Children classes

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
                            }, false); // Create a new board.
                    statusRed.setText(String.format("Red: %d", board.rCount));
                    statusBlack.setText(String.format("Black: %d", board.bCount));
                    endGame = false;
                    current.setText("New Game!");
                }
                case "Checker Game Rules" -> rulesBox.setVisible(true);
                case "About" -> aboutBox.setVisible(true);
            }
        }
    }

    /**
     * A child class that drives mouse interactions.
     */
    class MouseClickListener extends MouseAdapter {
        /**
         * Large mouseClicked() method that drive interactions on click.
         *
         * @param e mouse click.
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (!endGame) {
                // Converting from pixel to row and column
                int clickRow = e.getY() / BLOCK_DIMENSION;
                int clickCol = e.getX() / BLOCK_DIMENSION;

                char convertFromBoolean = 'b';
                if (board.isRedTurn) {
                    convertFromBoolean = 'r';
                }

                // If this is first click.
                if (!firstValidChoice) {
                    // Only move on to second click if piece is on controlling player's turn.
                    if (Character.toLowerCase(board.boardStatus[clickRow][clickCol]) == convertFromBoolean) {
                        firstValidChoice = true;
                        // Save initial location info into move array.
                        moveVar[0][0] = clickRow;
                        moveVar[0][1] = clickCol;
                        current.setText("A valid piece selected! Pick the destination...");
                    } else {
                        current.setText("Not a valid selection! Try again.");
                    }
                }

                // If this is second click.
                else {
                    // Desired location is always valid for the purpose of verification.
                    moveVar[1][0] = clickRow;
                    moveVar[1][1] = clickCol;

                    // Attempt to move the piece.
                    int holder = board.move(moveVar);
                    if (holder / 10 == 1) {
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
                                current.setText("Congratulations!");
                                endGame = true;
                                switchTurn();
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
                                current.setText("Congratulations!");
                                endGame = true;
                                switchTurn();
                                return;
                            }
                            default -> {
                                statusRed.setText(String.format("Red: %d", board.rCount));
                                statusBlack.setText(String.format("Black: %d", board.bCount));
                                current.setText("Success!");
                            }
                        }

                        // Check for capture status.
                        boolean checkRed = board.checkRed();
                        boolean checkBlack = board.checkBlack();

                        // If a 1 space move, switch turn immediately.
                        if (holder % 10 == 1) {
                            if (board.isRedTurn) {
                                board.capture = checkBlack;
                            } else {
                                board.capture = checkRed;
                            }
                            switchTurn();
                        } else {
                            // If a 2 space capture move.
                            if (board.isRedTurn) {
                                if (checkRed) {
                                    board.capture = true;
                                    // Does not invert turn if still more capture moves.
                                } else {
                                    board.capture = checkBlack;
                                    switchTurn();
                                }
                            } else {
                                if (checkBlack) {
                                    board.capture = true;
                                    // Does not invert turn if still more capture moves.
                                } else {
                                    board.capture = checkRed;
                                    switchTurn();
                                }
                            }
                        }

                        // Flush moveVar.
                        moveVar = new int[2][2];
                    } else {
                        switch (holder) {
                            // Print out appropriate error log.
                            case 0 -> current.setText("Capture move with no enemies! Try again.");
                            case 2 -> current.setText("The move is not a diagonal one! Try again.");
                            case 3 -> current.setText("The destination is occupied! Try again.");
                            case 4 -> current.setText("Only kings can move backwards! Try again.");
                            case 5 -> current.setText("You have to capture! Try again.");
                            case 6 -> current.setText("Cannot capture a friendly piece! Try again.");
                            case 9 -> current.setText("Cannot move for more than two spaces! Try again.");
                        }
                    }

                    // Return to first click.
                    firstValidChoice = false;
                }
            }
        }

        /**
         * Reverse turn, and change turn indicator.
         */
        public void switchTurn() {
            board.isRedTurn = !board.isRedTurn;
            if (board.isRedTurn) {
                turn.setText("Red turn.");
                turn.setForeground(Color.red);
            } else {
                turn.setText("Black turn.");
                turn.setForeground(Color.BLACK);
            }
        }
    }

    // ================================================= Main

    /**
     * I, Hephaestus The Lionhearted,
     * Henceforth Exterminate Everything Around
     * Me That Restricts Me from Being the Master.
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
