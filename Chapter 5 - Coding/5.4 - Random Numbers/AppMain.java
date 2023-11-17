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
        int die = DICE[(int)Math.floor(Math.min(Math.random(), 0.9) * DICE.length)];
        long sum = 0;
        System.err.println("Rolling a d" + die + " " + ROLLCOUNT + " times...");
        for (int i = 1; i <= ROLLCOUNT; i++) {
            long roll = (long)Math.ceil(Math.random() * die);
            System.err.println("Roll " + i + ": " + roll);
            sum += roll;
        }
        long average = sum / ROLLCOUNT;
        System.out.println("Average of " + ROLLCOUNT + " rolls of a d" + die + ": " + average);
    }
}
