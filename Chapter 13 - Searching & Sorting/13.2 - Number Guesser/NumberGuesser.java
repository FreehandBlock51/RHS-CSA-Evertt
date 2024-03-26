public class NumberGuesser extends NumberGuesserBase {
    /* Things you can use & how your methods should behave...
     *   Your methods should repeatedly call the parent class method guess(int n)
     *   to guess a number, until a match is found. That method returns...
     *       0 : You are correct (you got it!)
     *      -1 : My number is smaller than your guess
     *       1 : My number is larger than your guess
     * Each of your methods should return the correctly guessed number. */

    /* guessNumberBasic() should be a very simple linear/sequential
     *   guesser (i.e. it should guess 1, 2, 3, ... till it finds it).
     *   This method should not attempt to minimize guesses, it is purely 
     *   a linear / sequential guesser. Keep it simple. */
    public int guessNumberBasic() {
        int g = (NumberGuesserBase.MIN_NUMBER + NumberGuesserBase.MAX_NUMBER) / 2;
        int result;
        do {
            result = guess(g);
            g += result;
        } while (result != 0);
        return g;
    }

    private final boolean USE_RECURSION = false;

    /* guessNumberFast() should try to guess the number with the minimum
     *   number of guesses. This is the method I will judge you on. 
     *   Unlike the sequential guesser, this method should attempt to 
     *   minimize the number of guesses it takes to guess the answer. */
    public int guessNumberFast() {
        int blockStart = NumberGuesserBase.MIN_NUMBER;
        int blockEnd = NumberGuesserBase.MAX_NUMBER + 1; // so floor on division doesn't break us
        if (USE_RECURSION) {
            return guessNumberFastR(blockStart, blockEnd);
        }
        int result;
        int middle;
        do {
            middle = (blockStart + blockEnd) / 2;
            result = guess(middle);
            if (result < 0) {
                blockEnd = middle;
                continue;
            }
            if (result > 0) {
                blockStart = middle;
                continue;
            }
        } while (result != 0 && blockStart <= blockEnd);
        return middle;
    }
    public int guessNumberFastR() {
        return guessNumberFastR(NumberGuesserBase.MIN_NUMBER, NumberGuesserBase.MAX_NUMBER + 1);
    }
    public int guessNumberFastR(int min, int max) {
        int result;
        int middle;
        middle = (min + max) / 2;
        result = guess(middle);
        if (result == 0 || min > max) {
            return middle;
        }
        if (result < 0) {
            max = middle;
        }
        if (result > 0) {
            min = middle;
        }
        return guessNumberFastR(min, max);
    }
}
