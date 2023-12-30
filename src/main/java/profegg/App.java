package profegg;

import profegg.playerclasses.AI;
import profegg.playerclasses.Human;
import profegg.playerclasses.Player;

import java.lang.Thread;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        PvP(true, false, 0);
    }

    private static void PvP(boolean p1Human, boolean p2Human, int sleepTime) {

        ConnectFourBoard board = new ConnectFourBoard();
        board.equals(board);
        Player p1 = (p1Human) ? new Human('X', board, true) : new AI('X', board, true);
        Player p2 = (p2Human) ? new Human('O', board, false) : new AI('O', board, false);

        refreshBoard(board);

        while (true) {

            board.addChip(p1);
            refreshBoard(board);
            if (board.isWinner(true)) {
                System.out.println("Player 1 wins!");
                break;
            }

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            board.addChip(p2);
            refreshBoard(board);
            if (board.isWinner(false)) {
                System.out.println("Player 2 wins!");
                break;
            }

            if (board.isFull())
                System.out.println("It's a draw!");

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println(board.moveOrder());
    }

    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void refreshBoard(ConnectFourBoard board) {
        clearConsole();
        System.out.print("\nBOARD: \n" + board + "\n\n");
    }

}
