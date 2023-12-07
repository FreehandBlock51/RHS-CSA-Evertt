import java.util.ArrayList;
import java.util.List;

public abstract class CachedSequence extends Sequence {
    private final List<Double> itemCache;
    private final int cachedIndexes;

    protected CachedSequence(int cachedIndexes) {
        itemCache = new ArrayList<>(0);
        this.cachedIndexes = cachedIndexes;
    }
    
    @Override
    public double getTermAt(int index) {
        if (index >= getFirstTerms().length && index % cachedIndexes == 0) {
            if (index / cachedIndexes <= itemCache.size()) {
                return itemCache.get((index / cachedIndexes) - 1);
            }
            else if (index / cachedIndexes == itemCache.size() + 1) {
                itemCache.add(getNextTerm(index - 1));
                return itemCache.get(itemCache.size() - 1);
            }
        }
        return super.getTermAt(index);
    }
}
