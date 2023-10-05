public class AppMain {
    public static void main(String[] args) {
        // Line 1
        // TODO

        // Line 2
        // TODO
    }
    
    // Put your functions here...
    // Function 1 should: return a value with a maximum of a provided value, [0,𝑚𝑎𝑥𝑉𝑎𝑙𝑢𝑒).
    // Function 2 should: should return a value between two provided doubles, [𝑚𝑖𝑛𝑉𝑎𝑙𝑢𝑒,𝑚𝑎𝑥𝑉𝑎𝑙𝑢𝑒).

    static double max(double num, double max) {
        return Math.min(num, max);
    }

    static double clamp(double num, double min, double max) {
        return Math.max(Math.min(num, max), min);
    }
}
