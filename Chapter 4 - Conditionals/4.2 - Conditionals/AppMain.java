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
        assert Conditionals.compareDouble(6.001, 6) == true;
        assert Conditionals.compareDouble(6.011, 6) == false;
        assert Conditionals.compareDouble(-1.1, -1) == false;

        System.out.println("-------- logicCheck --------");
        // add your test cases here
        assert Conditionals.logicCheck(15, 20, false) == true;
        assert Conditionals.logicCheck(9, 7, true) == true;
        assert Conditionals.logicCheck(10, 20, false) == false;
        assert Conditionals.logicCheck(5, 20, true) == false;
    }
}
