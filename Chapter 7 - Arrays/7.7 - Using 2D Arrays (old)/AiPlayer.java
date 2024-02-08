import java.util.ArrayList;

public final class AiPlayer extends Player {

    public AiPlayer() {
        super("O");
    }

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
        final ArrayList<Move> moves = new ArrayList<>(Board.BOARD_SIZE * Board.BOARD_SIZE);
        for (int r = 0; r < Board.BOARD_SIZE; r++) {
            for (int c = 0; c < Board.BOARD_SIZE; c++) {
                if (board.isCellEmpty(r, c)) {
                    moves.add(new Move(r, c, mark));
                }
            }
        }
        return moves;
    }

    private static int minimax(Board board, int depth, boolean isMaximizingPlayer, String markToMaximize, String otherMark) {
        if (board.calcWinner() != 0) {
            return evalBoard(board);
        }
        
        if (isMaximizingPlayer) {
            int bestVal = Integer.MIN_VALUE;
            for(Move move : getPossibleMoves(board, markToMaximize)) {
                final int value = minimax(board.withHypotheticalMove(move), depth+1, false, markToMaximize, otherMark);
                bestVal = Math.max(bestVal, value);
            }
            return bestVal;
        }
        else {
            int bestVal = Integer.MAX_VALUE;
            for(Move move : getPossibleMoves(board, otherMark)) {
                final int value = minimax(board.withHypotheticalMove(move), depth+1, false, markToMaximize, otherMark);
                bestVal = Math.min(bestVal, value);
            }
            return bestVal;
        }
    }

    @Override
    protected Move getNextMovePosition(Board board) {
        for (int r = 0; r < Board.BOARD_SIZE; r++) {
            for (int c = 0; c < Board.BOARD_SIZE; c++) {
                if (board.getMarkAtCell(r, c).isEmpty()) {
                    return new Move(r, c);
                }
            }
        }
        return new Move();
    }
    
}
