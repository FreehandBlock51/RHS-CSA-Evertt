public class AppMain {
    public static final double EPSILON = 0.01;

    public static void main(String[] args) {
        System.out.println("-------- firstStars --------");
        // add your test cases here
        System.out.println("(\"**\" -> true) " + (Conditionals.firstStars("**") == true));
        System.out.println("(\"*-\" -> false) " + (Conditionals.firstStars("*-") == false));
        System.out.println("(\"***abc\" -> true) " + (Conditionals.firstStars("***abc") == true));
        System.out.println("(\"**a*\" -> false) " + (Conditionals.firstStars("**a*") == false));

        System.out.println("------ compareDouble -------");
        // add your test cases here
        System.out.println("(6.001, 6 -> true) " + (Conditionals.compareDouble(6.001, 6) == true));
        System.out.println("(6.011, 6 -> false) " + (Conditionals.compareDouble(6.011, 6) == false));
        System.out.println("(-1.1, -1 -> false) " + (Conditionals.compareDouble(-1.1, -1) == false));

        System.out.println("-------- logicCheck --------");
        // add your test cases here
        System.out.println("(15, 20, false -> true) " + (Conditionals.logicCheck(15, 20, false) == true));
        System.out.println("(9, 7, true -> true) " + (Conditionals.logicCheck(9, 7, true) == true));
        System.out.println("(10, 20, false -> false) " + (Conditionals.logicCheck(10, 20, false) == false));
        System.out.println("(5, 20, true -> false) " + (Conditionals.logicCheck(5, 20, true) == false));
    }
}
