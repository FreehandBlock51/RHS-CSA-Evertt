package ai;


import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

import game.Game;
import game.PowerUp;
import game.Tank;
import game.TankAIBase;
import game.Target;
import game.Vec2;

public class TankAI extends TankAIBase {

    private static class DummyPowerUp extends PowerUp {
        public static final String POWERUP_TYPE = "D";
        public DummyPowerUp(Vec2 pos) {
            super(pos, POWERUP_TYPE);
        }
    }

    public String getPlayerName() {
        return "Dalton";  // <---- Put your first name here
    }
    public int getPlayerPeriod() {
        return 1;                // <---- Put your period here
    }
        
    // You are free to add member variables & methods to this class (and delete this comment).
    //  You should use the methods in its base class (TankAIBase) to query the world. 
    //  Note that you are not allowed to reach into game code directly or make any
    //  modifications to code in the game package. Use your judgement and ask your 
    //  teacher if you are not sure. If it feels like cheating, it probably is.
    private static final int POT_MAX = 25;
    private int potCount = 0;

    public boolean updateAI() {
        // if (tank.hasCommand()) {
        //     return false;
        // }

        // final Vec2 snappedGridPos = new Vec2( // snap tank to grid so shots work correctly
        //     Math.floor(getTankPos().x) + 0.5,
        //     Math.floor(getTankPos().y) + 0.5
        //     );
        // if (snappedGridPos.distance(getTankPos()) > 0.5) {
        //     return queueCmd("move", getTankPos().subtract(snappedGridPos));
        // }

        final Stream<Target> targets = Arrays.stream(getTargets())
            .filter(ta -> ta.getPos().distance(getTankPos()) <= getTankShotRange() &&
                !areVecsEqual(ta.getPos(), getTankPos()))
            .sorted(this::compareTargets);
        final Function<Target, Boolean> processor = t -> {
            queueCmd("shoot", t.getPos().subtract(getTankPos()));
            return true;
        };
        // If nobody else exists, we can queue up more shots because we can guarantee they will hit.
        // Because of the way the tanks work, batching shots will allow us to shoot slightly faster,
        // which lets us score more points.
        final boolean shotResults = getOther() == null ?
            targets.map(processor).count() > 0 :
            targets.findFirst().map(processor).orElse(false);
        if (shotResults) {
            return true;
        }
        // else {
        //     final Vec2 dest = calculateCentroid(
        //         getTargets()[0].getPos(),
        //         getTargets()[1].getPos(),
        //         getTargets()[2].getPos()
        //         );
            
        //     if (Arrays.stream(getTargets()).map(t -> t.getPos().distance(dest)).filter(d -> d <= getTankShotRange() * 1.5).count() > 1) {
        //         System.out.println(dest.x + ", " + dest.y);
        //         return queueCmd("move", dest.subtract(getTankPos()));
        //     }
        //     else {
        //         return queueCmd("move", dest);
        //     }
        // }

        // if the other tank is standing still and within range, take some potshots
        if (getOther() != null && potCount < POT_MAX && getOther().getPos().distance(getTankPos()) <= getTankShotRange() && areVecsEqual(getOther().getVel(), Vec2.zero())) {
            potCount++;
            final Vec2 arg = getOther().getPos().subtract(getTankPos());
            if (!areVecsEqual(arg.unit(), getTankDir())) {
                return queueCmd("turn", arg.normalize());
            }
            return queueCmd("shoot", arg);
        }
        potCount = 0; // shooting the tank doesn't give a lot of points, so if we do it too much, we risk our opponent beating us

        final Vec2 centroid = calculateCentroid(getPowerUps()[0].getPos(), getPowerUps()[1].getPos(), getPowerUps()[2].getPos());
        final PowerUp[] toRank = Arrays.copyOf(getPowerUps(), getPowerUps().length + 1);
        toRank[toRank.length - 1] = new DummyPowerUp(centroid);
        return Arrays.stream(toRank)
            .sorted(this::comparePowerUps)
            .findFirst()
            .map(powerUp -> {
                final Vec2 dist = powerUp.getPos().subtract(getTankPos());

                return queueCmd("move", dist.subtract(dist.unit().multiply(0.25)));
            }).orElse(false);
    }

    private static Vec2 calculateIntersection(Vec2 a1, Vec2 a2, Vec2 b1, Vec2 b2) {
        /*
         * we are just solving these systems of equations:
         *  y = ((y1-y2)/(x1-x2))(x-x1) + y1
         *  y = ((y3-y4)/(x3-x4))(x-x3) + y3
         */

        final double x, y, ax1, ay1, ax2, ay2, bx1, by1, bx2, by2, slope1, slope2, intercept1, intercept2;

        ax1 = a1.x;
        ay1 = a1.y;
        ax2 = a2.x;
        ay2 = a2.y;
        bx1 = b1.x;
        by1 = b1.y;
        bx2 = b2.x;
        by2 = b2.y;

        slope1 = (ay1 - ay2) / (ax1 - ax2);
        slope2 = (by1 - by2) / (bx1 - bx2);
        intercept1 = ay1 - (slope1 * ax1);
        intercept2 = by1 - (slope2 * bx1);

        x = (intercept2 - intercept1) / (slope1 - slope2);
        y = slope1 * x + intercept1;

        return new Vec2(x, y);
    }

    private static Vec2 calculateMedian(Vec2 a, Vec2 b) {
        return new Vec2(
            (a.x + b.x) / 2,
            (a.y + b.y) / 2
        );
    }

    private static Vec2 calculateCentroid(Vec2 a, Vec2 b, Vec2 c) {
        final Vec2 ab, bc, ac; // medians of the lines
        // ab = calculateMedian(a, b);
        ac = calculateMedian(a, c);
        bc = calculateMedian(b, c);
        
        return calculateIntersection(a, bc, b, ac);
    }

    private int compareTargets(Target a, Target b) {
        // final int comparedDistances = compareDistances(a.getPos(), b.getPos());
        // if (comparedDistances == 0) {
            return Double.compare(
                Math.abs(a.getPos().subtract(getTankPos()).angle() - getTankAngle()),
                Math.abs(b.getPos().subtract(getTankPos()).angle() - getTankAngle())
            );
        // }
        // return comparedDistances;
    }

    private int calculatePotentialPoints(PowerUp powerUp) {
        final Vec2 pos = powerUp.getPos();
        final double range = powerUp.getType().equals("R") ?
            getTankShotRange() * (1 + ((double)Game.POINTS_POWERUP_RANGE / 100)) :
            getTankShotRange();

        int count = 0;
        for (Target t : getTargets()) {
            if (t.getPos().distance(pos) <= range) {
                count++;
            }
        }

        final int points = count * Game.POINTS_HIT_TARGET;
        switch (powerUp.getType()) {
            case "P":
                return points + Game.POINTS_POWERUP_PTS;
            case "S":
                return points + (int)(getTankMoveSpeed() + 0.9);
        
            default:
                return points;
        }
    }

    private int comparePowerUpPointValues(PowerUp a, PowerUp b) {
        return -Integer.compare(
            calculatePotentialPoints(a),
            calculatePotentialPoints(b)
        );
    }

    private int comparePowerUps(PowerUp a, PowerUp b) {
        final int comparedDistances = comparePowerUpPointValues(a, b);
        if (comparedDistances == 0) {
            return compareDistances(a.getPos(), b.getPos());
        }
        return comparedDistances;
    }
    private int compareDistances(Vec2 a, Vec2 b) {
        return Double.compare(
            a.distanceSqr(getTankPos()),
            b.distanceSqr(getTankPos())
        );
    }

    private static boolean areVecsEqual(Vec2 one, Vec2 other) {
        return one.distanceSqr(other) <= 0.001;
    }

    @Override
    public boolean queueCmd(String cmdStr, Vec2 param) {
        if (param.distance(Vec2.zero()) <= 0.1) {
            return false;
        }
        final boolean result;
        if (cmdStr.equals("move") && param.angle() % 90 != 0) {
            if (Math.abs(getTankAngle() - (param.x < 0 ? 180 : 0)) < Math.abs(getTankAngle() - (90 * Math.signum(param.y)))) {
                result = queueCmd("move", Vec2.right().multiply(param.x)) &
                    queueCmd("move", Vec2.up().multiply(param.y));
            }
            else {
                result = queueCmd("move", Vec2.up().multiply(param.y)) &
                    queueCmd("move", Vec2.right().multiply(param.x));
            }
        }
        else {
            result = super.queueCmd(cmdStr, param);
        }

        // if (getOther() != null &&
        // areVecsEqual(getOther().getPos().subtract(getTankPos()).normalize(), getTankDir()) &&
        // !cmdStr.equals("shoot")) {
        //     return super.queueCmd("shoot", getTankDir());
        // }
        return result;
    }
}