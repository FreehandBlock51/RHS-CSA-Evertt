public final class AccurateCreature extends Creature {
    public static final String TYPE = "Accurate";
    public static final int STRENGTH = 20;
    public static final int HIT_CHANCE = 80;

    public AccurateCreature(String name) {
        super(name, STRENGTH, HIT_CHANCE);
    }

    @Override
    public String toString() {
        return super.toString() + "(" + TYPE + ")";
    }
}
