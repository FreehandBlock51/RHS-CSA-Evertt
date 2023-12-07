public abstract class Sequence {
    public double getTermAt(int index) {
        double[] knownTerms = getFirstTerms();
        if (index < knownTerms.length) {
            return knownTerms[index];
        }
        return getNextTerm(index - 1);
    }
    protected abstract double[] getFirstTerms();
    protected abstract double getNextTerm(int prevTermIdx);

    @Override
    public String toString() {
        String str = this.getClass().getSimpleName() + "(";
        for (int i = 0; i < 5; i++) {
            str += getTermAt(i) + ", ";
        }
        str += "...)";
        return str;
    }
}
