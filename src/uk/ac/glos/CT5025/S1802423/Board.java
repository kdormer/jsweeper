package uk.ac.glos.CT5025.S1802423;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Board extends JPanel implements MouseListener {

    private Cell[][] cells;
    private boolean gameEnd;
    private final Player p1;
    private final Player p2;
    private final int rows = 20;
    private final int cols = 30;
    private final int frameX;
    private final int frameY;
    private final Difficulty difficulty;
    private final JPanel menuPanel = new JPanel();
    private final JPanel gamePanel = new JPanel();
    private final JLabel p1ScoreLabel = new JLabel();
    private final JLabel p2ScoreLabel = new JLabel();
    private final JButton resetButton = new JButton("RESET");

    /**
     * Constructor method used to perform initialisation of instance variables, interface and game.
     *
     * @param frameX
     * @param frameY
     * @param difficulty
     * @param p1
     * @param p2
     */
    public Board(int frameX, int frameY, Difficulty difficulty, Player p1, Player p2) {
        this.frameX = frameX;
        this.frameY = frameY;
        this.difficulty = difficulty;
        this.p1 = p1;
        this.p2 = p2;
        this.interfaceInit();
        this.startGame();
    }

    /**
     * Initialises interface, including setting up layout managers, applying look and feel,
     * and placing JComponents such as JButton and JLabels.
     */
    private void interfaceInit() {
        // Set layout managers
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        menuPanel.setLayout(new BorderLayout());
        gamePanel.setLayout(new GridLayout(rows, cols));

        // Set preferred sizes for internal panels
        menuPanel.setPreferredSize(new Dimension(frameX, (frameY / 100) * 10));
        gamePanel.setPreferredSize(new Dimension(frameX, (frameY / 100) * 90));

        // Set background colours
        menuPanel.setBackground(Color.DARK_GRAY);
        gamePanel.setBackground(new Color(164, 164, 164));

        // Set panel borders
        menuPanel.setBorder(BorderFactory.createMatteBorder(15, 0, 0, 0, Color.DARK_GRAY));
        gamePanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 15));

        // Set fonts on score labels + button
        p1ScoreLabel.setFont(new Font("Tahoma", Font.BOLD, 50));
        p2ScoreLabel.setFont(new Font("Tahoma", Font.BOLD, 50));
        resetButton.setFont(new Font("Tahoma", Font.BOLD, 25));

        // Set preferred sizes for layout manager
        p1ScoreLabel.setPreferredSize(new Dimension(frameX / 3, (frameY / 100) * 10));
        p2ScoreLabel.setPreferredSize(new Dimension(frameX / 3, (frameY / 100) * 10));
        resetButton.setPreferredSize(new Dimension(frameX / 3, (frameY / 100) * 10));

        // Style reset button
        resetButton.setBackground(Color.WHITE);
        resetButton.setForeground(Color.DARK_GRAY);
        resetButton.setOpaque(true);
        resetButton.setBorderPainted(false);
        resetButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Style score labels
        p1ScoreLabel.setForeground(Color.WHITE);
        p2ScoreLabel.setForeground(Color.WHITE);

        // Center score label text
        p1ScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        p1ScoreLabel.setVerticalAlignment(SwingConstants.CENTER);
        p2ScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        p2ScoreLabel.setVerticalAlignment(SwingConstants.CENTER);

        // Set starting value to score labels
        p1ScoreLabel.setText("000");
        p2ScoreLabel.setText("000");

        // Add necessary event listeners
        resetButton.addMouseListener(this);
        this.addMouseListener(this);

        // Add components to menu panel
        menuPanel.add(p1ScoreLabel, BorderLayout.LINE_START);
        menuPanel.add(p2ScoreLabel, BorderLayout.LINE_END);
        menuPanel.add(resetButton, BorderLayout.CENTER);

        // Add panels to main Board panel
        this.add(menuPanel);
        this.add(gamePanel);
    }

    /**
     * Adds every Cell (abstract child class of JButton) to the game board.
     *
     * @param cells 2D array of Cell objects obtained from CellFactory
     */
    private void showCells(Cell[][] cells) {
        for (Cell[] cell : cells) {
            for (Cell value : cell) {
                gamePanel.add(value);
            }
        }
    }

    /**
     * Calls the Cell reveal() method on every Cell-derived object
     * in a given array.
     */
    private void revealAllCells() {
        for (Cell[] cell : cells) {
            for (Cell value : cell) {
                value.reveal();
            }
        }
    }

    /**
     * Initialises game state and starts the game.
     */
    private void startGame() {
        p1.setScore(0);
        p2.setScore(0);
        p1.setPlayerTurn(true);
        p2.setPlayerTurn(false);
        this.updateTurnLabels();
        this.updateScores();
        this.gameEnd = false;
        this.cells = (new CellFactory()).getCellArray(rows, cols, difficulty, this);
        this.showCells(cells);
    }

    /**
     * Resets game state.
     */
    private void resetGame() {
        gamePanel.removeAll();
        gamePanel.revalidate();
        gamePanel.repaint();
        p1ScoreLabel.setText("000");
        p2ScoreLabel.setText("000");
        this.resetButton.setText("RESET");
        this.startGame();
    }

    /**
     * Stops game, displays winner and updates fields such as highScore if necessary.
     */
    private void stopGame() {
        String message;
        this.gameEnd = true;
        resetButton.setForeground(Color.WHITE);
        resetButton.setBackground(new Color(175, 12, 12));

        p1.setGamesPlayed(p1.getGamesPlayed() + 1);
        p2.setGamesPlayed(p2.getGamesPlayed() + 1);

        if (p1.isPlayerTurn()) {
            message = p2.getName() + " wins!";
            p2.setWinCount(p2.getWinCount() + 1);
            p1.setLossCount(p1.getLossCount() + 1);

        } else {
            message = p1.getName() + " wins!";
            p1.setWinCount(p1.getWinCount() + 1);
            p2.setLossCount(p2.getLossCount() + 1);
        }

        if (p1.getScore() > p1.getHighScore()) {
            p1.setHighScore(p1.getScore());
        }

        if (p2.getScore() > p2.getHighScore()) {
            p2.setHighScore(p2.getScore());
        }

        if (!p1.isGuest()) {
            p1.storePlayer();
        }

        if (!p2.isGuest()) {
            p2.storePlayer();
        }

        this.resetButton.setText(message);
        this.revealAllCells();
    }

    /**
     * Switch player turn. For example, calling this method changes the game state
     * from Player 1's turn to Player 2's turn.
     */
    private void switchPlayer() {
        p1.switchPlayerTurn();
        p2.switchPlayerTurn();
    }

    /**
     * Updates the colours of each player's score JLabel
     * to indicate the current turn.
     */
    private void updateTurnLabels() {
        if (p1.isPlayerTurn()) {
            p1ScoreLabel.setForeground(new Color(175, 12, 12));
            p2ScoreLabel.setForeground(Color.WHITE);


        } else if (p2.isPlayerTurn()) {
            p2ScoreLabel.setForeground(new Color(175, 12, 12));
            p1ScoreLabel.setForeground(Color.WHITE);

        }
    }

    /**
     * Updates score JLabel values based on new player scores.
     */
    private void updateScores() {
        p1ScoreLabel.setText(this.getScoreString(p1.getScore()));
        p2ScoreLabel.setText(this.getScoreString(p2.getScore()));
    }

    /**
     * Returns string to be displayed in score JLabel based on passed player score.
     *
     * @param score Player object score value.
     * @return formatted string to be displayed in score JLabel.
     */
    private String getScoreString(int score) {
        String scoreString = Integer.toString(score);

        if (score < 10) {
            return "00" + scoreString;
        } else if (score < 100) {
            return "0" + scoreString;
        } else {
            return scoreString;
        }
    }

    /**
     * Checks current player turn and adds the passed amount
     * to the relevant Player object's score field.
     *
     * @param score integer value of Player object's score field.
     */
    private void addScore(int score) {
        if (score >= 1) {
            if (p1.isPlayerTurn()) {
                p1.addScore(score);

            } else {
                p2.addScore(score);
            }
        }
    }

    /**
     * Implementation of MouseListener's mouseClicked method.
     * Handles mouse click event on Board Cells and on the reset JButton.
     *
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() instanceof Bomb && !gameEnd) {
            this.stopGame();

        } else if (e.getSource() instanceof Cell && !gameEnd) {
            Cell targetCell = (Cell) e.getSource();

            if (!targetCell.isRevealed) {
                targetCell.reveal();

                if (targetCell.isEmpty()) {
                    targetCell.revealNeighbours();

                    for (Cell cell : targetCell.neighbours) {
                        this.addScore(cell.getValue());
                    }

                } else {
                    this.addScore(targetCell.getValue());
                }

                this.updateScores();

                if (--Cell.hiddenCells == Cell.bombCount) {
                    this.stopGame();

                } else {
                    this.switchPlayer();
                    this.updateTurnLabels();
                }
            }


        } else if (e.getSource() == this.resetButton) {
            this.resetGame();
        }
    }


    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * Implementation of MouseListener interface's mouseEntered() method.
     * Handles colour changes when mouse enters specified objects such as Cell-derived objects.
     *
     * @param e Event object
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == resetButton) {
            resetButton.setForeground(Color.WHITE);
            resetButton.setBackground(new Color(175, 12, 12));

        } else if (e.getSource() instanceof Cell && !gameEnd) {
            Cell targetCell = (Cell) e.getSource();

            if (!targetCell.getIsRevealed()) {
                targetCell.setBackground(new Color(164, 164, 164));
            }
        }
    }

    /**
     * Implementation of MouseListener interface's mousedExited() method.
     * Handles colour changes when mouse leaves components such as Cell-derived objects.
     *
     * @param e Event object
     */
    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == resetButton && !gameEnd) {
            resetButton.setBackground(Color.WHITE);
            resetButton.setForeground(Color.DARK_GRAY);

        } else if (e.getSource() instanceof Cell && !gameEnd) {
            ((Cell) e.getSource()).setBackground(new Color(174, 174, 174));
        }
    }
}