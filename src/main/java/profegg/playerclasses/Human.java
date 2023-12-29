package profegg.playerclasses;

import java.util.Scanner;

import profegg.GameBoard;

public class Human extends Player {

    Scanner input;

    public Human(char chip, GameBoard board, boolean first) {
        super(chip, board, first);
        input = new Scanner(System.in);
    }

    public int getMove() {
        return promptForMove();
    }

    private int promptForMove() {

        /*
         * NOTE: Handle stuff like ^Z
         * and make it so you can quit with -1
         */
        System.out.print("Pick a column: ");
        
        try {
            int move = input.nextInt();
            if (move < 0 || move > 7 || !board.columnIsOpen(move)) throw new Exception();
            return move;
        } catch (Exception e) {
            System.out.println("Please enter a valid column between 1 and 7 where the column isn't full!\n");
            input.nextLine(); // Swallow current input line to clear input buffer
            return promptForMove(); // reprompt user
        }
    }

}
