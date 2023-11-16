public class AppMain {
    public static void main(String[] args) {
        int n = 1, count = 0;
        while (count < 20) {
            if (isPrime(n)) {
                System.out.println(n + " is prime");
                count++;
            }
            if (++n < 0) {
                System.err.println("FAIL: Integer Overflow!");;
                break;
            }
        }
        System.out.println("Found " + count + " primes!");
    }

    static boolean isPrime(int n) {
        n = Math.abs(n);
        for (int factor = 2; factor < n; factor++) {
            if (n % factor == 0) {
                return false;
            }
        }
        return true;
    }
}
