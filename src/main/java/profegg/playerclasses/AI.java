package profegg.playerclasses;

import java.util.List;

import profegg.ConnectFourBoard;
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

    public AI(ConnectFourBoard board) {
        super(board);
    }


    public int getMove() {
        return negaMaxRoot(7);
    }

    /***************************************************************************
    * SEARCH STUFF (NEGAMAX)
    ***************************************************************************/
    private int negaMaxRoot(int depth) {
        if (depth == 0) return evaluate();

        int bestEval = (int) Double.NEGATIVE_INFINITY;

        List<Integer> openColumns = board.getOpenColumns();
        int bestMove = 0;
        for (int column : openColumns) {
            board.addChip(column);
            int eval = -negaMaxProper(depth - 1);
            if (eval > bestEval) {
                bestEval = eval;
                bestMove = column;
            } else if (eval == bestEval) {
                // IMPLEMENT THIS:
                // pick a random move
            }
            board.unAddChip();
        }
        return bestMove;
    }

    private int negaMaxProper(int depth) {
        
        if (depth == 0) return evaluate();

        int bestEval = (int) Double.NEGATIVE_INFINITY;
        List<Integer> openColumns = board.getOpenColumns();
        for (int column : openColumns) {
            board.addChip(column);
            int eval = -negaMaxProper(depth - 1);
            bestEval = Math.max(eval, bestEval);
            board.unAddChip();
        }
        
        return bestEval;
    }

    /***************************************************************************
    * EVALAUTION STUFF
    ***************************************************************************/
    private int evaluate() {

        if (board.isWinner(board.playerOneToMove())) 
            return (int) Double.POSITIVE_INFINITY; // Won game

        if (board.isFull()) 
            return 0; // Drawn game

        // WEIGHT CHECKS
        var p1PLocations = board.copyP1MoveHistory();
        var p2PLocations = board.copyP2MoveHistory();

        int p1Weight = getTotalPieceWeight(p1PLocations);
        int p2Weight = getTotalPieceWeight(p2PLocations);

        int weightDifference = p1Weight - p2Weight;

        // FINAL EVAL
        int perspective = (board.playerOneToMove()) ? 1 : -1;
        
        int eval = weightDifference * perspective;

        return eval;
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


    /***************************************************************************
    * OTHER
    ***************************************************************************/
    // private int getRandomMove() {
    //     List<Integer> openColumns = board.getOpenColumns();
    //     return openColumns.get((int) (Math.random() * openColumns.size()));
    // }
}
