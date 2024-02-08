import java.util.Scanner;

public final class HumanPlayer extends Player {
    private final Scanner scanner;

    public HumanPlayer() {
        super("X");
        scanner = new Scanner(System.in);
    }

    @Override
    protected Move getNextMovePosition(Board board) {
        Move move = null;
        while (move == null) {
            board.printBoard();
            System.out.print("Your move (e.g. '0,2')? ");
            String[] posStrs = scanner.nextLine().split(",");
            int x = Integer.parseInt(posStrs[0]);
            int y = Integer.parseInt(posStrs[1]);
            if (board.getMarkAtCell(x, y).isEmpty()) {
                move = new Move(x, y);
            }
            else {
                System.out.println("Cannot place a mark on (" + x + ", " + y + ") because a mark has already been placed there!");
            }
        }
        return move;
    }
    
    @Override
    protected void finalize() {
        final Scanner s = scanner;
        if (s != null)
            s.close();
    }
}