public class CreditCard {
    private final String name;
    private final int creditLimit;
    public static final int DEFAULT_CREDIT_LIMIT = 2000;
    private int balance;

    public CreditCard(String name, int creditLimit) {
        this.name = name;
        if (creditLimit < 0) {
            throw new RuntimeException("Credit limit must be at least 0!");
        }
        this.creditLimit = creditLimit;
        this.balance = 0;
    }
    public CreditCard(String name) { this(name, DEFAULT_CREDIT_LIMIT); }

    public boolean makePurchase(int amtToSpend) {
        if (balance - amtToSpend < -creditLimit) {
            return false;
        }
        balance -= amtToSpend;
        return true;
    }
    public void makePayment(int amtToPay) {
        balance += amtToPay;
    }

    public int getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        if (balance < 0) {
            return name + ": " + -balance + " due";
        }
        else {
            return name + ": " + balance + " saved";
        }
    }
}
