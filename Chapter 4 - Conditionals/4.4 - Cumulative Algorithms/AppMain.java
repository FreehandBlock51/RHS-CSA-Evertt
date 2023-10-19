public class AppMain {
    public static void main(String[] args) {
        System.out.println("-------- alphabet --------");
        // add your test cases here
        System.out.println("alphabet('a', 5) -> \"abcde\": " + (Cumulative.alphabet('a', 5).contentEquals("abcde")));
        System.out.println("alphabet('c', 4) -> \"cdef\": " + (Cumulative.alphabet('c', 4).contentEquals("cdef")));
        System.out.println("alphabet('y', 6) -> \"yzabcd\": " + (Cumulative.alphabet('y', 6).contentEquals("yzabcd")));

        System.out.println("------- factorSum --------");
        // add your test cases here
        System.out.println("factorSum(6) -> 6: " + (Cumulative.factorSum(6) == 6));
        System.out.println("factorSum(14) -> 10: " + (Cumulative.factorSum(14) == 10));

        System.out.println("------- isPerfect --------");
        // add your test cases here
        System.out.println("isPerfect(6) -> true: " + (Cumulative.isPerfect(6) == true));
        System.out.println("isPerfect(14) -> false: " + (Cumulative.isPerfect(14) == false));
    }
}
