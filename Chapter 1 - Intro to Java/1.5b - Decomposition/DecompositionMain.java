public class DecompositionMain {
    public static void main(String[] args) {
        printUpperTriangle();
        printSquare();
        printUS();
        printSquare();
        printUpperTriangle();
    }

    static void printUpperTriangle() {
        System.out.println("   /\\"   );
        System.out.println("  /  \\"  );
        System.out.println(" /    \\" );
    }
    static void printSquare() {
        System.out.println("+------+" );
        System.out.println("|      |" );
        System.out.println("|      |" );
        System.out.println("+------+" );
    }
    static void printUS() {
        System.out.println("|UNITED|" );
        System.out.println("|STATES|" );
    }
}
