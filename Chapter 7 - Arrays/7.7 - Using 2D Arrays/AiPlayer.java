public final class AiPlayer extends Player {

    public AiPlayer() {
        super("O");
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
