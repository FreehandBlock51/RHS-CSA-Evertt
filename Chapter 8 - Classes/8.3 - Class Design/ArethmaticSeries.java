public class ArethmaticSeries extends ArethmaticSequence {
    public ArethmaticSeries(double firstTerm, double addend) {
        super(firstTerm, addend);
    }
    
    @Override
    public double getTermAt(int index) {
        double sum = 0;
        for (int i = 0; i <= index; i++) {
            sum += super.getTermAt(i);
        }
        return sum;
    }
}
