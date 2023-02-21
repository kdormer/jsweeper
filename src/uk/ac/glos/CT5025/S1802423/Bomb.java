package uk.ac.glos.CT5025.S1802423;

import java.awt.*;

public class Bomb extends Cell {
    /**
     * Override parent Cell constructor to set value to a 0 character.
     */
    public Bomb() {
        super();
        this.value = 'O';
    }

    /**
     * Override parent Cell reveal() method to set the background to red.
     */
    @Override
    public void reveal() {
        super.reveal();
        this.setBackground(new Color(175, 12, 12));
    }
}