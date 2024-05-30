import java.util.ArrayList;

public class Practice {
    /* strPatternA - prints out a pattern of dashes, driven by the
        givin input parameters. Use the examples to understand the pattern.
          strPatternA(3, 2)
             "--
              -----
              --------"
          strPatternA(2, 5)
             "-----
              -------"
          strPatternA(4, 1)
             "-
              -----
              ---------
              -------------"
     */
    public String strPatternA(int x, int y) {
        final StringBuilder b = new StringBuilder();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < i * x + y; j++) {
                b.append('-');
            }
            b.append('\n');
        }
        return b.delete(b.length() - 1, b.length()).toString();
    }

    /* strPatternB - prints a string multiple times, separated by commas. 
          strPatternB("abc", 3) -> "abc, abc, abc"
          strPatternB("x", 4) -> "x, x, x, x"
          strPatternB("ttfn", 1) -> "ttfn"
     */
    public String strPatternB(String str, int n) {
        if (str.isEmpty()) {
            return str;
        }
        final StringBuilder b = new StringBuilder();
        for (int i = 0; i + 1 < n; i++) {
            b.append(str);
            b.append(", ");
        }
        b.append(str);
        return b.toString();
    }

    /* percToLetterGrade - Converts from a [0,100] to an N,D,C,B,A letter grade
          percToLetterGrade(89.9) -> "B"
          percToLetterGrade(61.8) -> "D"
          percToLetterGrade(55.5) -> "N"
     */
    public String percToLetterGrade(double perc) {
        if (perc >= 90.0) return "A";
        if (perc >= 80.0) return "B";
        if (perc >= 70.0) return "C";
        if (perc >= 60.0) return "D";
        return "N";
    }

    /* strToList - Converts a list of integers into an array. The string is 
        deliminated by a character given as a parameter. It returns null
        in an invalid chacter is found in the string. Note that the helper
        method tryParseInt is provided for you below.
          strToList("1,2,3,4", ',') -> { 1, 2, 3, 4 }
          strToList("5; 2", ';') -> { 5, 2 }
          strToList("5; 2", ',') -> null
     */
    public int[] strToList(String strList, char delim) {
        final String[] splitList = strList.split("\\" + Character.toString(delim));
        if (splitList.length <= 0) {
            return null;
        }

        final ArrayList<Integer> intList = new ArrayList<>(splitList.length);
        for (int i = 0; i < splitList.length; i++) {
            final String raw = splitList[i].trim();
            if (raw.isEmpty()) {
                continue;
            }

            try {
                intList.add(Integer.parseInt(raw));
            }
            catch (NumberFormatException nfe) {
                return null;
            }
        }
        final int[] result = new int[intList.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = intList.get(i);
        }
        return result;
    }

    // Helper method - converts an integer contained in a string to an Integer
    //  type. If an integer is not contained in the string, it will return null.
    public Integer tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
