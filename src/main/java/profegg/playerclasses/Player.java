package profegg.playerclasses;

import profegg.ConnectFourBoard;

public abstract class Player {

    ConnectFourBoard board;

    Player(ConnectFourBoard board) {
        this.board = board;
    }

    public abstract int getMove();
}
