public final class Circle extends Shape {
    public static final String SHAPE_NAME = "Circle";

    private final double radius;

    public Circle(String color, double radius) {
        super(SHAPE_NAME, color);
        this.radius = radius;
    }

    @Override
    public double calcArea() {
        return radius * radius * Math.PI;
    }
    
    @Override
    public double calcPerimeter() {
        return 2 * radius * Math.PI;
    }
}
