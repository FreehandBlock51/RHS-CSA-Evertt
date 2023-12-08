public final class Line {
    private final double slope, yIntercept;

    public double getSlope() {
        return slope;
    }
    public double getYIntercept() {
        return yIntercept;
    }

    public Line(double slope, double yIntercept) {
        this.slope = slope;
        this.yIntercept = yIntercept;
    }
    public Line(double x1, double y1, double x2, double y2) {
        this.slope = (y2 - y1) / (x2 - x1);
        this.yIntercept = y1 - (slope * x1);
    }

    public double getY(double x) {
        return slope * x + yIntercept;
    }
    public double getX(double y) {
        return (y - yIntercept) / slope;
    }

    @Override
    public String toString() {
        return "y = " + slope + "x + " + yIntercept;
    }

    public void printIntercept(Line other) {
        // f1(x) = f2(x)
        // m1x + b1 = m2x + b2
        // m1x - m2x = b2 - b1
        // x = (b2 - b1) / (m1 - m2)
        // y = f1(x) or y = f2(x)

        final double m1 = this.slope,
                     b1 = this.yIntercept,
                     m2 = other.slope,
                     b2 = other.yIntercept;
        final double x = (b2 - b1) / (m1 - m2);
        final double y = getY(x);
        if (Double.isInfinite(x) // anything / 0 = Infinity (will occur when m1=m2, but b1!=b2)
         || !Double.isFinite(y)) { // if there is no y (y=Infinity or y=NaN), one of the lines doesn't have a point at the intercept, meaning there is no intercept
            System.out.print("No Intercept!");
        }
        else if (Double.isNaN(x)) { // 0 / 0 = NaN (will occur when m1=m2 and b1=b2)
            System.out.print("Lines are the same!");
        }
        else {
            System.out.print("(" + x + ", " + y + ")");
        }
    }

    // ooo very complex calculus so hard to understand oooooo
    public double getDerivativeAt(double x) {
        return slope;
    }
}
