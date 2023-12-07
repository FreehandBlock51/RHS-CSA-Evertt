public class FibonnaciSequence extends Sequence {
    private static final double[] FIRST_TERMS = new double[] {0, 1};

    @Override
    public double getTermAt(int index) {
        if (index == 0) {
            return FIRST_TERMS[0];
        }
        double cur = FIRST_TERMS[1],
         prev = FIRST_TERMS[0],
         tmp;

        for (int i = 1; i < index; i++) {
            tmp = cur;
            cur += prev;
            prev = tmp;
        }
        return cur;
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
