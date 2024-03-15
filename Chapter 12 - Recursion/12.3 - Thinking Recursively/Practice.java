import java.util.Arrays;

public class Practice {
    /* Write a palindrome string checker. It should take a String as input and
     *  return a boolean to indicate if it is a palindrome. Note that a palindrome
     *  is a strig that reads the same backwards as forwards. Note that any single 
     *  letter string is considered a panindrome. 
     * Your method must use recursion in its solution.
     * 
     * Examples...
     *  input: isPalindrome("racecar"), output: true
     *  input: isPalindrome("test"), output: false
     */ 
    public boolean isPalindrome(String str) {
        if (str.length() == 0) {
            return true;
        }
        if (!str.substring(str.length() - 1).equals(str.substring(0, 1))) {
            return false;
        }
        return isPalindrome(str.substring(1, str.length() - 1));
    }


    /* Write a method to sum the digits of a number. It should take an int and
     *  return an int. Precondition is that n is non-negative.
     * Your method must use recursion in its solution.
     * 
     * Examples...
     *  input: sumDigits(123), output: 6
     *  input: sumDigits(572), output: 14
     *  input: sumDigits(116), output: 8
     */
    public int sumDigits(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        return sumDigits_aux(n, 0);
    }
    private int sumDigits_aux(int n, int curSum) {
        if (n == 0) {
            return curSum;
        }
        return sumDigits_aux(n / 10, curSum + (n % 10));
    }

    
    /* Write a method that returns a symmetric String that matches the
     *  examples given below. Extrapolate the generic case from the given examples.
     * Your method must use recursion in its solution.
     * 
     * Examples...
     *  input: createString(3), output: "321.123"
     *  input: createString(5), output: "54321.12345"
     *  input: createString(1), output: "1.1"
     *  input: createString(0), output: ""
     */
    public String createString(int n) {
        if (n <= 0) {
            return "";
        }
        return createString_aux(n, "");
    }
    private String createString_aux(int n, String str) {
        if (n <= 0) { // make it symmetrical
            final StringBuilder builder = new StringBuilder(str + ".");
            for (int i = str.length() - 1; i >= 0; i--) {
                builder.append(str.substring(i, i + 1));
            }
            return builder.toString();
        }
        return createString_aux(n - 1, str + n);
    }


    /* Write a method that find the smallest number in an array, starting 
     *  at an index specified by an input parameter.
     *  Preconditions: list has at least one element, and 0 <= n < list.length
     * Your method must use recursion in its solution.
     * 
     * Examples...
     *  input: minValue({-3, 7, -8, 4, 0}, 0), output: -8
     *  input: minValue({8, 7, 1, 10, 6, 9}, 3), output: 6
     *  input: minValue({-1, 0, 1, 2, 3}, 1), output: 0
     */
    public int minValue(int[] arr, int idx) {
        try {
            final int firstMin = arr[idx];
            return minValue_aux(arr, idx + 1, firstMin)
        }
        catch (NullPointerException e) {
            throw new IllegalArgumentException("Given a null array!");
        }
        catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException();
        }
    }
    private int minValue_aux(int[] arr, int curIdx, int curMin) {
        if (curIdx >= arr.length) {
            return curMin;
        }
        final int curVal = arr[curIdx];
        if (curVal < curMin) {
            return minValue_aux(arr, curIdx + 1, curVal);
        }
        else {
            return minValue_aux(arr, curIdx + 1, curMin);
        }
    }
}