public class AppMain {
    /* Background: Equation of a line...
     *  In math, the equation for a line can be defined by the formula...
     *     y = m x + b
     * ...where m is the slope and b is the y intercept. A line can also be defined
     * by two points - in this case you could find the slope between the points and
     * use one point with the formula...
     *    y - y1 = m (x - x1)
     * 
     * In this lab you are writing a class called Line. The class should do the following...
     *  - Support two constructors, one for each of the ways to define a line discussed above. 
     *  - Write accessors to provide the slope and the y-intercept.
     *  - Write a method that evaluates y, given a value for x.
     *  - Write a method that evaluates x, given a value for y.
     *  - Write a toString() method that prints the equation for the line.
     *  - Write a method that PRINTS the intersection point with another line.
     * 
     * Your main method should fully test your implementation of line, printing out the
     *  results in a readable way. Your methods do not need to check for error cases, like
     *  vertical lines or the two points being the same point.
     * Include the following in your test cases...
     *     Line lineA = new Line(2, 5);
     *     Line lineB = new Line(-0.5, -10);
     *     Line lineC = new Line(0, 1, 10, -5);
     *     System.out.print("A & B: ");
     *     lineA.printIntercept(lineB);
     *     System.out.print("A & C: ");
     *     lineA.printIntercept(lineC);
     *     System.out.print("B & C: ");
     *     lineB.printIntercept(lineC);
     */

    public static void main(String[] args) {
        Line lineA = new Line(2, 5);
        Line lineB = new Line(-0.5, -10);
        Line lineC = new Line(0, 1, 10, -5);
        System.out.print("A & B: ");
        lineA.printIntercept(lineB);
        System.out.print("\nA & C: ");
        lineA.printIntercept(lineC);
        System.out.print("\nB & C: ");
        lineB.printIntercept(lineC);

        final Line parallelLine1 = new Line(3, 0),
                   parallelLine2 = new Line(3, 5);
        System.out.print("\nParallel lines have an intercept at: ");
        parallelLine1.printIntercept(parallelLine2);

        System.out.print("\nCoincident lines have an intercept at: ");
        lineA.printIntercept(lineA);
    }
}
