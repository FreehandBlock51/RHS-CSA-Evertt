import java.util.Arrays;

public class AppMain {
    // The program should...
    //  Uses at least two functions.
    //  Picks a random die: 𝑑4, 𝑑6, 𝑑8, 𝑑10, 𝑑12, and 𝑑20.
    //  Then find the average of rolling that die rolled 𝟏𝟎𝟎 times.
    // 
    // Example output…
    //  Die: d8
    //  Average = 4.32

    private static final int ROLLCOUNT = 100;

    private static final int[] DICE = new int[] {4, 6, 8, 10, 12, 20};

    public static void main(String[] args) {
        final boolean DEBUG = Arrays.asList(args).contains("--debug");

        int die = pickRandomDie();
        double sum = 0;
        logDebug(DEBUG, "Rolling a d" + die + " " + ROLLCOUNT + " times...");
        for (int i = 1; i <= ROLLCOUNT; i++) {
            long roll = roll(die);
            logDebug(DEBUG, "Roll " + i + ": " + roll);
            sum += roll;
        }
        double average = sum / ROLLCOUNT;
        System.out.println("Die: d" + die + "\nAverage = " + average);
    }

    private static long roll(int die) {
        return (long)Math.ceil(Math.random() * die);
    }

    private static int pickRandomDie() {
        return DICE[(int)Math.floor(Math.random() * DICE.length)]; // this doesn't fail because Math.random() has a range of [0,1)
    }

    private static void logDebug(boolean debug, String msg) {
        if (debug) {
            System.err.println(msg);
        }
    }
}
