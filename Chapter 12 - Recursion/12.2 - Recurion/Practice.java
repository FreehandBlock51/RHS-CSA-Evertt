public class Practice {
    /* Write a recurisve version of the method pow, which computes the value of
     *  b to the power of e. Preconditions: b >= 1 && e >= 1
     *  note: Do not modify pow, but do implement powR to have the same behavior,
     *        but impliment it with recursion, instead of a loop.
     * Example...
     *  input: powR(3, 2), output: 9
     *  input: powR(5, 3), output: 125
     */ 
    public int pow(int b, int e) {
        int result = 1;
        for (int i = 0; i < e; i++) {
            result *= b;
        }
        return result;
    }
    public int powR(int b, int e) {
        if (e == 0) {
            return 1;
        }
        if (e < 0) {
            return 1 / powR(b, -e);
        }
        return b * powR(b, e - 1);
    }


    /* Write a recurisve version of the method createString, which prints out an asterisk
     *  surrounded by a symmetric set of brackets. Preconditions: n >= 0
     *  note: Do not modify createString, but do implement createStringR to have the same behavior,
     *        but impliment it with recursion, instead of a loop.
     * Example...
     *  input: createStringR(3), output: [[[*]]]
     *  input: createStringR(5), output: [[[[[*]]]]]
     *  input: createStringR(0), output: *
     */
    public String createString(int n) {
        String prefix = "";
        String postfix = "";
        for (int i = 0; i < n; i++) {
            prefix += "[";
            postfix += "]";
        }
        return prefix + "*" + postfix;
    }
    public String createStringR(int n) {
        if (n <= 0) {
            return "*";
        }
        return "[" + createStringR(n - 1) + "]";
    }

    private static final int BITS_IN_AN_INT = 8 * Integer.BYTES;
    
    /* Write a method called printBinary that takes an integer and prints out the number
     *  in binary (base 2). Your implementation must use recursion. 
     *  Preconditions: Integer.MAX_VALUE >= n >= 0
     * Examples...
     *  input: printBinary(5), output: 0101 (or 101)
     *  input: printBinary(201), output: 011001001 (or 11001001)
     */
    public void printBinary(int n) {
        if (n > Integer.MAX_VALUE | n < 0) {
            throw new IllegalArgumentException();
        }
        // System.out.print("0b");
        
        for (int bitLoc = BITS_IN_AN_INT - Integer.numberOfLeadingZeros(n); bitLoc >= 0; bitLoc--) {
    
            final int curBit = n & (1 << bitLoc); // mask just the bit `bitLoc` bits away from the right
            if (curBit == 0) {
                System.out.print("0");
            }
            else {
                System.out.print("1");
            }
        }

        System.out.println();
    }
    public void printBinaryR(int n) {
        if (n > Integer.MAX_VALUE | n < 0) {
            throw new IllegalArgumentException();
        }
        // System.out.print("0b");
        printBinaryRAux(n, BITS_IN_AN_INT - Integer.numberOfLeadingZeros(n));
        System.out.println();
    }
    private void printBinaryRAux(int n, int bitLoc) {
        if (bitLoc < 0) {
            return; // all bits printed
        }

        final int curBit = n & (1 << bitLoc); // mask just the bit `bitLoc` bits away from the right
        if (curBit == 0) {
            System.out.print("0");
        }
        else {
            System.out.print("1");
        }

        printBinaryRAux(n, bitLoc - 1);
    }
}