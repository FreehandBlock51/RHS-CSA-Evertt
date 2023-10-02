public class Program {
    public static void main(String[] args) {
        System.out.println("\nSequence 1:");
        seq1();
        System.out.println("\nSequence 2:");
        seq2();
        System.out.println("\nSequence 3:");
        seq3();
        System.out.println("\nSequence 4:");
        seq4();
        System.out.println("\nSequence 5:");
        seq5();
        System.out.println("\nSequence 6:");
        seq6();
        System.out.println("\nSequence 7:");
        seq7();
    }

    static void seq1() {
        for (int i = 1; i <= 6; i++) {
            for (int j = i; j > 0; j--) {
                System.out.print(i);
            }
            System.out.println();
        }
    }

    static void seq2() {
        for (int r = 1; r <= 5; r++) {
            for (int c = r; c < 5; c++) {
                System.out.print(' ');
            }
            System.out.println(r);
        }
    }

    static void seq3() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j <= 9; j++) {
                for (int c = 0; c < 3; c++) {
                    System.out.print(j);
                }
            }
            System.out.println();
        }
    }

    static void seq4() {
        int d, curNum;
        for (int n = 0; n < 5; n++) {
            for (d = 5 - n; d > 0; d--) {
                System.out.print('-');
            }

            curNum = (n * 2) + 1;
            for (int i = 0; i < curNum; i++) {
                System.out.print(curNum);
            }
            
            for (; d < 5 - n; d++) {
                System.out.print('-');
            }
            System.out.println();
        }
    }

    static void seq5() {
        int i;
        for (int r = 9; r > 0; r--) {
            i = r;
            for (int c = 0; c < 9; c++) {
                if (i < 1) { i = 9; }
                System.out.print(i + " ");
                i--;
            }
            System.out.println();
        }
    }

    static void seq6() {
        int prevNum1;
        int prevNum2 = 0;
        int curNum = 1;
        for (int i = 0; i < 15; i++) {
            System.out.print(curNum + " ");
            prevNum1 = prevNum2;
            prevNum2 = curNum;
            curNum = prevNum1 + prevNum2;
        }
        System.out.println();
    }

    static void seq7() {
        for (int i = 0; i < 4; i++) {
            System.out.print("         |");
        }
        System.out.println();
        for (int i = 1; i <= 40; i++) {
            System.out.print(i % 10);
        }
        System.out.println();
    }
}