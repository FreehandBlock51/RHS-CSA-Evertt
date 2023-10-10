public class AppMain {
    public static void main(String[] args) {
        // Line 1
        for (int i = 0; i < 5; i++) {
            System.out.print(randInt(100) + " ");
        }
        System.out.println();

        // Line 2
        for (int i = 0; i < 5; i++) {
            System.out.print(randInt(100, 500) + " ");
        }
        System.out.println();
    }
    
    // Put your functions here...
    // Function 1 should: return a value with a maximum of a provided value, [0,𝑚𝑎𝑥𝑉𝑎𝑙𝑢𝑒).
    // Function 2 should: should return a value between two provided doubles, [𝑚𝑖𝑛𝑉𝑎𝑙𝑢𝑒,𝑚𝑎𝑥𝑉𝑎𝑙𝑢𝑒).

    public static int randInt(int maxValue) {
        return randInt(0, maxValue);
    }

    public static int randInt(int minValue, int maxValue) {
        if (maxValue < minValue) { throw new IllegalArgumentException("maxValue must be larger than minValue!"); }
        int range = maxValue - minValue; // the range of the random number
        return (int)(Math.random() * range) + minValue;
    }
}
