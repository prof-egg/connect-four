package profegg;

import profegg.playerclasses.AI;
import profegg.playerclasses.Human;
import java.lang.Thread;

/**
 * Hello world!
 *
 */
public class App {

    private static void AIVsAI(int sleepTime) {
        GameBoard board = new GameBoard();
        AI player = new AI('X', board, true);
        AI ai = new AI('O', board, false);
        refreshBoard(board);

        while (true) {

            board.addChip(player);
            refreshBoard(board);
            if (board.isWinner(player)) {
                System.out.println("You won!");
                break;
            }

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            board.addChip(ai);
            refreshBoard(board);
            if (board.isWinner(ai)) {
                System.out.println("You lost!");
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

    private static void HumanVsAI() {
        GameBoard board = new GameBoard();
        Human player = new Human('X', board, true);
        AI ai = new AI('O', board, false);
        refreshBoard(board);

        while (true) {

            board.addChip(player);
            refreshBoard(board);
            if (board.isWinner(player)) {
                System.out.println("You won!");
                break;
            }

            board.addChip(ai);
            refreshBoard(board);
            if (board.isWinner(ai)) {
                System.out.println("You lost!");
                break;
            }

            if (board.isFull())
                System.out.println("It's a draw!");

        }
        System.out.println(board.moveOrder());
    }

    private static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void refreshBoard(GameBoard board) {
        clearConsole();
        System.out.print("\nBOARD: \n" + board + "\n\n");
    }

    public static void main(String[] args) {

        HumanVsAI();
    }
}
