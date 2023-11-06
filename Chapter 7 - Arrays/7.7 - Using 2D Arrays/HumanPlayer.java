import java.util.Scanner;

public final class HumanPlayer extends Player {
    private final Scanner scanner;

    public HumanPlayer() {
        super("X");
        scanner = new Scanner(System.in);
    }

    @Override
    protected Move getNextMovePosition(Board board) {
        System.out.print("Enter next move position [x,y]: ");
        String[] posStrs = scanner.nextLine().split(",");
        int x = Integer.parseInt(posStrs[0]);
        int y = Integer.parseInt(posStrs[1]);
        return new Move(x, y, mark);
    }
    
    @Override
    protected void finalize() {
        scanner.close();
    }
}