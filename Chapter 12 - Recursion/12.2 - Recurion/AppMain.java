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

        System.out.println("-------- powR --------");
        assertEq(practice.pow(0, 1), practice.powR(0, 1));
        assertEq(practice.pow(25, 8), practice.powR(25, 8));
        assertEq(practice.pow(7,999), practice.powR(7, 999));
        assertEq(practice.pow(8, 3), practice.powR(8, 3));
        assertEq(practice.pow(9, 0), practice.powR(9, 0));

        System.out.println("\n-------- createStringR --------");
        assertEq(practice.createString(0), practice.createStringR(0));
        assertEq(practice.createString(1), practice.createStringR(1));
        assertEq(practice.createString(2), practice.createStringR(2));
        assertEq(practice.createString(3), practice.createStringR(3));
        assertEq(practice.createString(4), practice.createStringR(4));
        assertEq(practice.createString(100), practice.createStringR(100));

        System.out.println("\n-------- printBinary --------");
        System.out.print("(should be 0101) printBinary(5) => ");
        practice.printBinary(5);
        System.out.print("(should be 011001001) printBinary(201) => ");
        practice.printBinary(201);
        System.out.print("(should be 0) printBinary(0) => ");
        practice.printBinary(0);

        System.out.println("\n-------- printBinaryR --------");
        System.out.print("(should be 0101) printBinaryR(5) => ");
        practice.printBinaryR(5);
        System.out.print("(should be 011001001) printBinaryR(201) => ");
        practice.printBinaryR(201);
        System.out.print("(should be 0) printBinaryR(0) => ");
        practice.printBinaryR(0);
    }
}
