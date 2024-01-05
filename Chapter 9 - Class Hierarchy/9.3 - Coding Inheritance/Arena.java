public class Arena {
    private final int CREATURE_COUNT = 8;
    private int creaturesRemaining;

    public Arena() {
        creaturesRemaining = CREATURE_COUNT;
    }

    private static Creature makeCreature(String name) {
        switch (name) {
            case "Fluffletuft":
            case "Fuzzlenook":
            case "Quillfluff":
                return new StrongCreature(name);
            case "Shagglewisp":
            case "Puffernip":
            case "Snugglewight":
                return new FastCreature(name);
            case "Whiskerwhisp":
            case "Tanglethorn":
                return new AccurateCreature(name);
            default:
                throw new UnsupportedOperationException();
        }
    }

    private Creature nextCreature() {
        // Decrease our number remaining...
        int creatureIndex = creaturesRemaining;
        creaturesRemaining--;

        /*  Create each requested create. Use creatureIndex and the list specified
               in AppMain's app description to determine which one to create/return. */

        switch (creatureIndex) {
            case 8:
                return makeCreature("Fluffletuft");
            case 7:
                return makeCreature("Whiskerwhisp");
            case 6:
                return makeCreature("Fuzzlenook");
            case 5:
                return makeCreature("Shagglewisp");
            case 4:
                return makeCreature("Puffernip");
            case 3:
                return makeCreature("Tanglethorn");
            case 2:
                return makeCreature("Quillfluff");
            case 1:
                return makeCreature("Snugglewight");
        }
        
        return null;
    }

    // Runs a creature vs creature dual, returns the winner
    private Creature runDuel(Creature a, Creature b) {
        // Report
        System.out.println("Dueling: " + a + " vs " + b + "!");

        // Keep fighting till someone wins
        while (!a.isDefeated() && !b.isDefeated()) {
            a.performAttack(b);
            if (!b.isDefeated()) {
                b.performAttack(a);
            }
        }
        if (a.isDefeated()) {
            return b;
        }
        return a;
    }

    // Runs all the duels until an ultimate winner is found
    public void runDuels() {
        Creature a = nextCreature();
        Creature b = nextCreature();
        Creature winner = null;
        while (a != null && b != null) {
            // Run the dual
            winner = runDuel(a, b);

            // Report
            System.out.println(" winner: " + winner);

            if (winner == a) {
                b = nextCreature();
            } else {
                a = nextCreature();
            }
            winner.resetHealth();
        }

        // Report the final winner...
        if (winner != null) {
            System.out.println("Ultimate champion: " + winner + "!!!");
        }
        else {
            System.out.println("No winner found :(");
        }
    }
}
