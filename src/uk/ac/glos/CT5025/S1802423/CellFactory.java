package uk.ac.glos.CT5025.S1802423;

import java.awt.event.MouseListener;
import java.util.Random;

public class CellFactory {
    /**
     * Base factory method that returns new Cell-derived object based on a specified String.
     *
     * @param cellType String value according to which Cell-derived object should be returned.
     * @return New Cell-derived object, either Number or Bomb.
     */
    private Cell getCell(String cellType) {
        return (cellType.equalsIgnoreCase("BOMB")) ? new Bomb() : new Number();
    }

    /**
     * Returns array of Cell-derived objects ready to be used in composition with the Board class.
     *
     * @param rows       Integer specifying rows of board.
     * @param cols       Integer specifying columns of board.
     * @param difficulty Difficulty enum type specifying Difficulty of game in the form of bomb amount.
     * @param listener   Event listener to be applied to each Cell on the board.
     * @return 2D array of initialised Cell-derived objects (Bombs & Numbers).
     */
    public Cell[][] getCellArray(int rows, int cols, Difficulty difficulty, MouseListener listener) {
        Cell[][] cells = new Cell[rows][cols];
        Random rnd = new Random();
        int bombModifier = 0;

        switch (difficulty) {
            case EASY:
                bombModifier = 10;
                break;
            case MEDIUM:
                bombModifier = 20;
                break;
            case HARD:
                bombModifier = 50;
                break;
            case IMPOSSIBLE:
                bombModifier = 75;
                break;
        }

        /*
         * Populate array with initialised Cell objects with Bombs
         * being randomly added according to a difficulty modifier
         * corresponding with the enum Difficulty type passed.
         */
        if (bombModifier > 0) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (rnd.nextInt(100) < bombModifier) {
                        Cell.bombCount += 1;
                        cells[i][j] = this.getCell("BOMB");
                    } else {
                        cells[i][j] = this.getCell("NUMBER");
                    }

                    Cell.hiddenCells += 1;
                    cells[i][j].addMouseListener(listener);
                }
            }

            /*
             * Loop through now initialised Cell array and count neighbours of each cell
             * which are then added to an array in each Cell and the total amount of bombs is then set
             * as the Cell's value.
             * This enables the functionality of clicking a cell and knowing how many bombs are
             * surrounding it.
             */
            for (int row = 0; row < cells.length; row++) {
                for (int col = 0; col < cells[row].length; col++) {

                    int bombNeighbourCount = 0;

                    /*
                     * Loops through each neighbouring array index both adjacent to and diagonally next to
                     * a Cell in a given matrix/2D array.
                     */
                    for (int xN = row - 1; xN <= row + 1; xN++) {
                        for (int yN = col - 1; yN <= col + 1; yN++) {

                            /*
                             * Check if index actually exists in array to prevent
                             * ArrayIndexOutOfBoundsException from being thrown.
                             */
                            if ((xN >= 0 && yN >= 0) && (xN < cells.length && yN < cells[row].length)) {
                                cells[row][col].neighbours.add(cells[xN][yN]);

                                if (cells[xN][yN] instanceof Bomb && !(cells[row][col] instanceof Bomb)) {
                                    bombNeighbourCount++;
                                }
                            }
                        }
                    }

                    /*
                     * Set each non-Bomb Cell object's value to number of
                     * neighbouring bombs.
                     */
                    if (!(cells[row][col] instanceof Bomb)) {
                        char value = Character.forDigit(bombNeighbourCount, 10);
                        cells[row][col].setValue(value);
                    }
                }
            }
        }

        return cells;
    }
}
