package profegg;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class ConnectFourBoardTest {

    @Test
    public void correctMoveHistory1() {
        ConnectFourBoard board = new ConnectFourBoard("456");
        
        List<Point> correctHistoryList = new ArrayList<>(Arrays.asList(new Point[] {
            new Point(6, 4),
            new Point(6, 5),
            new Point(6, 6),
        }));

        assertTrue(board.copyMoveHistory().equals(correctHistoryList));
    }

    @Test
    public void correctP1MoveHistory1() {
        ConnectFourBoard board = new ConnectFourBoard("456");
        
        List<Point> correctHistoryList = new ArrayList<>(Arrays.asList(new Point[] {
            new Point(6, 4),
            new Point(6, 6),
        }));

        assertTrue(board.copyP1MoveHistory().equals(correctHistoryList));
    }
    
    @Test
    public void correctP2MoveHistory1() {
        ConnectFourBoard board = new ConnectFourBoard("456");
        
        List<Point> correctHistoryList = new ArrayList<>(Arrays.asList(new Point[] {
            new Point(6, 5)
        }));

        assertTrue(board.copyP2MoveHistory().equals(correctHistoryList));
    }

    @Test
    public void correctMoveHistoryWithUnadding1() {
        ConnectFourBoard board = new ConnectFourBoard("4567");
        board.unAddChip();
        
        List<Point> correctHistoryList = new ArrayList<>(Arrays.asList(new Point[] {
            new Point(6, 4),
            new Point(6, 5),
            new Point(6, 6),
        }));

        assertTrue(board.copyMoveHistory().equals(correctHistoryList));
    }

    @Test
    public void correctP1MoveHistoryWithUnadding1() {
        ConnectFourBoard board = new ConnectFourBoard("4567");
        board.unAddChip();
        
        List<Point> correctHistoryList = new ArrayList<>(Arrays.asList(new Point[] {
            new Point(6, 4),
            new Point(6, 6),
        }));

        assertTrue(board.copyP1MoveHistory().equals(correctHistoryList));
    }
    
    @Test
    public void correctP2MoveHistoryWithUnadding1() {
        ConnectFourBoard board = new ConnectFourBoard("4567");
        board.unAddChip();
        
        List<Point> correctHistoryList = new ArrayList<>(Arrays.asList(new Point[] {
            new Point(6, 5)
        }));

        assertTrue(board.copyP2MoveHistory().equals(correctHistoryList));
    }

    @Test
    public void playerOneToMove1() {
        ConnectFourBoard board = new ConnectFourBoard("741253");
        assertTrue(board.playerOneToMove());
    }

    @Test
    public void playerOneToMove2() {
        ConnectFourBoard board = new ConnectFourBoard();
        assertTrue(board.playerOneToMove());
    }

    @Test
    public void playerOneToMove3() {
        ConnectFourBoard board = new ConnectFourBoard("4455");
        board.unAddChip();
        board.unAddChip();
        assertTrue(board.playerOneToMove());
    }

    @Test
    public void playerOneToMove4() {
        ConnectFourBoard board = new ConnectFourBoard("4455");
        board.unAddChip();
        board.unAddChip();
        assertTrue(board.playerOneToMove());
    }

    @Test
    public void playerOneToMove5() {
        ConnectFourBoard board = new ConnectFourBoard("4455");
        board.unAddChip();
        board.unAddChip();
        assertTrue(board.playerOneToMove());
    }
}
