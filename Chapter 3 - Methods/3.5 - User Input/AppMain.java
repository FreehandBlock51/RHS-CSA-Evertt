import java.util.Calendar;
import java.util.Scanner;

public class AppMain {
    public static void main(String[] args) {
        // Create a Scanner object.
        // Asks the user to type their age, the current year, a future year, and their name.
        // Print out "<Name> will be <computed age> years old in <future year>".

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("What is your name? ");
            String name = scanner.nextLine();

            System.out.print("What is your current age? ");
            int curAge = scanner.nextInt();

            System.out.print("What is the current year? ");
            int curYear = scanner.nextInt();

            System.out.print("What year are you trying to calculate your age at? ");
            int targetYear = scanner.nextInt();

            int yearsUntilTarget = targetYear - curYear;

            int targetAge = curAge + yearsUntilTarget;
            System.out.printf("%s will be %d years old in %d\n", name, targetAge, targetYear);
        }
    }
}
