import java.util.ArrayList;

public class AiPlayer extends Player {
    public static final String marker = "O";
    public static final int SEARCH_DEPTH = 100;

    private static int evalBoard(Board board) {
        switch (board.calcWinner()) {
            case 1:
                return -1;
            case 2:
                return 1;
            default:
                return 0;
        }
    }

    private static Iterable<Move> getPossibleMoves(Board board, String mark) {
        final ArrayList<Move> moves = new ArrayList<>(board.rows() * board.columns());
        for (int r = 0; r < board.rows(); r++) {
            for (int c = 0; c < board.columns(); c++) {
                if (board.isEmpty(r, c)) {
                    moves.add(new Move(r, c, mark));
                }
            }
        }
        return moves;
    }
    
    private static Board withHypotheticalMove(Board board, Move move) {
        Board newBoard = new Board();
        for (int r = 0; r < board.rows(); r++) {
            for (int c = 0; c < board.columns(); c++) {
                newBoard.placeMark(new Move(r, c, board.getMarkerAt(r, c)));
            }
        }
        newBoard.placeMark(move);
        return newBoard;
    }

    private static int minimax(Board board, int depth, boolean isMaximizingPlayer, String markToMaximize, String otherMark) {
        if (board.calcWinner() != 0 || depth <= 0) {
            return evalBoard(board);
        }
        
        if (isMaximizingPlayer) {
            int bestVal = Integer.MIN_VALUE;
            for(Move move : getPossibleMoves(board, markToMaximize)) {
                final int value = minimax(withHypotheticalMove(board, move), depth-1, false, markToMaximize, otherMark);
                bestVal = Math.max(bestVal, value);
            }
            return bestVal;
        }
        else {
            int bestVal = Integer.MAX_VALUE;
            for(Move move : getPossibleMoves(board, otherMark)) {
                final int value = minimax(withHypotheticalMove(board, move), depth-1, false, markToMaximize, otherMark);
                bestVal = Math.min(bestVal, value);
            }
            return bestVal;
        }
    }

    public Move getNextMove(Board board) {
        Move move = new Move();
        int moveScore = Integer.MIN_VALUE;
        for (Move newMove : getPossibleMoves(board, marker)) {
            final int newScore = minimax(withHypotheticalMove(board, move), SEARCH_DEPTH, true, marker, HumanPlayer.marker);
            if (newScore > moveScore) {
                move = newMove;
                moveScore = newScore;
            }
        }
        return move;
    }
}
