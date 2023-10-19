public class Cumulative {
    public static String alphabet(char first, int count) {
        // Return a String consiting of a section of the alphabet.
        //  It should start with letter 'first' and contain 'count' letters.
        //  If count goes past 'z', then it should wrap back around to 'a' 
        //    for the next letter.
        //  You can assume that first is a lowercase letter.
        
        // Examples...
        //  alphabet('a', 5) -> "abcde"
        //  alphabet('c', 4) -> "cdef"
        //  alphabet('y', 6) -> "yzabcd"
        StringBuilder builder = new StringBuilder(count);
        
        final int ALPHABET_LENGTH = 26;
        final int remainingLetters = ALPHABET_LENGTH - (first - 'a');
        
        for (int i = 0; i < count; i++) {
            if (i > remainingLetters) {
                builder.append((char)('a' + ((i - remainingLetters) % ALPHABET_LENGTH)));
            }
            else {
                builder.append((char)(first + i));
            }
        }

        return builder.toString();
    }

    public static int factorSum(int n) {
        // Returns the sum of all factors of n, except for n itself
        
        // Examples...
        //  factorSum(6) -> 6      // 1 + 2 + 3
        //  factorSum(14) -> 10    // 1 + 2 + 7
        int sum = 0;
        for (int factor = 1; factor < n; factor++) {
            if (n % factor == 0) {
                sum += factor;
            }
        }
        return sum;
    }

    public static boolean isPerfect(int n) {
        // Returns true if n is a perfect number, false otherwise
        //  A perfect number is a positive integer that is equal to the sum of its proper divisors.
        //  Use the factorSum function you wrote to help you do this (and do not copy the code).
        
        // Examples...
        //  isPerfect(6) -> true   // 1 + 2 + 3 = 6, so it is a perfect number
        //  isPerfect(14) -> false // 1 + 2 + 7 = 10, which is not 14, so not perfect
        if (n <= 0) { return false; }
        return n == factorSum(n);
    }
}
