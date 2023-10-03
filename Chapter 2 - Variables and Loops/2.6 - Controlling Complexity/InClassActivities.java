public final class InClassActivities {
    public static void main(String args[]) {
        drawUpsideDownTriangle(4);
        drawUpsideDownTriangle(8);
    }

    public static void drawUpsideDownTriangle(int rows) {
        int curStarAmt;
        final int maxRowLength = (2 * rows) - 1;
        for (int r = rows; r > 0; r--) {
            curStarAmt = (2*r)-1;
            
            for (int c = 0; c < (maxRowLength - curStarAmt) / 2; c++) { System.out.print(' '); }

            for (int c = 0; c < curStarAmt; c++) {
                System.out.print('*');
            }
            System.out.println();
        }
    }
}