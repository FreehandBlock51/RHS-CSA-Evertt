public abstract class Player {
    public final String mark;

    public Player(String mark) {
        this.mark = mark;
    }

    /**
     * gets the position of the next move
     * @param board the current board state
     * @return a Move object with the position of the next move (mark is overwritten)
     */
    protected abstract Move getNextMovePosition(Board board);

    public final Move getNextMove(Board board) {
        Move move = getNextMovePosition(board);
        move.mark = mark;
        return move;
    }
}
