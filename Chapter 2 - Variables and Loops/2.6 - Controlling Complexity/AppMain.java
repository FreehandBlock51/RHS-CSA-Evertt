public class AppMain {
    // Desired output (two different examples, depending on what the constant is set to)...
    // SIZE = 3
    //   #============#
    //   |    <><>    |
    //   |  <>....<>  |
    //   |<>........<>|
    //   |<>........<>|
    //   |  <>....<>  |
    //   |    <><>    |
    //   #============#
    // SIZE = 4
    //   #================#
    //   |      <><>      |
    //   |    <>....<>    |
    //   |  <>........<>  |
    //   |<>............<>|
    //   |<>............<>|
    //   |  <>........<>  |
    //   |    <>....<>    |
    //   |      <><>      |
    //   #================#
    
    public static void main(String[] args) throws Exception {
        drawEnclosedDiamond(3);
        drawEnclosedDiamond(4);
    }

    public static void drawEnclosedDiamond(int size) throws Exception {
        if (size <= 0) { throw new Exception("size must be greater than 0!"); }
        System.out.println("SIZE = " + size);
        drawTopBottom(size);
        drawMiddle(size);
        drawTopBottom(size);
    }
    public static void drawTopBottom(int size) throws Exception {
        System.out.print("#");
        for (int i = 0; i < size * 2; i++) { System.out.print("--"); } // container has a width of size*2 because every space, dot, and end are 2 characters instead of 1
        System.out.println("#");
    }
    public static void drawMiddle(int size) throws Exception {
        size--; // so the widest lines have no spaces
        for (int i = 0; i <= size; ++i) {
            drawLineOfShape(i, size);
        }
        for (int i = size; i >= 0; i--) {
            drawLineOfShape(i, size);
        }
    }
    static void drawLineOfShape(int shapeWidth, int containerWidth) throws Exception {
        final int amtOfSpaces = (containerWidth - shapeWidth); // the amount of spaces to print on each side
        if (amtOfSpaces < 0) { throw new Exception("containerWidth cannot be less than shapeWidth!"); }

        // spaces and dots are doubled because ending sequence ('<>') is 2 characters long
        System.out.print("|");
        for (int i = 0; i < amtOfSpaces; i++) { System.out.print("  "); }
        System.out.print("<>");
        for (int i = 0; i < shapeWidth; i++) { System.out.print("...."); }
        System.out.print("<>");
        for (int i = 0; i < amtOfSpaces; i++) { System.out.print("  "); }
        System.out.println("|");
    }
}
