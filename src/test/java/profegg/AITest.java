package profegg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import profegg.playerclasses.Human;

/**
 * Unit test for simple App.
 */
public class AITest {

    @Test
    public void getCorrectOpponentMove1() {
        var board = new GameBoard("44");
        int index = board.getIndexAtPoint(1, 1);
        assertEquals(new Point(index, index), index);
    }


}
