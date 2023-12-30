package profegg;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import profegg.playerclasses.Player;

public class ConnectFourBoard {

    /*
     * STUFF TO ADD:
     * - Better draw detection
     * - Better win detection
     * - When there is a win on the board color the pieces yellow
     * - the equals method
     */

    public final int rows = 6;     // Board rows
    public final int columns = 7;  // Board columns

    private char[] board;            // Treated as if it were a 2d array
    private int[] openColumnSpaces;  // Keeps track of the open spaces in each column (empty col = 6, full col = 0)

    private int moveCount;     // How many moves have been made so far
    private String moveOrder;  // Moves recorded as a seiries of column placements

    private ArrayList<Point> moveHistory;           // The complete move history of the game recorded as a list of points
    private ArrayList<Point> playerOneMoveHistory;  // The complete move history of the first player recoreded as a list of points
    private ArrayList<Point> playerTwoMoveHistory;  // The complete move history of the second player recoreded as a list of points

    /**Initializes an empty connect four board. */
    public ConnectFourBoard() {
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


    /**
     * Initializes a connect four board from {@code moveOrder} 
     * @param moveOrder the order of moves which have been played
     * @throws IllegalArgumentException if any number provided is not between 1 and 7
     * @throws ArrayIndexOutOfBoundsException if column is full
     */
    public ConnectFourBoard(String moveOrder) {
        this();

        for (int i = 0; i < moveOrder.length(); i++) 
            addChip(moveOrder.charAt(i) - '0');
    }

    /**
     * Adds a chip to the column provided and returns the point where it landed.
     * @param col the column you would like to add a chip to
     * @return the point where the chip was added
     * @throws IllegalArgumentException if ({@code col < 1 || col > 7})
     * @throws ArrayIndexOutOfBoundsException if column is full
     */
    public Point addChip(int col) {

        if (!columnIsOpen(col)) throw new ArrayIndexOutOfBoundsException("Column " + col + " appears full, can't add chip");

        int row = openSpacesAtColumn(col);

        board[getIndexAtPoint(row, col)] = getCurrentChip();
        openColumnSpaces[col - 1]--;

        moveOrder += col;
        moveCount++;

        Point point = new Point(row, col);
        moveHistory.add(point);
        if (playerOneToMove()) playerOneMoveHistory.add(point);
        else playerTwoMoveHistory.add(point);

        return point;
    }

    /**
     * Gets a column from {@code player} to add a chip to and returns the point where the chip landed.
     * @param player the player that you would like to get a move from
     * @return the point where the chip was added
     * @throws IllegalArgumentException if ({@code col < 1 || col > 7})
     * @throws ArrayIndexOutOfBoundsException if column is full
     */
    public Point addChip(Player player) {

        int col = player.getMove();
        if (!columnIsOpen(col)) throw new ArrayIndexOutOfBoundsException("Column " + col + " appears full, can't add chip");

        int row = openSpacesAtColumn(col);

        board[getIndexAtPoint(row, col)] = getCurrentChip();
        openColumnSpaces[col - 1]--;

        moveOrder += col;
        moveCount++;

        Point point = new Point(row, col);
        moveHistory.add(point);
        if (playerOneToMove()) playerOneMoveHistory.add(point);
        else playerTwoMoveHistory.add(point);

        return point;
    }

    /**
     * Removes the last chip from the board and returns the point at which it was remove.
     * @return the point at which the chip was removed
     * @throws NoSuchElementException if the board is empty
     */
    public Point unAddChip() {

        if (moveCount < 1) throw new NoSuchElementException("Attempted to unadd chip when board was empty");

        // String playerMoveHistory = (playerOneToMove()) ? "playerTwoMoveHistory" : "playerOneMoveHistory";
        // This doesnt make much sense, I feel like it should be the other way around like the comment above
        // since if its player 1 to move, that means the last move to be made was by player two, but it onl works
        // like this, this may be a bug or a i may be stupid
        ArrayList<Point> playerMoveHistory = (playerOneToMove()) ? playerOneMoveHistory : playerTwoMoveHistory;        
        playerMoveHistory.removeLast();
        Point point = moveHistory.removeLast();
        
        moveOrder = moveOrder.substring(0, moveOrder.length() - 1);
        moveCount--;

        board[getIndexAtPoint(point.row, point.col)] = ' ';
        openColumnSpaces[point.col - 1]++;

        return point;
    }   

    /**
     * Returns {@code true} if there is a connect four for the specified player, else {@code false}.
     * @param p1 if this is true, method will look {@code player 1 wins}, else will look for {@code player 2 wins}
     * @return {@code true} if there is a connect four for the specified player, else {@code false}
     */
    public boolean isWinner(boolean p1) {

        char winChip = (p1) ? 'X' : 'O';

        if (verticalWinExist(winChip)) return true;
        if (horizontalWinExist(winChip)) return true;
        if (forwardDiagnalWinExist(winChip)) return true;
        if (backwardDiagnalWinExist(winChip)) return true;
           
        return false;
    }

    /**
     * Returns a list of the open available columns.
     * @return a list of the open available columns
     */
    public List<Integer> getOpenColumns() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < openColumnSpaces.length; i++) {
            int col = i + 1;
            if (columnIsOpen(col)) list.add(col);
        }
        return list;
    }

    /**
     * Returns {@code true} if {@code col} is open, else {@code false}.
     * @param col the column you want to check if is open
     * @return {@code true} if {@code col} is open, else {@code false}
     * @throws IllegalArgumentException if ({@code col < 1 || col > 7})
     */
    public boolean columnIsOpen(int col) {
        if (openSpacesAtColumn(col) != 0) return true;
        return false;
    }

    /**
     * Returns the amount of open slots in {@code col}.
     * @param col the column you want to get the amount of open space at
     * @return the amount of open slots in {@code col}
     * @throws IllegalArgumentException if ({@code col < 1 || col > 7})
     */
    public int openSpacesAtColumn(int col) {
        verifyValidColumn(col);
        return openColumnSpaces[col - 1];
    }

    /**
     * Returns the char on the board at point {@code p}.
     * @param p the point on the board you want to get
     * @return the char on the board at point {@code p}
     * @throws IllegalArgumentException if ({@code row < 1 || col < 1 || row > 6 || col > 7})
     */
    public char getChipAtPoint(Point p) {
        return board[getIndexAtPoint(p)];
    }

    /**
     * Returns the char on the board at point {@code row}, {@code column}. 
     * @param row row of the board where row 1 is the topmost row and row 6 is the bottommost row
     * @param col column of the board where column 1 is the leftmost column and column 7 is the righmost column
     * @return the char on the board at point row, column
     * @throws IllegalArgumentException if ({@code row < 1 || col < 1 || row > 6 || col > 7})
     */
    public char getChipAtPoint(int row, int col) {
        return board[getIndexAtPoint(row, col)];
    }

    /**
     * Returns an array of <em>heights</em> where each index is 
     * {@code col - 1} (so index 0 corresponds to column 1), and 
     * each value represents how many open spaces are in that column.
     * 
     * @return an array of heights.
     */
    public int[] copyHeights() {
        int[] thing = new int[columns];
        for (int i = 0; i < columns; i++) 
            thing[i] = openColumnSpaces[i];
        return thing;
    }

    /**
     * Returns a shallow list copy of the move history.
     * @return a shallow copy of the move history
     */
    public List<Point> copyMoveHistory() {
        return new ArrayList<Point>(moveHistory);
    }

    /**
     * Returns a shallow list copy of player one's move history.
     * @return a shallow copy of player one's move history
     */
    public List<Point> copyP1MoveHistory() {
        return new ArrayList<Point>(playerOneMoveHistory);
    }

    /**
     * Returns a shallow list copy of player two's move history.
     * @return a shallow copy of player two's move history
     */
    public List<Point> copyP2MoveHistory() {
        return new ArrayList<Point>(playerTwoMoveHistory);
    }

    /*
     * NOTE: This is used for draw detection, first check
     * if there is any winners, then call this.
     * Making better draw detection is on my wishlist.
     */
    /**
     * Returns true if the board is full.
     * @return true if the board is full
     */
    public boolean isFull() {
        return moveCount == board.length;
    }

    /**
     * Returns {@code true} if it is player one's turn, else {@code false}. 
     * <em>NOTE: Will return {@code false} if the game is over.</em>
     * 
     * @return {@code true} if it is player one's turn, else {@code false}
     */
    public boolean playerOneToMove() {
        return moveCount % 2 == 0;
    }

    /**
     * Returns the order of columns played in.
     * @return the order of columns played in
     */
    public String moveOrder() {
        return moveOrder;
    }

    /**
     * Returns the amount of moves played so far.
     * @return the amount of moves played so far
     */
    public int moveCount() {
        return moveCount;
    }

    /**
     * Returns a string representation of this board.
     *
     * @return the board with empty slots marked as {@code [ ]}, and filled 
     * slots with either {@code [X]} or {@code [O]}
     */
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

            String color = (board[i] == 'X') ? Color.FG_RED : Color.FG_CYAN;
            build += "[" + color + board[i] + Color.RESET + "]";

            if (lastColInRow && i != board.length - 1) build += "\n";
        }
        return build;
    }

    /**
     * Compares this connect four board to the specified object. The result 
     * is {@code true} if and only if the argument is not {@code null} and 
     * is a {@code ConnectFourBoard} object that represents the same board 
     * position.
     * 
     * @param otherObject The object to compare this 
     * {@code ConnectFourBoard} against
     * 
     * @return {@code true} if the given object represents a {@code ConnectFourBoard} equivalent to this board, {@code false} otherwise
     * 
     * @see #exactlyEquals(ConnectFourBoard)
     * 
     */
    public boolean equals(Object otherObject) {
        if (this == otherObject) return true;
        if (otherObject == null) return false;
        if (otherObject.getClass() != this.getClass()) return false;

        ConnectFourBoard that = (ConnectFourBoard) otherObject;
        if (this.moveCount != that.moveCount) return false;

        for (int i = 0; i < this.board.length; i++)
            if (this.board[i] != that.board[i]) return false;
            
        return true;
    }

    /**
     * Compares this connect four board to the specified object. The result 
     * is {@code true} if and only if the argument is not {@code null} and 
     * is a {@code ConnectFourBoard} object that represents the same board 
     * position with the same sequence of moves.
     * 
     * @param anotherBoard the {@code ConnectFourBoard} to compare this
     * {@code ConnectFourBoard} with
     * 
     * @return {@code true} if the given {@code ConnectFourBoard} is 
     * equivalent to this board (move series included), else {@code false}
     * 
     * @see #equals(Object)
     */
    public boolean exactlyEquals(ConnectFourBoard anotherBoard) {
        if (this == anotherBoard) return true;
        if (anotherBoard == null) return false;

        if (this.moveCount != anotherBoard.moveCount) return false;
        if (!this.moveOrder.equals(anotherBoard.moveOrder)) return false;

        for (int i = 0; i < this.board.length; i++)
            if (this.board[i] != anotherBoard.board[i]) return false;
            
        return true;
    }

    /***************************************************************************
    * Helper functions to check for connect fours
    ***************************************************************************/
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

    /***************************************************************************
    * Helper functions for the board
    ***************************************************************************/
    private char getCurrentChip() {
        return (playerOneToMove()) ? 'X' : 'O';
    }

    private int getIndexAtPoint(Point p) {
        verifyValidPoint(p);
        return (p.row - 1) * 7 + p.col - 1;
    }

    private int getIndexAtPoint(int row, int col) {
        verifyValidPoint(row, col);
        return (row - 1) * 7 + col - 1;
    }

    /***************************************************************************
    * Helper functions to check for illegal args
    ***************************************************************************/
    private void verifyValidPoint(Point p) {
        if (p.row < 1 || p.col < 1 || p.row > 6 || p.col > 7) throw new IllegalArgumentException("Bad point: (" + p.row + ", " + p.col + ")");
    }

    private void verifyValidPoint(int row, int col) {
        if (row < 1 || col < 1 || row > 6 || col > 7) throw new IllegalArgumentException("Bad point: (" + row + ", " + col + ")");
    }

    private void verifyValidColumn(int col) {
        if (col < 1 || col > 7) throw new IllegalArgumentException("Bad column: (" + col + ")");
    }
}
