public class ArethmaticSequence extends CachedSequence {
    private final double firstTerm;
    private final double addend;

    public ArethmaticSequence(double firstTerm, double addend) {
        super(10);
        this.firstTerm = firstTerm;
        this.addend = addend;
    }

    @Override
    protected double[] getFirstTerms() {
        return new double[] {firstTerm};
    }

    @Override
    protected double getNextTerm(int prevTermIdx) {
        return getTermAt(prevTermIdx) + addend;
    }
}
