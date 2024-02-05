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
