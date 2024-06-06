
/* There are some specific rules that your code needs to follow...
 *  1. Replace words as follows. 
 *      hello -> ahoy 
 *      my -> me
 *      friend -> bucko
 *      sir -> matey
 *      stranger -> scurvy dog
 *      where -> whar
 *      is -> be
 *      the -> th'
 *      you -> ye
 *      old -> barnacle covered
 *  2. If a word begins with a capital letter, then when you translate it, it should also begin with a 
 *      capital letter. Ignore the case of the rest of the word. 
 *      Examples...
 *          translate "Where" / "WHERE" / "WhErE" / "WhERE" / etc. as "Whar", 
 *              but translate "where" / "wHERE" / "wHeRe" / "wHere" / etc. as "whar". 
 *          You should only output "Whar" or "whar" as a translation for any form of "where",
 *              none of the other letters in "Whar" or "whar" should ever be capitalized apart from 
 *              possibly the first. 
 * 3. Pirates have a habit of saying "Arrr". Start your pirate text with "Arrr! ".
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;

public class PirateConverter {
    private static final Map<String, String> pirateMap = new Hashtable<String, String>();
    static {
        pirateMap.put("hello", "ahoy");
        pirateMap.put("my", "me");
        pirateMap.put("friend", "bucko");
        pirateMap.put("sir", "matey");
        pirateMap.put("stranger", "scurvy dog");
        pirateMap.put("where", "whar");
        pirateMap.put("is", "be");
        pirateMap.put("the", "th'");
        pirateMap.put("you", "ye");
        pirateMap.put("old", "barnacle covered");
    }

    private static String translate(String word) {
        final String loweredWord = word.toLowerCase();
        if (!pirateMap.containsKey(loweredWord)) {
            return word;
        }
        final String loweredTranslation = pirateMap.get(loweredWord);
        final String origFirstLetter = word.substring(0, 1);
        if (origFirstLetter.equals(origFirstLetter.toUpperCase())) {
            final String transFirstLetter = loweredTranslation.substring(0, 1);
            final String restOfTrans = loweredTranslation.substring(1);
            return transFirstLetter.toUpperCase() + restOfTrans;
        }
        return loweredTranslation;
    }
    private static String trimToWord(String untrimmed) {
        return untrimmed.replaceAll("(^\\W+)|(\\W+$)", "");
    }
    private static String translateWithExtra(String untrimmedWord) {
        final String trimmedWord = trimToWord(untrimmedWord);
        int wordStart;
        for (wordStart = 0; wordStart <= untrimmedWord.length(); wordStart++) {
            if (untrimmedWord.substring(wordStart).startsWith(trimmedWord)) {
                break;
            }
        }
        final String prefix = untrimmedWord.substring(0, wordStart);
        final String postfix = untrimmedWord.substring(wordStart + trimmedWord.length());

        final String translatedWord = translate(trimmedWord);
        return prefix + translatedWord + postfix;
    }
    
    private String txt_en = null;
    private String txt_pr = null;

    public String getEnglishText() {
        return txt_en;
    }
    public String getPirateText() {
        return txt_pr;
    }

    public boolean fileToPirateTalk(String filename) {
        txt_en = null;
        txt_pr = null;

        final File file = new File(filename);
        if (!file.canRead()) {
            return false;
        }
        
        final StringBuilder b_en = new StringBuilder();
        final StringBuilder b_pr = new StringBuilder("Arr! ");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                final String line = scanner.nextLine();
                b_en.append(line);
                b_en.append("\n");
                int i = 0;
                while (i < line.length()) {
                    int nextSpace = line.indexOf(" ", i);
                    if (nextSpace == i) {
                        b_pr.append(" ");
                        i++;
                    }
                    else if (nextSpace < 0) {
                        final String rawEnd = line.substring(i);
                        final String translatedEnd = translateWithExtra(rawEnd);
                        b_pr.append(translatedEnd);
                        break;
                    }
                    else {
                        final String nextWord = line.substring(i, nextSpace);
                        final String translatedWord = translateWithExtra(nextWord);
                        b_pr.append(translatedWord);
                        i = nextSpace;
                    }
                }
                b_pr.append("\n");
            }
        }
        catch (FileNotFoundException fnfe) {
            return false;
        }

        txt_en = b_en.toString();
        txt_pr = b_pr.toString();

        return true;
    }
}
