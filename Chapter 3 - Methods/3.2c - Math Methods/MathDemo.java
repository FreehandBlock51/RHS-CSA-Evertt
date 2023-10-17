// Demos the creation of simple math methods (pow, sumToN, ...)
public class MathDemo {
    public static void main(String[] args) throws Exception {

        // Add code the exercises (run & test) all the code you wrote in this class. 
        // Pick good examples (non-trivial but also something you can hand-verify).  
        // Be sure to identify the output For example: System.out.println("pow(2, 3) == " + pow(2, 3));
        
        System.out.println("pow(2,3) = " + pow(2,3));
        try {
            System.out.print("pow(4,-1) = ");
            System.out.println(pow(4,-1));
            System.err.println("Control should never reach this :(");
        } catch (ArithmeticException e) {
            System.out.println("Method failed where it should fail :)");
        }

        System.out.println("factorial(5) = " + factorial(5));
        System.out.println("factorial(0) = " + factorial(0));

        System.out.println("sumToN(0) = " + sumToN(0));
        System.out.println("sumToN(3) = " + sumToN(3));

        System.out.println("sumSquares(3) = " + sumSquares(3));
        System.out.println("sumSquares(9) = " + sumSquares(9));

        System.out.println("repeat(\"abc\", 5) = \"" + repeat("abc", 5) + "\"");
        System.out.println("repeat(\"5\", 2) = \"" + repeat("5", 2) + "\"");

        System.out.println("padLeft(\"abc\", 5) = \"" + padLeft("abc", 5) + "\"");
        System.out.println("padLeft(\"abc\", 2) = \"" + padLeft("abc", 2) + "\"");
        System.out.println("padLeft(\"5\", 2) = \"" + padLeft("5", 2) + "\"");

        System.out.println("\nprintTableOfSquares(10):");
        printTableOfSquares(10);
        System.out.println("\nprintTableOfSquares(100):");
        printTableOfSquares(100);
        System.out.println("\nprintTableOfSquares(100000):");
        try {
            printTableOfSquares(100000);
            System.err.println("Control should never reach this :(");
        } catch (Exception e) {
            System.out.println("Method failed where it should fail :)");
        }

        System.out.println("---- MathDemo: Done ----"); 
    }
    
    // ------------------------------------------------------
    // computes 'num' to the 'exponent'  Thus pow(2,3) == 8
    // ‘exponent’ is required to be non-negative integer.
    // num and exponent are both integers. returns an integer.  
    public static int pow(int base, int exponent) throws ArithmeticException {
        if (exponent < 0) { throw new ArithmeticException("exponent cannot be negative"); }

        return (int)Math.pow(base, exponent);
    }
    
    // ------------------------------------------------------
    // computes n! that is n * (n-1) * (n-2) ... 3 * 2 * 1
    // factorial(0) == 1.  
    // takes an integer n and returns an integer; 
    public static int factorial(int n) {
        if (n == 0) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    // ------------------------------------------------------
    // computes the sum of all integers from 1 to maxNum inclusive.  
    // Thus sumToN(0) == 0  sumToN(3) == 6
    // takes an integer maxNum and returns an integer; 
    public static int sumToN(int maxNum) {
        if (maxNum < 1) {
            return 0;
        }
        return maxNum + sumToN(maxNum - 1);
    }
    
    // ------------------------------------------------------
    // computes the sum of the squares to n.   
    // that is N*N + (N-1)*(N-1) ... 3*3 + 2*2 + 1*1
    // takes an integer maxNum and returns an integer     
    public static int sumSquares(int maxNum) {
        if (maxNum < 1) {
            return 0;
        }
        return maxNum*maxNum + sumSquares(maxNum - 1);
    }
    
    // ------------------------------------------------------
    // returns a string that is 'str' repeated 'count' times 
    // takes a str and count argument and returns a string. 
    public static String repeat(String str, int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(str);
        }
        return builder.toString();
    }
    
    // ------------------------------------------------------
    // returns a string that is 'str' padded with spaces 
    // so that it has a total of 'width' characters
    // Callers should insure that the length of str <= width
    // Takes a str and width argument and returns a string.
    public static String padLeft(String str, int width) {
        final int spacesToInsert = width - str.length();
        return repeat(" ", spacesToInsert) + str;
    }
    
    // ------------------------------------------------------
    // print a table of square that starts at 1 and goes up to and includes maxN
    // +-----+-------+
    // |  N  |  N*N  |
    // +-----+-------+
    // |   1 |     1 |
    // |   2 |     4 |
    //      ... omitted ...
    // |  10 |   100 |
    //      ... omitted ...
    // | 100 | 10000 | 
    // +-----+-------+
    // You can assert that maxN*maxN < 100000 and maxN < 1000
    // Thus N will be at most a 3 digit number and maxN will be
    // at most a 5 digit number.   
    public static void printTableOfSquares(int maxN) throws Exception {
        // if our biggest square exceeds the integer limit, our table won't be accurate
        if (maxN > Math.sqrt(Integer.MAX_VALUE)) {
            throw new Exception("maxN^2 exceeds the integer limit!");
        }

        // add 2 to both widths to account for spaces on either side of the number
        final int nColWidth = Integer.toString(maxN).length() + 2;
        final int nSqrColWidth = Integer.toString(maxN*maxN).length() + 2;

        // header
        final String nColTitle = "N";
        final String nSqrColTitle = "N*N";
        // add the modulus bit at the end to ensure that titles are left-justified
        final int nHeaderLeftSpaceCount = (nColWidth - nColTitle.length()) / 2 + ((nColWidth - nColTitle.length()) % 2);
        final int nSqrHeaderLeftSpaceCount = (nSqrColWidth - nSqrColTitle.length()) / 2 + ((nSqrColWidth - nSqrColTitle.length()) % 2);
        printSeperator(nColWidth, nSqrColWidth);
        System.out.println("|" + repeat(" ", nHeaderLeftSpaceCount) + nColTitle + repeat(" ", nColWidth - nColTitle.length() - nHeaderLeftSpaceCount) + "|" + repeat(" ", nSqrHeaderLeftSpaceCount) + nSqrColTitle + repeat(" ", nSqrColWidth - nSqrColTitle.length() - nSqrHeaderLeftSpaceCount) + "|");
        printSeperator(nColWidth, nSqrColWidth);

        // data
        for (int i = 1; i <= maxN; i++) {
            System.out.println("|" + padLeft(Integer.toString(i), nColWidth - 1) + " |" + padLeft(Integer.toString(i*i), nSqrColWidth - 1) + " |");
        }

        printSeperator(nColWidth, nSqrColWidth);
    }
    private static void printSeperator(int... colWidths) {
        for (int colWidth : colWidths) {
            System.out.print("+" + repeat("-", colWidth));
        }
        System.out.println("+");
    }
}  
