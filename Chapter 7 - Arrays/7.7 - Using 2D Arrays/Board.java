import java.util.Arrays;

public final class Board {
    private final String[][] board = new String[3][3];

    private final Player playerX;
    private final Player playerO;

    public Board(Player playerX, Player playerO) {
        this.playerX = playerX;
        this.playerO = playerO;
    }

    public String getMarkAtCell(int r, int c) {
        return board[r][c];
    }

    public void placeMark(Move move) {
        if (board[move.r][move.c] == "") { // only place marks on empty squares
            board[move.r][move.c] = move.mark;
        }
    }

    public void printBoard() {
        String[] row;
        String rowString;
        for (int r = 0; r < board.length; r++) {
            row = board[r];
            rowString = "";
            for (int c = 0; c < row.length - 1; c++) {
                rowString += " " + row[c] + " |";
            }
            rowString += " " + row[row.length - 1] + " ";
            System.out.println(rowString);

            if (r + 1 >= board.length) { return; }

            for (int i = 0; i < rowString.length(); i++) {
                System.out.print("-");
            }
            System.out.println();
        }
    }

    public int calcWinner() {
        switch (getGameResult()) {
            case X_WINS:
                return 1;
            case O_WINS:
                return 2;
            case NO_WINNER:
            default:
                return 0;
        }
    }

    private GameResult getGameResult() {
        GameResult result;

        result = getWinnerInRows();
        if (result != GameResult.NO_WINNER) {
            return result;
        }

        result = getWinnerInColumns();
        if (result != GameResult.NO_WINNER) {
            return result;
        }

        return getWinnerInDiagionals();
    }

    public boolean isGameInProgress() {
        for (String[] row : board) {
            for (String cell : row) {
                if (cell.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    private GameResult getWinnerFromMark(String mark) {
        if (mark.equals(playerX.mark)) {
            return GameResult.X_WINS;
        }
        if (mark.equals(playerO.mark)) {
            return GameResult.O_WINS;
        }
        return GameResult.NO_WINNER;
    }

    private GameResult getWinnerInRows() {
        for (String[] row : board) {
            if (Arrays.stream(row).distinct().count() == 1) {
                final String winningCell = row[0];
                final GameResult result = getWinnerFromMark(winningCell);
                if (result != GameResult.NO_WINNER) {
                    return result;
                }
            }
        }
        return GameResult.NO_WINNER;
    }

    private GameResult getWinnerInColumns() {
        column_loop:
        for (int c = 0; c < board[0].length; c++) {
            final String cellContents = board[0][c];
            for (int r = 1; r < board.length; r++) {
                if (!cellContents.equals(cellContents)) {
                    continue column_loop;
                }
            }
            final GameResult result = getWinnerFromMark(cellContents);
            if (result != GameResult.NO_WINNER) {
                return result;
            }
        }
        return GameResult.NO_WINNER;
    }

    private GameResult getWinnerInDiagionals() {
        final int FAR_RIGHT_INDEX = board[0].length - 1;

        final String topLeft = board[0][0];
        boolean checkTopLeft = true;
        final String topRight = board[0][FAR_RIGHT_INDEX];
        boolean checkTopRight = true;
        for (int i = 1; i <= FAR_RIGHT_INDEX; i++) {
            if (!(checkTopLeft && checkTopRight)) {
                break;
            }

            if (checkTopLeft && !board[i][i].equals(topLeft)) {
                checkTopLeft = false;
            }
            if (checkTopRight && !board[i][FAR_RIGHT_INDEX - i].equals(topRight)) {
                checkTopRight = false;
            }
        }

        if (checkTopLeft) {
            return getWinnerFromMark(topLeft);
        }
        else if (checkTopRight) {
            return getWinnerFromMark(topRight);
        }
        else {
            return GameResult.NO_WINNER;
        }
    }

    private enum GameResult {
        NO_WINNER,
        X_WINS,
        O_WINS,
    }
}
