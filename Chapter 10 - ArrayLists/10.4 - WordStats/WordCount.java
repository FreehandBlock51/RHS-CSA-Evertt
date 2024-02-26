public final class WordCount implements Comparable<WordCount> {
    private final String word;
    private final int count;

    private WordCount(String word, int count) {
        this.word = word;
        this.count = count;
    }

    private static int countWord(String word, String[] words) {
        int count = 0;
        for (String w : words) {
            if (w.equals(word)) {
                count++;
            }
        }
        return count;
    }

    public static WordCount getWordCount(String word, String text) {
        return getWordCount(word, text.split(" "));
    }
    public static WordCount getWordCount(String word, String[] words) {
        return new WordCount(word.intern(), countWord(word, words));
    }

    public String getWord() {
        return word;
    }
    public int getCount() {
        return count;
    }

    @Override
    public int compareTo(WordCount o) {
        if (this.count > o.count) {
            return -1;
        }
        else if (this.count < o.count) {
            return 1;
        }
        else {
            return Character.compare(this.word.charAt(0), o.word.charAt(0));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof WordCount && compareTo((WordCount)other) == 0;
    }

    @Override
    public int hashCode() {
        final int countHash = Integer.hashCode(count);
        final int wordHash = word.hashCode();
        return
            ((countHash & 0xFF00) ^ ((countHash << 16) & 0xFF00)) |
            ((wordHash & 0x00FF) ^ ((wordHash >> 16) & 0x00FF));
    }

    @Override
    public String toString() {
        return this.word + " (" + this.count + ")";
    }
}
