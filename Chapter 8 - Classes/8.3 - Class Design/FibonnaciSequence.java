public class FibonnaciSequence extends CachedSequence {
    private static final double[] FIRST_TERMS = new double[] {0, 1};

    public FibonnaciSequence() {
        super(5);
    }

    @Override
    protected double[] getFirstTerms() {
        return FIRST_TERMS;
    }

    @Override
    protected double getNextTerm(int prevTermIdx) {
        return getTermAt(prevTermIdx - 1) + getTermAt(prevTermIdx);
    }
    
}
