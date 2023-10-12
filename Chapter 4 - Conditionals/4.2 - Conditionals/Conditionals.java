public class Conditionals {
    public static boolean firstStars(String text) {
        // Returns true under the following conditions (false in all others).
        //  If the string has three or less characters, then all characters must be stars/asterisks.
        //  If the string has more than three characters, then the first three must be stars/asterisks.
        
        // Examples...
        //  minThreeStars("**") -> true
        //  minThreeStars("*-") -> false
        //  minThreeStars("***abc") -> true
        //  minThreeStars("**a*") -> false

        return text.length() > 0 && String.join("", text, "***").startsWith("***");
    }

    public static boolean compareDouble(double number, double expected) {
        // Returns true if number is close to expected.
        //  Use the constant EPSILON, defined in AppMain for this function.
        //  If number is within EPSILON of expected (exclusive), then return true. False otherwise.
        
        // Examples...
        //  compareDouble(6.001, 6) -> true
        //  compareDouble(6.011, 6) -> false
        //  compareDouble(-1.1, -1) -> false
        return Math.abs(number - expected) <= AppMain.EPSILON;
    }

    public static boolean logicCheck(int x, int y, boolean b) {
        // Rewrite the following logic into a single return statement.
        //  You cannot use any if statements, only a single return statement.
        //  The logic should remain the same.

        return x > 10 || (x > y && b);
    }
}
