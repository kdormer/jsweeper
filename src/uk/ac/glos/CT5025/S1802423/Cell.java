package uk.ac.glos.CT5025.S1802423;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class Cell extends JButton {
    public static int hiddenCells = 0;
    public static int bombCount = 0;

    public ArrayList<Cell> neighbours = new ArrayList<>(8);
    protected boolean isRevealed = false;
    protected char value;

    /**
     * Constructor method used to set look and feel
     * using inherited JButton/JComponent methods.
     */
    public Cell() {
        this.setPreferredSize(new Dimension(40, 40));
        this.setBorder(BorderFactory.createRaisedBevelBorder());
        this.setBackground(new Color(174, 174, 174));
        this.setOpaque(true);
        this.setFont(new Font("Tahoma", Font.PLAIN, 24));
    }

    /**
     * Reveal cell called when Cell is clicked.
     * Set displayed text to Cell value field and
     * remove raised bevel border.
     * Colour of set text is set based on value.
     */
    public void reveal() {
        if (this.value != 'O' && this.value != ' ') {
            switch (this.value) {
                case '1':
                    this.setForeground(Color.BLUE);
                    break;
                case '2':
                    this.setForeground(new Color(48, 200, 1));
                    break;
                case '3':
                    this.setForeground(Color.RED);
                    break;
                case '4':
                    this.setForeground(new Color(0, 0, 128));
                    break;
                case '5':
                    this.setForeground(new Color(178, 34, 34));
                    break;
                case '6':
                    this.setForeground(new Color(72, 209, 204));
                    break;
                case '7':
                    this.setForeground(Color.BLACK);
                    break;
                case '8':
                    this.setForeground(Color.DARK_GRAY);
                    break;
            }
        }

        this.setBorder(BorderFactory.createEmptyBorder());
        this.setText(Character.toString(value));
        this.isRevealed = true;
    }

    public boolean getIsRevealed() {
        return this.isRevealed;
    }

    /**
     * Call's reveal() method on each Cell object stored in
     * the neighbours array. Reveals all neighbours.
     */
    public void revealNeighbours() {
        for (Cell cell : neighbours) {
            cell.reveal();
        }
    }

    public boolean isEmpty() {
        return this.value == ' ';
    }

    public int getValue() {
        return Character.digit(this.value, 10);
    }

    public void setValue(char value) {
        if (value != '0') {
            this.value = value;
        } else {
            this.value = ' ';
        }
    }
}