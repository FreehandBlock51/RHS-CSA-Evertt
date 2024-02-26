import java.util.ArrayList;

public final class WordCounts {
    private static final WordCount[] ARRAY_TYPE_INSTANCE = new WordCount[0];

    private final ArrayList<WordCount> wordCounts;

    private WordCounts(String[] words) {
        this.wordCounts = new ArrayList<>();
        for (String word : words) {
            WordCount count = WordCount.getWordCount(word, words);
            if (!wordCounts.contains(count)) {
                wordCounts.add(count);
            }
        }
        wordCounts.sort(null);
    }

    public static WordCounts getWordCounts(String text) {
        return new WordCounts(text.toLowerCase().replaceAll("[.,?!\"/\\{}\\[\\]()]", "").split(" "));
    }

    public int getWordCount(String word) throws IllegalArgumentException {
        for (WordCount count : wordCounts) {
            if (count.getWord().equals(word)) {
                return count.getCount();
            }
        }
        throw new IllegalArgumentException();
    }

    public WordCount[] getWordCounts() {
        return wordCounts.toArray(ARRAY_TYPE_INSTANCE);
    }
}
