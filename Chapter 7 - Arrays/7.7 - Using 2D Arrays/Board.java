import java.util.Arrays;

/* The Board stores the state of the Tic-Tac-Toe board */
public class Board {
    public static final String empty = " ";
    public static final int SIZE = 3;

    // 3x3 array of single character Strings. This stores the current
    //  state of the board. After construction, it is guarenteed to
    //  contain nine String elements. 
    // Each element stores one of the following...
    //    Board.empty, HumanPlayer.marker, or AiPlayer.marker
    private String[][] board; 

    public Board() {
        board = new String[SIZE][SIZE];
        reset();
    }

    // Reset board so that each element is a an empty string (use Board.empty)
    //  postcondition: board is a 3x3 array. all elements are Board.empty.
    public void reset() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = empty;
            }
        }
    }

    // Returns the number of rows in the board
    public int rows() {
        return board.length;
    }

    // Returns the number of columns in the board
    public int columns() {
        if (board.length == 0) {
            return 0;
        }
        return board[0].length;
    }

    // Returns true if the specified row/column space is empty 
    //  (does not already store an X or O)
    //  You can assume r & c are valid values.
    public boolean isEmpty(int r, int c) {
        return getMarkerAt(r, c).equals(empty);
    }

    // Returns the String marker at the specified row/column (e.g. "X")
    //  You can assume r & c are valid values.
    public String getMarkerAt(int r, int c) {
        return board[r][c];
    }

    // Creates and returns a String that can be printed display the board.
    //  Example return value: " X |   | O \n-----------\n O | X |   \n-----------\n   |   | O "
    public String renderToString() {
        String[] row;
        String rowString;
        StringBuilder builder = new StringBuilder();

        for (int r = 0; r < board.length; r++) {
            row = board[r];
            rowString = "";
            String mark;
            for (int c = 0; c < row.length - 1; c++) {
                mark = row[c].isEmpty() ? " " : row[c];
                rowString += " " + mark + " |";
            }
            rowString += " " + row[row.length - 1] + " ";
            if (row[row.length - 1].isEmpty()) {
                rowString += " ";
            }
            builder.append(rowString);
            builder.append('\n');

            if (r + 1 >= board.length) { break; }

            for (int i = 0; i < rowString.length(); i++) {
                builder.append("-");
            }
            builder.append('\n');
        }

        return builder.toString();
    }

    // Returns the number of empty spaces on the board. 
    //  An empty board (at the start of the game) would return 9.
    //  After both players have places one mark, it would return 7.
    public int numberEmpty() {
        int numEmpty = 0;
        for (int r = 0; r < rows(); r++) {
            for (int c = 0; c < columns(); c++) {
                if (isEmpty(r, c)) {
                    numEmpty++;
                }
            }
        }
        return numEmpty;
    }

    // Update the board with the specified move applied.
    //  You can assume it is a valid move.
    public void placeMark(Move move) {
        if (isEmpty(move.r, move.c)) {
            board[move.r][move.c] = move.mark;
        }
    }

    // Determines if there is a winner, based on the current board state.
    //  Note that in the case that no one has won, it returns 0.
    //  Returns: 0:none, 1:human(HumanPlayer.marker), 2:AI(AiPlayer.marker)
    public int calcWinner() {
        return getGameResult().result;
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
                if (cell == null || cell.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    private GameResult getWinnerFromMark(String mark) {
        if (mark == null) {
            return GameResult.NO_WINNER; // to prevent null pointer exception
        }
        if (mark.equals(HumanPlayer.marker)) {
            return GameResult.X_WINS;
        }
        if (mark.equals(AiPlayer.marker)) {
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
        for (int c = 0; c < columns(); c++) {
            final String cellContents = board[0][c];
            if (cellContents.isEmpty()) {
                continue;
            }

            for (int r = 1; r < rows(); r++) {
                if (!board[r][c].equals(cellContents)) {
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
        final int FAR_RIGHT_INDEX = columns() - 1;

        final String topLeft = board[0][0];
        boolean checkTopLeft = true;
        final String topRight = board[0][FAR_RIGHT_INDEX];
        boolean checkTopRight = true;
        for (int i = 1; i <= FAR_RIGHT_INDEX; i++) {
            if (!(checkTopLeft || checkTopRight)) {
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
        NO_WINNER(0),
        X_WINS(1),
        O_WINS(2);

        public final int result;

        GameResult(int result) {
            this.result = result;
        }
    }
}
