
public class Rectangle extends Shape {
    public static final String SHAPE_NAME = "Rectangle";

    private final double width;
    private final double height;

    protected Rectangle(String name, String color, double width, double height) {
        super(name, color);
        this.width = width;
        this.height = height;
    }

    public Rectangle(String color, double width, double height) {
        this(SHAPE_NAME, color, width, height);
    }

    @Override
    public final double calcArea() {
        // To be implemented in subclasses
        return width * height;
    }

    @Override
    public final double calcPerimeter() {
        // To be implemented in subclasses
        return (2 * width) + (2 * height);
    }
}
