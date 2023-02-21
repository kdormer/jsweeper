package uk.ac.glos.CT5025.S1802423;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    // Width + height of usable space - excludes frame
    private static final int SIZE_X = 1200;
    private static final int SIZE_Y = 800;

    private final Player p1;
    private final Player p2;
    private final Difficulty difficulty;

    public GameFrame(Player p1, Player p2, Difficulty difficulty) {
        super("JSweeper");
        this.p1 = p1;
        this.p2 = p2;
        this.difficulty = difficulty;
        this.setFrame();
        this.showFrame();
    }

    /**
     * Sets GameFrame's content pane to a new instance of the Board class, which extends JPane.
     * The Board class' constructor then performs necessary initialisation.
     */
    private void setFrame() {
        this.setContentPane(new Board(SIZE_X, SIZE_Y, difficulty, p1, p2));
    }

    /**
     * Performs necessary operations to display the JFrame.
     * Size of JFrame is mindful of frame size, obtained using the Insets class.
     */
    private void showFrame() {
        Insets insets = this.getInsets();
        this.setSize(SIZE_X + insets.left + insets.right,
                SIZE_Y + insets.top + insets.bottom);

        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
