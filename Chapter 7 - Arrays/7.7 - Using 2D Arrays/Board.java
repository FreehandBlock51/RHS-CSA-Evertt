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
        for (int i = 0; i < board.length; i++) {
            if (board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2]) 
                 && !board[i][0].equals("")) { // rows
                
            }
            if (board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i]) 
                 && !board[0][i].equals("")) { // columns
                return board[0][i].equals(playerX.mark) ? 1 : 2;
            }
        }

        if (board[1][1].equals("")) {
            return 0;
        }

        if ((board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) ||
             (board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0]))) { // diagonals
            return board[1][1].equals(playerX.mark) ? 1 : 2;
        }

        return 0;
    }
}
