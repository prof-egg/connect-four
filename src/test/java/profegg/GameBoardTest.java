package profegg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import profegg.playerclasses.Human;

/**
 * Unit test for simple App.
 */
public class GameBoardTest {

    @Test
    public void indexAt11is0() {
        var board = new GameBoard();
        int index = board.getIndexAtPoint(1, 1);
        assertEquals(0, index);
    }

    @Test
    public void indexAt67is41() {
        var board = new GameBoard();
        int index = board.getIndexAtPoint(6, 7);
        assertEquals(41, index);
    }

    @Test
    public void isNotWinner1() {
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][ ][ ][ ][ ]
        var board = new GameBoard();
        Human p = new Human('X', board, true);
        assertFalse(board.isWinner(p));
    }

    @Test
    public void isNotWinner2() {
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][X][O][ ][ ]
        // [ ][ ][ ][X][O][ ][ ]
        // [ ][ ][ ][X][O][ ][ ]
        var board = new GameBoard();
        Human p = new Human('X', board, true);
        // board.addChip(4, 'X');
        // board.addChip(5, 'O');
        // board.addChip(4, 'X');
        // board.addChip(5, 'O');
        // board.addChip(4, 'X');
        // board.addChip(5, 'O');
        assertFalse(board.isWinner(p));
    }

    @Test
    public void isNotWinner3() {
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][ ][ ][ ][ ]
        // [X][ ][ ][O][ ][ ][ ]
        // [X][ ][ ][O][ ][ ][ ]
        // [X][ ][ ][O][ ][ ][ ]
        var board = new GameBoard();
        Human p = new Human('X', board, true);
        // board.addChip(1, 'X');
        // board.addChip(4, 'O');
        // board.addChip(1, 'X');
        // board.addChip(4, 'O');
        // board.addChip(1, 'X');
        // board.addChip(4, 'O');
        assertFalse(board.isWinner(p));
    }

    @Test
    public void isNotWinner4() {
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][O][ ][ ][X]
        // [ ][ ][ ][O][ ][ ][X]
        // [ ][ ][ ][O][ ][ ][X]
        var board = new GameBoard();
        Human p = new Human('X', board, true);
        // board.addChip(7, 'X');
        // board.addChip(4, 'O');
        // board.addChip(7, 'X');
        // board.addChip(4, 'O');
        // board.addChip(7, 'X');
        // board.addChip(4, 'O');
        assertFalse(board.isWinner(p));
    }

    @Test
    public void isWinner1() {
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][X][ ][ ][ ]
        // [ ][ ][ ][X][ ][ ][O]
        // [ ][ ][ ][X][ ][ ][O]
        // [ ][ ][ ][X][ ][ ][O]
        var board = new GameBoard();
        Human p = new Human('X', board, true);
        int[] placements = { 4, 7, 4, 7, 4, 7, 4 };
        placeChips(board, placements);
        assertTrue(board.isWinner(p));
    }

    @Test
    public void isWinner2() {
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][ ][X][ ][ ]
        // [ ][ ][X][X][O][ ][ ]
        // [ ][ ][X][O][O][ ][ ]
        // [ ][X][O][X][O][ ][ ]
        var board = new GameBoard();
        Human p = new Human('X', board, true);
        int[] placements = { 4, 4, 4, 5, 2, 3, 3, 5, 3, 5, 5 };
        placeChips(board, placements);
        assertTrue(board.isWinner(p));
    }

    @Test
    public void isWinner3() {
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][X][ ][ ][ ]
        // [ ][ ][ ][X][X][ ][ ]
        // [ ][ ][ ][O][O][X][ ]
        // [O][ ][ ][X][O][O][X]
        var board = new GameBoard();
        Human p = new Human('X', board, true);
        int[] placements = { 4, 4, 4, 5, 7, 5, 4, 6, 6, 1, 5 };
        placeChips(board, placements);
        assertTrue(board.isWinner(p));
    }

    @Test
    public void isWinner4() {
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][ ][ ][ ][ ]
        // [ ][ ][ ][O][O][ ][ ]
        // [ ][X][X][X][X][O][ ]
        var board = new GameBoard();
        Human p = new Human('X', board, true);
        int[] placements = { 4, 4, 5, 5, 3, 6, 2 };
        placeChips(board, placements);
        assertTrue(board.isWinner(p));
    }

    // Player with chip 'X' always goes first
    private void placeChips(GameBoard board, int[] placements) {
        for (int i = 0; i < placements.length; i++) {
            char chip = 'O';
            if (i % 2 == 0) chip = 'X';

            // board.addChip(placements[i], chip);
        }    
    }
}
