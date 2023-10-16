import java.util.ArrayList;

public class StringUtil {
    public static String firstHalf(String input) {
        // Returns a string containing the first half of 'input'
        // In the case of an odd number of characters, the extra goes in the second half (excluded here)
        //  Example: "0123456789" -> "01234"
        // 
        // Requirements...
        //  - Use a loop to build the return string.

        return input.substring(0, (input.length() - 1) / 2);
    }

    public static String beforeSpace(String input) {
        // Returns a string containing the portion of the string BEFORE the space
        // In the case of no space, the full string should be returned
        //  Example: "abcd ef" -> "abcd"
        // 
        // Requirements...
        //  - Use the string function substring.

        return input.substring(0, Math.max(input.indexOf(" "), 0));
    }

    public static String afterSpace(String input) {
        // Returns a string containing the portion of the string AFTER the space
        // In the case of no space, an empty string should be returned
        //  Example: "abcd ef" -> "ef"
        // 
        // Requirements...
        //  - Use the string function substring.

        final int spaceIdx = input.indexOf(" ");

        return spaceIdx < 0 ? "" : input.substring(spaceIdx + 1);
    }

    public static String swapAtSpace(String input) {
        // Returns a string that swaps the section before and after the space
        //  Example: "abcd ef" -> "ef abcd"
        // 
        // Requirements...
        //  - The other functions you created for this.

        return afterSpace(input) + ' ' + beforeSpace(input);
    }

    public static char firstNonRepeatedChar(String input) {
        // Returns the first character that is not repeated later in the string (looking left to right)
        //  Example: "abcabcdef" -> 'd'

        ArrayList<String> repeatedChars = new ArrayList<>();

        String s = input.substring(0, 1);
        
        for (int i = 1; (!input.substring(i + 1).contains(s) && !repeatedChars.contains(s)) || i < input.length(); i++) {
            s = input.substring(i, i + 1);
            repeatedChars.add(s);
        }

        return s.charAt(0);
    }
}
