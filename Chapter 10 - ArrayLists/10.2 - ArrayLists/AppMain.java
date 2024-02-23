import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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

    @SuppressWarnings("rawtypes")
    private static void assertIterablesAreEqual(Iterable a, Iterable b) {
        System.out.println("Testing that " + a + " has the same items as " + b + "...");
        Iterator aIter = a.iterator();
        Iterator bIter = b.iterator();
        while (aIter.hasNext() & bIter.hasNext()) {
            assertEq(aIter.next(), bIter.next());
        }
        assertThat(!aIter.hasNext() & !bIter.hasNext(), "Iterators " + a.toString() + " and " + b.toString() + " are of different lengths!");
        System.out.println("Test " + a + " is equivalent to " + b + " passed!");
    }

    private static class ArrayIterable<T> implements Iterable<T> {
        private final T[] arr;
        public ArrayIterable(T[] arr) {
            this.arr = arr;
        }
        @Override
        public Iterator<T> iterator() {
            return Arrays.stream(arr).iterator();
        }
        @Override
        public String toString() {
            return Arrays.toString(arr);
        }
    }

    private static <T> void assertArrayListEqualsArray(ArrayList<T> list, T[] arr) {
        assertIterablesAreEqual(list, new ArrayIterable<>(arr));
    }

    private static <T> void testToArrayList(T[] arr) {
        ArrayList<T> list = Practice.toArrayList(arr);
        System.out.println("Testing " + Arrays.toString(arr) + " == " + list.toString() + " ...");
        assertEq(list.size(), arr.length);
        for (int i = 0; i < arr.length; i++) {
            assertEq(list.get(i), arr[i]);
        }
    }

    public static void main(String[] args) {
        Practice practice = new Practice();

        System.out.println("-------- toArrayList --------");
        testToArrayList(new String[] {"ewfesd", "asdz", "3"});
        testToArrayList(new String[] {"a", "b", "c"});
        testToArrayList(new String[0]);
        assertEq(Practice.toArrayList(null), null);

        System.out.println("\n-------- reverseList --------");
        assertArrayListEqualsArray(Practice.reverseList(new String[] {"a", "b", "c"}), new String[] { "c", "b", "a" });
        assertArrayListEqualsArray(Practice.reverseList(new String[] {""}), new String[] {""});
        assertEq(Practice.reverseList(null), null);

        System.out.println("\n-------- splitString --------");
        assertArrayListEqualsArray(Practice.splitString("This is a test"), new String[] {"This", "is", "a", "test"});
        assertArrayListEqualsArray(Practice.splitString("Hi. What, is your name?"), new String[] { "Hi", "What", "is", "your", "name" });
        assertEq(Practice.splitString(null), null);

        System.out.println("\n-------- justPrimes --------");
        assertArrayListEqualsArray(Practice.justPrimes(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 25, 89}), new Integer[] {2, 3, 5, 7, 89});
        assertArrayListEqualsArray(Practice.justPrimes(new int[0]), new Integer[0]);
        assertArrayListEqualsArray(Practice.justPrimes(new int[] {1,6,4,10}), new Integer[0]);
        assertArrayListEqualsArray(Practice.justPrimes(new int[] {1,3,5,-7,-1,13}), new Integer[] {3,5,13});
        assertEq(Practice.justPrimes(null), null);
    }
}
