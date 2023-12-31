package profegg.playerclasses;

import profegg.ConnectFourBoard;

public abstract class Player {

    private static int availablePlayerID = 1;

    protected final int playerID;

    protected ConnectFourBoard board;

    Player(ConnectFourBoard board) {
        this.board = board;
        this.playerID = availablePlayerID++;
    }

    public abstract int getMove();
}
