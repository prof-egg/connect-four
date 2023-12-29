package profegg;

import java.util.ArrayList;
import java.util.List;

import profegg.playerclasses.Player;

public class GameBoard {

    /*
     * STUFF TO ADD:
     * - Better draw detection
     * - Colored pieces
     */

    private char[] board;
    /* indecies correspond to columns
    // and values represent how many open 
    // spaces are left in that column*/
    private int[] openColumnSpaces; 

    public final int rows = 6;
    public final int columns = 7;

    private int moveCount;
    private String moveOrder;

    private ArrayList<Point> moveHistory;
    private ArrayList<Point> playerOneMoveHistory;
    private ArrayList<Point> playerTwoMoveHistory;

    public GameBoard() {
        board = new char[columns * rows];
        for (int i = 0; i < board.length; i++)
            board[i] = ' ';

        openColumnSpaces = new int[columns];
        for (int i = 0; i < openColumnSpaces.length; i++)
            openColumnSpaces[i] = 6;

        moveCount = 0;
        moveOrder = "";

        moveHistory = new ArrayList<>();
        playerOneMoveHistory = new ArrayList<>();
        playerTwoMoveHistory = new ArrayList<>();
    }

    public GameBoard(String moveOrder) {
        this();

        for (int i = 0; i < moveOrder.length(); i++) {
            int num = moveOrder.charAt(i) - '0';
            addChip(num);
            moveCount++;
        }
        this.moveOrder = moveOrder;
    }

    public boolean isWinner(Player p) {

        char winChip = p.chip();

        if (verticalWinExist(winChip)) return true;
        if (horizontalWinExist(winChip)) return true;
        if (forwardDiagnalWinExist(winChip)) return true;
        if (backwardDiagnalWinExist(winChip)) return true;
           
        return false;
    }

    public int[] copyHeights() {
        int[] thing = new int[columns];
        for (int i = 0; i < columns; i++) 
            thing[i] = openColumnSpaces[i];
        return thing;
    }

    public List<Point> copyMoveHistory() {
        return new ArrayList<Point>(moveHistory);
    }
    public List<Point> copyP1MoveHistory() {
        return new ArrayList<Point>(playerOneMoveHistory);
    }
    public List<Point> copyP2MoveHistory() {
        return new ArrayList<Point>(playerTwoMoveHistory);
    }
    /*
     * NOTE: This is used for draw detection, first check
     * if there is any winners, then call this.
     * Making better draw detection is on my wishlist.
     */
    public boolean isFull() {
        return moveCount == board.length;
    }

    public int moveCount() {
        return moveCount;
    }

    public boolean playerOneToMove() {
        return moveCount % 2 == 0;
    }

    /**
     * 
     * @param col
     * @return
     * @throws IllegalArgumentException If (col < 0 || col > 7)
     */
    public void addChip(int col) {
        if (!columnIsOpen(col)) throw new RuntimeException("Column " + col + " appears full, can't add chip.");

        int row = openSpacesAtColumn(col);
        board[getIndexAtPoint(row, col)] = getCurrentChip();

        openColumnSpaces[col - 1]--;
        moveOrder += col;
        moveCount++;

        Point p = new Point(row, col);
        moveHistory.add(p);
        if (moveCount % 2 == 0) playerOneMoveHistory.add(p);
        else playerTwoMoveHistory.add(p);
    }

    public void unAddChip() {
        // String playerMoveHistory = (playerOneToMove()) ? "playerTwoMoveHistory" : "playerOneMoveHistory";
        // This doesnt make much sense, I feel like it should be the other way around like the comment above
        // But works only like this
        ArrayList<Point> playerMoveHistory = (playerOneToMove()) ? playerOneMoveHistory : playerTwoMoveHistory;
        // System.out.println(playerMoveHistory);
        
        playerMoveHistory.removeLast();
        Point p = moveHistory.removeLast();
        moveOrder = moveOrder.substring(0, moveOrder.length() - 1);
        openColumnSpaces[p.col - 1]++;
        moveCount--;
        board[getIndexAtPoint(p.row, p.col)] = ' ';
    }   

    public void addChip(Player p) {
        int col = p.getMove();
        int row = openSpacesAtColumn(col);
        board[getIndexAtPoint(row, col)] = getCurrentChip();
        openColumnSpaces[col - 1]--;
        moveOrder += col;
        moveCount++;
        moveHistory.add(new Point(row, col));
    }

    private char getCurrentChip() {
        return (playerOneToMove()) ? 'X' : 'O';
    }

    public List<Integer> getOpenColumns() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < openColumnSpaces.length; i++) {
            int col = i + 1;
            if (columnIsOpen(col)) list.add(col);
        }
        return list;
    }

    /**
     * 
     * @param col
     * @return
     * @throws IllegalArgumentException If (col < 0 || col > 7)
     */
    public boolean columnIsOpen(int col) {
        if (openSpacesAtColumn(col) != 0) return true;
        return false;
    }

    /**
     * 
     * @param col
     * @return
     * @throws IllegalArgumentException If (col < 0 || col > 7)
     */
    public int openSpacesAtColumn(int col) {
        verifyValidColumn(col);
        return openColumnSpaces[col - 1];
    }

    /**
     * Returns 2d row (of size 6) col (of size 7) point as 1d array index
     * @param row row of the board such as if the board was represented as a 2d array
     * @param col column of the board such as if the board was represented as a 2d array
     * @return 2d row (of size 6) col (of size 7) point as 1d array index
     * @throws IllegalArgumentException If (row < 0 || col < 0 || row > 6 || col > 7)
     */
    public int getIndexAtPoint(int row, int col) {
        verifyValidPoint(row, col);
        return (row - 1) * 7 + col - 1;
    }

    /**
     * Returns the value of board such as if it was represented as a 2d array (char[6][7])
     * @param row row of the board such as if the board was represented as a 2d array
     * @param col column of the board such as if the board was represented as a 2d array
     * @return the value of board such as if it was represented as a 2d array (char[6][7])
     * @throws IllegalArgumentException If (row < 0 || col < 0 || row > 6 || col > 7)
     */
    public char getChipAtPoint(int row, int col) {
        return board[getIndexAtPoint(row, col)];
    }

    public String moveOrder() {
        return moveOrder;
    }

    private boolean verticalWinExist(char winChip) {
        for (int col = 1; col <= columns; col++) {
            int tick = 0;
            for (int row = 1; row <= rows; row++) {
                char chip = getChipAtPoint(row , col);
                if (chip == winChip) {
                    tick++;
                    if (tick == 4) return true;
                } else {
                    if (row > 3) break;
                    tick = 0;
                }
            }
        }  
        return false;
    }

    private boolean horizontalWinExist(char winChip) {
        for (int row = 1; row <= rows; row++) {
            int tick = 0;
            for (int col = 1; col <= columns; col++) {
                char chip = getChipAtPoint(row , col);
                if (chip == winChip) {
                    tick++;
                    if (tick == 4) return true;
                } else {
                    if (col > 4) break;
                    tick = 0;
                }
            }
        }  
        return false;
    }

    private boolean forwardDiagnalWinExist(char winChip) {
        // Diagnals that fit (lower case x indicates where to start search)
        // [ ][ ][ ][X][X][X][X]
        // [ ][ ][X][X][X][X][X]
        // [ ][X][X][X][X][X][X]
        // [x][X][X][X][X][X][ ]
        // [x][X][X][X][X][ ][ ]
        // [x][x][x][x][ ][ ][ ]

        // Start points for search
        int[] startRows = { 4, 5, 6, 6, 6, 6 };
        int[] startCols = { 1, 1, 1, 2, 3, 4 };
        int[] possibleConfigurations = { 1, 2, 3, 3, 2, 1 };

        for (int i = 0; i < startRows.length; i++) {
            int row = startRows[i];
            int col = startCols[i];

            int tick = 0;
            int spacesTraveled = 1; // counting the starting point as 1 space traveled
            while (row > 0 && col < 8) {

                char chip = getChipAtPoint(row , col);
                if (chip == winChip) {
                    tick++;
                    if (tick == 4) return true;
                } else {
                    if (possibleConfigurations[i] < spacesTraveled) 
                        break;
                    tick = 0;
                }

                // make the point go up 1 right 1 (from the perspective of a player)
                row--;
                col++;

                spacesTraveled++;
            }
        }
        return false;
    }

    private boolean backwardDiagnalWinExist(char winChip) {
        // Diagnals that fit (lower case x indicates where to start search)
        // [X][X][X][X][ ][ ][ ]
        // [X][X][X][X][X][ ][ ]
        // [X][X][X][X][X][X][ ]
        // [ ][X][X][X][X][X][x]
        // [ ][ ][X][X][X][X][x]
        // [ ][ ][ ][x][x][x][x]

        // Start points for search
        int[] startRows = { 6, 6, 6, 6, 5, 4 };
        int[] startCols = { 4, 5, 6, 7, 7, 7 };
        int[] possibleConfigurations = { 1, 2, 3, 3, 2, 1 };

        for (int i = 0; i < startRows.length; i++) {
            int row = startRows[i];
            int col = startCols[i];

            int tick = 0;
            int spacesTraveled = 1; // counting the starting point as 1 space traveled
            while (row > 0 && col > 0) {

                char chip = getChipAtPoint(row , col);
                if (chip == winChip) {
                    tick++;
                    if (tick == 4) return true;
                } else {
                    if (possibleConfigurations[i] < spacesTraveled) 
                        break;
                    tick = 0;
                }

                // make the point go up 1 left 1 (from the perspective of a player)
                row--;
                col--;

                spacesTraveled++;
            }
        }
        return false;
    }

    private void verifyValidPoint(int row, int col) {
        if (row < 0 || col < 0 || row > 6 || col > 7) throw new IllegalArgumentException("(" + row + ", " + col + ")");
    }

    private void verifyValidColumn(int col) {
        if (col < 0 || col > 7) throw new IllegalArgumentException("(" + col + ")");
    }
    
    public String toString() {
        String build = "";
        for (int i = 0; i < board.length; i++) {

            // BOARD EXAMPLE
            // [ ][ ][ ][ ][ ][ ][ ]
            // [ ][ ][ ][ ][ ][ ][ ]
            // [ ][ ][ ][O][ ][ ][ ]
            // [ ][ ][ ][X][X][ ][ ]
            // [ ][ ][ ][X][O][ ][ ]
            // [ ][ ][ ][X][O][ ][ ]

            boolean lastColInRow = (i + 1) % 7 == 0;

            String color = Color.FG_CYAN;
            if (board[i] == 'X') color = Color.FG_RED;
            build += "[" + color + board[i] + Color.RESET + "]";

            if (lastColInRow && i != board.length - 1) build += "\n";
        }
        return build;
    }
}
