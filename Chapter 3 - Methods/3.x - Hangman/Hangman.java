import java.util.Scanner;

public class Hangman {

	private static final String[] words = {"hangman", "banana", "computer", "dog", "rain", "water", "terminal", "midnight", "redmond", "science", "cat" };
	private static String word;
	private static String hiddenLetters;
	private static int incorrectCount = 0;

	public static void main(String[] args) {
		getWord();
		getGuesses();
	}

	public static void getGuesses() {
		Scanner scan = new Scanner(System.in);

		while (incorrectCount < 7 && hiddenLetters.contains("*")) {
			System.out.println("Guess a letter in the word:");
			System.out.println(hiddenLetters);
			String guess = scan.next();
			
    		while (guess.matches(".*[^a-z].*")) {
        		System.out.println("Please enter letters only, try again");
        		guess = scan.next();
    		}
			while (guess.length() > 1) {
        		System.out.println("Please enter a single letter only, try again");
        		guess = scan.next();
    		}
			hang(guess);
		}
		scan.close();
	}

	public static void hang(String guess) {
		String updatedLetters = "";
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == guess.charAt(0)) {
				updatedLetters += guess.charAt(0);
			} else if (hiddenLetters.charAt(i) != '*') {
				updatedLetters += word.charAt(i);
			} else {
				updatedLetters += "*";
			}
		}

		if (hiddenLetters.equals(updatedLetters)) {
			incorrectCount++;
			printHangman();
		} else {
			hiddenLetters = updatedLetters;
		}
		if (hiddenLetters.equals(word)) {
			System.out.println("Correct! You win! The word was " + word);
		}
	}

	// Part 1
	// Prints out the game image to the terminal based on how many incorrect guesses have been made.
	// This game has 7 guesses. Be creative!
	public static void printHangman() {
		if (incorrectCount > 0) {
			System.out.println("Wrong guess, try again!");
		}

		switch (incorrectCount) {
			case 0:
				System.out.println("┏━━┓\n┃\n┃  \n┃   \n┃   \n┻━━━━━");
				break;
			case 1:
				System.out.println("┏━━┓\n┃  o\n┃  \n┃   \n┃   \n┻━━━━━");
				break;
			case 2:
				System.out.println("┏━━┓\n┃  o\n┃  +\n┃   \n┃   \n┻━━━━━");
				break;
			case 3:
				System.out.println("┏━━┓\n┃  o\n┃ -+\n┃   \n┃   \n┻━━━━━");
				break;
			case 4:
				System.out.println("┏━━┓\n┃  o\n┃ -+-\n┃   \n┃   \n┻━━━━━");
				break;
			case 5:
				System.out.println("┏━━┓\n┃  o\n┃ -+-\n┃  |\n┃   \n┻━━━━━");
				break;
			case 6:
				System.out.println("┏━━┓\n┃  o\n┃ -+-\n┃  |\n┃  ^\n┻━━━━━");
				break;
			case 7:
				System.out.println("┏━━┓\n┃  o\n┃ -+-\n┃  |\n┃  ^\n┻━   ━");
				break;
		
			default:
				System.err.println("Player has made too many errors; game is over");
		}
	}

	// Part 2: 
	// Modify this function to get the word to guess from user input in the terminal
	public static void getWord() {
		word = new String(System.console().readPassword("Enter the word: "));
		hiddenLetters = new String(new char[word.length()]).replace("\0", "*");
	}
}