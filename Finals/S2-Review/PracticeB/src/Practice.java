public class Practice {
    /* strCatColumn - concatenates a column in a 2D array of strings.
        returns null on invalid parameters.
          strCatColumn({ { "a", "b", "c" }, { "d", "e", "f" } }, 0 ) -> "ad"
          strCatColumn({ { "a", "b", "c" }, { "d", "e", "f" } }, 1 ) -> "be"
     */
    public String strCatColumn(String[][] m, int c) {
        if (m == null || c < 0) {
            return null;
        }

        final StringBuilder b = new StringBuilder(m.length);
        for (String[] r : m) {
            if (c >= r.length) {
                return null;
            }
            b.append(r[c]);
        }
        return b.toString();
    }

    /* calcSeriesRecursive - write a RECURSIVE method that computes the sum
        of the first n terms of: a(1)+b + a(2)+b + ... + an+b
          calcSeriesRecursive(2,1,3) -> 15       (because: 3 + 5 + 7 = 15)
          calcSeriesRecursive(-3,10,12) -> -114
     */
    public int calcSeriesRecursive(int a, int b, int n) {
        return calcSeriesRecursive_aux(a, b, n, 0);
    }
    private int calcSeriesRecursive_aux(int a, int b, int n, int s) {
        if (n <= 0) return s;
        return calcSeriesRecursive_aux(a, b, n - 1, s + (a * n + b));
    }

    /* createFibonacciArray - write a method that returns an array containing
        the numbers in the fibonacci sequence starting at a given number. If
        the given number is not part of the sequence, then null should be 
        returned. The number of items in sequence is the second parameter.
          createFibonacciArray(1, 5) -> { 1, 1, 2, 3, 5 }
          createFibonacciArray(8, 4) -> { 8, 13, 21, 34 }
          createFibonacciArray(4, 3) -> null
     */
    public int[] createFibonacciArray(int first, int seqCount) {
        try {
            int[] result = null;
            int prev = 0;
            int cur = 1;
            int itemCount = 0;
            while (itemCount < seqCount) {
                if (cur == first && itemCount == 0) {
                    itemCount = 1;
                    result = new int[seqCount];
                    result[0] = first;
                }
                else if (cur > first && itemCount == 0) {
                    break;
                }
                else if (cur >= first) {
                    result[itemCount] = cur;
                    itemCount++;
                }

                // advance the sequence
                final int temp = prev;
                prev = cur;
                cur += temp;
            }
            return result;
        }
        catch (NullPointerException npe) {
            // if we reference null, it's probably our array not being initialized,
            //  which means that we passed our starting point.  Therefore, the starting
            //  point isn't part of the sequence, so we return null.
            return null;
        }
    }
}
