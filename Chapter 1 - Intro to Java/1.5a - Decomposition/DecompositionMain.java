public class DecompositionMain {
    public static void main(String[] args) {
        printUpperTriangle();
        printLowerTriangle();
        printUpperTriangle();
        printLowerTriangle();
    }

    static void printUpperTriangle() {
        System.out.println("  /\\" );
        System.out.println(" /  \\" );
        System.out.println("/    \\" );
    }
    static void printLowerTriangle() {
        System.out.println("\\    /" );
        System.out.println(" \\  /" );
        System.out.println("  \\/" );
    }
}
