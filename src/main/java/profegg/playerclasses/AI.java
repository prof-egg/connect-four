package profegg.playerclasses;

import java.util.ArrayList;
import java.util.List;

import profegg.GameBoard;
import profegg.Point;

public class AI extends Player {

    // Features Wish List (I stole and trimmed this from a chess bot: https://github.com/SebLague/Tiny-Chess-Bot-Challenge-Results/blob/main/Bots/Bot_628.cs):
    // - Alpha-Beta Pruning Negamax
    // - Quiescent Search
    // - Move Ordering:
    // -- TT Move
    // -- MVV-LVA
    // -- Two Killer Moves
    // -- History Heuristic
    // - Transposition Table (move ordering and cutoffs, also used in place of
    // static eval)
    // - Iterative Deepening
    // - Aspiration Windows
    // - Principle Variation Search
    // - Check Extensions
    // - Pawn Move to 2nd/7th Rank Extensions (aka Passed Pawn Extensions)
    // - Null Move Pruning
    // - Late Move Reductions
    // - Reverse Futility Pruning
    // - Futility Pruning
    // - Late Move Pruning
    // - Internal Iterative Reductions
    // - Time Management with a soft and hard bound
    // - Eval Function:
    // -- ??

    private ArrayList<Point> myPieceLocations;
    private ArrayList<Point> opponentPieceLocations;

    /**@deprecated */
    int[] lastKnownHeights;

    public AI(char chip, GameBoard board, boolean first) {
        super(chip, board, first);

        myPieceLocations = new ArrayList<>();
        opponentPieceLocations = new ArrayList<>();
        
        lastKnownHeights = board.copyHeights(); // DELETE

        List<Point> moveHistory = board.copyMoveHistory();
        if (moveHistory.size() == 0) return;

        int evenOdd = 1;
        if (first) evenOdd = 0;
        for (int i = 0; i < moveHistory.size(); i++) {
            if (i % 2 == evenOdd) 
                myPieceLocations.add(moveHistory.get(i));
            else
                opponentPieceLocations.add(moveHistory.get(i));
        }
    }

    /**
     * @deprecated
     * @return
     */
    private List<Integer> getChangedHeightIndecies() {
        ArrayList<Integer> list = new ArrayList<>();
        int[] currentHeights = board.copyHeights();
        for (int i = 0; i < board.columns; i++ ) 
            if (lastKnownHeights[i] != currentHeights[i]) list.add(i);
        return list;
    }

    public int getMove() {
        int move = negaMaxRoot(7);

        // Keep track of opponent pieces
        if (board.moveCount() > 0) 
            opponentPieceLocations.add(board.copyMoveHistory().getLast());

        // Keep track of my placements
        int row = board.openSpacesAtColumn(move);
        myPieceLocations.add(new Point(row, move));

        lastKnownHeights = board.copyHeights(); // DELETE
        
        return move;
    }

    /**
     * @deprecated Dont use this
     * @return
     */
    private Point getLastOpponentMove() {
        // keep track of oppenent placements
        // to do so just keep a track of the heights
        // of the columns since the last move we made
        // Then check for changes in heights in the column
        // Either there is 2 changes in heights, where the change in height
        // that we didnt mess with was the opponents doing
        // or only 1 height changes, where the opponent played in 
        // the same column as our last move 
        List<Integer> changedHeights = getChangedHeightIndecies();
        if (changedHeights.size() == 1) {
            int colThatChangedHeight = changedHeights.get(0) + 1;
            int calcRow = board.openSpacesAtColumn(colThatChangedHeight) - 1;
            return new Point(calcRow, colThatChangedHeight);
        } else if (changedHeights.size() == 2) {
            Point ourLastMove = myPieceLocations.getLast();
            int index = changedHeights.indexOf(ourLastMove.col);
            // This inverts the index we want, if 1 then 0, if 0 then 1
            // We want the index of the point that ISNT our last move
            int otherIndex = 1 - index; 
            int colThatChangedHeight = changedHeights.get(otherIndex) + 1;
            int calcRow = board.openSpacesAtColumn(colThatChangedHeight) - 1;
            return new Point(calcRow, colThatChangedHeight);
        } else 
            throw new RuntimeException("Called when no opponent move has been made");
    }

    //////////////////////
    // SEARCH (NEGAMAX) //
    //////////////////////

    private int negaMaxRoot(int depth) {
        if (depth == 0) return evaluate();

        if (board.isFull()) {
            // IMPLEMENT THIS:
            // if (board.connectedFour()) 
            //     return (int) Double.NEGATIVE_INFINITY;
            return 0; // Drawm game
        }

        int bestEval = (int) Double.NEGATIVE_INFINITY;

        List<Integer> openColumns = board.getOpenColumns();
        int bestMove = 0;
        for (int column : openColumns) {
            board.addChip(column);
            // System.out.print("\nMOVE MADE: \n" + board + "\n\n");
            int eval = -negaMaxProper(depth - 1);
            if (eval > bestEval) {
                bestEval = eval;
                bestMove = column;
            }
            board.unAddChip();
            // System.out.print("\nMOVE UNMADE: \n" + board + "\n\n");
        }
        return bestMove;
    }

    private int negaMaxProper(int depth) {
        
        if (depth == 0) return evaluate();

        if (board.isFull()) {
            // IMPLEMENT THIS:
            // if (board.connectedFour()) 
            //     return (int) Double.NEGATIVE_INFINITY;
            return 0; // Drawm game
        }

        int bestEval = (int) Double.NEGATIVE_INFINITY;

        List<Integer> openColumns = board.getOpenColumns();
        for (int column : openColumns) {
            board.addChip(column);
            // System.out.print("\nMOVE MADE: \n" + board + "\n\n");
            int eval = -negaMaxProper(depth - 1);
            bestEval = Math.max(eval, bestEval);
            board.unAddChip();
            // System.out.print("\nMOVE UNMADE: \n" + board + "\n\n");
        }
        
        return bestEval;
    }

    // private class SearchNode {
    //     public final ArrayList<SearchNode> futurePositions = null;
    //     public final GameBoard board;
    //     public final int eval;
    //     public final int depth;
        
    //     public SearchNode(GameBoard board, int depth) {
    //         this.board = board;
    //         this.eval = evaluate(this.board);
    //         this.depth = depth;

    //         if (depth == 0) return;

    //         for (int column : board.getOpenColumns()) {
    //             char chip = (board.playerOneToMove()) ? 'X' : 'O';
    //             GameBoard newGameBoard = new GameBoard(board.moveOrder());
    //             newGameBoard.addChip(column, chip);
    //             futurePositions.add(new SearchNode(newGameBoard, depth - 1));
    //         }
    //     }
    // }

    //////////
    // EVAL //
    //////////
    private int evaluate() {

        var p1PLocations = board.copyP1MoveHistory();
        var p2PLocations = board.copyP2MoveHistory();

        int p1Weight = getTotalPieceWeight(p1PLocations);
        int p2Weight = getTotalPieceWeight(p2PLocations);

        int eval = p1Weight - p2Weight;

        int perspective = (board.playerOneToMove()) ? -1 : 1;

        return eval * perspective;
    }

    private int getTotalPieceWeight(List<Point> points) {
        int totalWeight = 0;
        for (Point p : points) 
            totalWeight += getPointWeight(p);
        return totalWeight;
    }

    private int getPointWeight(Point p) {
        int totalWeight = 0;
        int row = p.row - 1;
        int col = p.col - 1;

        totalWeight += horizontalWeights[col];
        totalWeight += verticalWeights[row];
        totalWeight += forwardDiagnalWeights[row][col];
        totalWeight += backwardDiagnalWeights[row][col];

        return totalWeight;
    }

    // private final int[][] horizontalWeights = {
    //     { 1, 2, 3, 4, 3, 2, 1 },
    //     { 1, 2, 3, 4, 3, 2, 1 },
    //     { 1, 2, 3, 4, 3, 2, 1 },
    //     { 1, 2, 3, 4, 3, 2, 1 },
    //     { 1, 2, 3, 4, 3, 2, 1 },
    //     { 1, 2, 3, 4, 3, 2, 1 }
    // };

    private final int[] horizontalWeights = {
        1, 2, 3, 4, 3, 2, 1
    };

    // private final int[][] verticalWeights = {
    //     { 1, 1, 1, 1, 1, 1, 1 },
    //     { 2, 2, 2, 2, 2, 2, 2 },
    //     { 3, 3, 3, 3, 3, 3, 3 },
    //     { 3, 3, 3, 3, 3, 3, 3 },
    //     { 2, 2, 2, 2, 2, 2, 2 },
    //     { 1, 1, 1, 1, 1, 1, 1 }
    // };

    private final int[] verticalWeights = {
        1,
        2,
        3,
        3,
        2,
        1,
    };

    private final int[][] forwardDiagnalWeights = {
        { 0, 0, 0, 1, 1, 1, 1 },
        { 0, 0, 1, 2, 2, 2, 1 },
        { 0, 1, 2, 3, 3, 2, 1 },
        { 1, 2, 3, 3, 2, 1, 0 },
        { 1, 2, 2, 2, 1, 0, 0 },
        { 1, 1, 1, 1, 0, 0, 0 }
    };

    private final int[][] backwardDiagnalWeights = {
        { 1, 1, 1, 1, 0, 0, 0 },
        { 1, 2, 2, 2, 1, 0, 0 },
        { 1, 2, 3, 3, 2, 1, 0 },
        { 0, 1, 2, 3, 3, 2, 1 },
        { 0, 0, 1, 2, 2, 2, 1 },
        { 0, 0, 0, 1, 1, 1, 1 }
    };


    ///////////
    // OTHER //
    ///////////
    private int getRandomMove() {
        List<Integer> openColumns = board.getOpenColumns();
        return openColumns.get((int) (Math.random() * openColumns.size()));
    }
}
