public final class FastCreature extends Creature {
    public static final String TYPE = "Fast";
    public static final int STRENGTH = 15;
    public static final int HIT_CHANCE = 50;
    public static final int ATTACKS_PER_TURN = 2;

    public FastCreature(String name) {
        super(name, STRENGTH, HIT_CHANCE);
    }

    @Override
    public void performAttack(Creature target) {
        for (int i = 0; i < ATTACKS_PER_TURN; i++) {
            super.performAttack(target);
        }
    }

    @Override
    public String toString() {
        return super.toString() + "(" + TYPE + ")";
    }
}
