import java.util.ArrayList;
import java.util.Arrays;

public class Practice {
    /* Write a method called toArrayList that takes an array of Strings and
     *  return an ArrayList populated with the same list of Strings.
     * Example...
     *  input: { "a", "b", "c" }, output: [ a, b, c ]
     */
    public static <T> ArrayList<T> toArrayList(T[] arr) {
        return arr == null ? null : new ArrayList<>(Arrays.asList(arr));
    }

    /* Write a method called reverseList that takes an array of Strings and
     *  return a new ArrayList with the same strings, but with their locations
     *  in the list reversed in order.
     * Example...
     *  input: { "a", "b", "c" }, output: [ c, b, a ]
     */ 
    public static <T> ArrayList<T> reverseList(T[] arr) {
        if (arr == null) {
            return null;
        }
        ArrayList<T> list = new ArrayList<>(arr.length);
        for (int i = arr.length - 1; i >= 0; i--) {
            list.add(arr[i]);
        }
        return list;
    }

    /* Write a method called splitString that takes a String and returns an
     *  ArrayList with each word in the String as an element of the ArrayList.
     *  Words in the input are delimited by spaces and punctuation including
     *  periods, commas, exclemation, and question marks.
     * Note that I'm asking you to write your own (don't use a system method for this)
     * Examples...
     *  input: "This is a test", output: [ this, is, a, test ]
     *  input: "Hi. What, is your name?", output: [ Hi, What, is, your, name ]
     */ 
    public static ArrayList<String> splitString(String str) {
        if (str == null) {
            return null;
        }
        ArrayList<String> list = new ArrayList<>();
        int startIdx;
        int endIdx = -1;
        while (endIdx < str.length()) {
            startIdx = endIdx + 1;
            endIdx = str.indexOf(" ", startIdx);
            if (endIdx == -1) {
                endIdx = str.length();
            }

            String sub = str.substring(startIdx, endIdx);
            if (sub.length() == 0) {
                continue;
            }
            char last = sub.charAt(sub.length() - 1);
            switch (last) {
                case '!':
                case '?':
                case '.':
                case ',':
                    list.add(sub.substring(0, sub.length() - 1));
                    break;
                default:
                    list.add(sub);
            }
        }
        return list;
    }

    /* Write a method called justPrimes that takes an Array of integer types
     *  and returns an ArrayList of the subset of those numbers that are prime.
     * Examples...
     *  input: { 1, 3, 4, 7, 8, 11 }, output: [ 1, 3, 7, 11 ]
     */ 
    public static ArrayList<Integer> justPrimes(int[] arr) {
        if (arr == null) {
            return null;
        }
        ArrayList<Integer> list = new ArrayList<>(arr.length);
        outer:
        for (int n : arr) {
            if (n <= 1) { // 1, 0, and negatives are not prime
                continue outer;
            }

            for (int i = 2; i <= n / 2; i++) {
                if (n % i == 0) {
                    continue outer;
                }
            }

            list.add(n);
        }
        return list;
    }
}