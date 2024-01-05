public final class StrongCreature extends Creature {
    public static final String TYPE = "Strong";
    public static final int STRENGTH = 30;
    public static final int HIT_CHANCE = 50;

    public StrongCreature(String name) {
        super(name, STRENGTH, HIT_CHANCE);
    }

    @Override
    public String toString() {
        return super.toString() + "(" + TYPE + ")";
    }
}
