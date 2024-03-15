import java.util.Objects;

public class AppMain {
    private static void assertThat(boolean bool, String errMsg) {
        assert bool;
        
        if (!bool) {
            throw new AssertionError(errMsg);
        }
    }

    private static void assertEq(Object a, Object b) {
        assertThat(Objects.equals(a, a), "Objects.equals(" + Objects.toString(a) + ", " + Objects.toString(a) + ") failed! (a != a)");
        assertThat(Objects.equals(b, b), "Objects.equals(" + Objects.toString(b) + ", " + Objects.toString(b) + ") failed! (b != b)");
        assertThat(Objects.equals(a, b), "Objects.equals(" + Objects.toString(a) + ", " + Objects.toString(b) + ") failed! (a != b)");
        assertThat(Objects.equals(b, a), "Objects.equals(" + Objects.toString(b) + ", " + Objects.toString(a) + ") failed! (b != a)");

        System.out.println("assertion " + Objects.toString(a) + " == " + Objects.toString(b) + " passed!");
    }

    public static void main(String[] args) {
        Practice practice = new Practice();

        System.out.println("-------- isPalindrome --------");
        assertEq(practice.isPalindrome("racecar"), true);
        assertEq(practice.isPalindrome("test"), false);

        System.out.println("\n-------- sumDigits --------");
        assertEq(practice.sumDigits(123), 6);
        assertEq(practice.sumDigits(572), 14);
        assertEq(practice.sumDigits(116), 8);

        System.out.println("\n-------- createString --------");
        assertEq(practice.createString(3), "321.123");
        assertEq(practice.createString(5), "54321.12345");
        assertEq(practice.createString(1), "1.1");
        assertEq(practice.createString(0), "");
        
        System.out.println("\n-------- minValue --------");
        assertEq(practice.minValue(new int[] {-3, 7, -8, 4, 0}, 0), -8);
        assertEq(practice.minValue(new int[] {8, 7, 1, 10, 6, 9}, 3), 6);
        assertEq(practice.minValue(new int[] {-1, 0, 1, 2, 3}, 1), 0);
    }
}
