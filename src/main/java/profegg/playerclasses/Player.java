package profegg.playerclasses;

import profegg.GameBoard;

public abstract class Player {

    char chip; // 'X' or 'O' preferably
    GameBoard board;
    boolean first;

    Player(char chip, GameBoard board, boolean first) {
        this.chip = chip;
        this.board = board;
        this.first = first;
    }

    public abstract int getMove();

    public char chip() {
        return chip;
    }
}
