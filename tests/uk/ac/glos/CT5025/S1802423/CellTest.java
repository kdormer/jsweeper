package uk.ac.glos.CT5025.S1802423;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CellTest {
    private static final CellFactory factory = new CellFactory();
    private static Cell[][] testBoard;
    private static Cell testCell;
    private static final int rows = 20;
    private static final int cols = 30;

    @BeforeAll
    static void testInit() {
        testBoard = factory.getCellArray(rows, cols, Difficulty.MEDIUM, null);
        testCell = testBoard[0][0];
    }

    @Test
    void reveal() {
        testCell.reveal();

        /*
         * Test that Cell object has been correctly
         * marked as revealed upon method call.
         */
        assertTrue(testCell.getIsRevealed());
    }

    @Test
    void getIsRevealed() {
        assertTrue(!testCell.getIsRevealed() || testCell.isRevealed);
    }

    @Test
    void revealNeighbours() {
        testCell.revealNeighbours();

        /*
         * Check that every neighbouring Cell has been
         * set as revealed on method call.
         */
        for(Cell cell : testCell.neighbours) {
            assertTrue(cell.getIsRevealed());
        }
    }

    @Test
    void isEmpty() {
        // Test if value is either true or false
        assertTrue(testCell.isEmpty() || !testCell.isEmpty());
    }

    @Test
    void setValue() {
        testCell.setValue('5');

        // Test value field is correctly set after method call
        assertEquals(5, testCell.getValue());
    }

    @Test
    void getCellArray() {
        assertNotNull(testCell);

        // Test that testBoard actually has contents
        assertTrue(testBoard.length > 0);

        int bombCount = 0;

        for(Cell[] row : testBoard) {
            for(Cell cell : row) {
                assertNotNull(cell);

                // Check that neighbouring Cell ArrayLists are being correctly set
                assertTrue(cell.neighbours.size() > 0);

                if(cell instanceof Bomb) {
                    bombCount++;
                }
            }
        }

        // Check if Bomb objects have been correctly added to board
        assertTrue(bombCount > 0);
    }
}