package ai;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import game.Game;
import game.PowerUp;
import game.TankAIBase;
import game.Target;
import game.Vec2;

public class TankAI extends TankAIBase {

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

    public boolean updateAI() {
        // if (tank.hasCommand()) {
        //     return false;
        // }

        if (getOther() != null && getOther().getPos().distance(getTankPos()) <= getTankShotRange() && areVecsEqual(getOther().getVel(), Vec2.zero())) {
            final Vec2 arg = getOther().getPos().subtract(getTankPos());
            if (!areVecsEqual(arg.unit(), getTankDir())) {
                return queueCmd("turn", arg.normalize());
            }
            return queueCmd("shoot", arg);
        }

        final Vec2 snappedGridPos = new Vec2( // snap tank to grid so shots work correctly
            Math.floor(getTankPos().x) + 0.5f,
            Math.floor(getTankPos().y) + 0.5f
            );
        if (!areVecsEqual(getTankPos(), snappedGridPos)) {
            return queueCmd("move", getTankPos().subtract(snappedGridPos));
        }

        final Stream<Target> targets = Arrays.stream(getTargets())
            .filter(ta -> ta.getPos().distance(getTankPos()) <= getTankShotRange())
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
        //     final Vec2 dest = calculateMiddle(
        //         getTargets()[0].getPos(),
        //         getTargets()[1].getPos(),
        //         getTargets()[2].getPos()
        //         );
            
        //     if (Arrays.stream(getTargets()).map(t -> t.getPos().distance(dest)).filter(d -> d <= getTankShotRange() * 1.5).count() > 1) {
        //         System.out.println(dest.x + ", " + dest.y);
        //         return queueCmd("move", dest.subtract(getTankPos()));
        //     }
        // }

        return Arrays.stream(getPowerUps())
            .sorted(this::comparePowerUps)
            .findFirst()
            .map(powerUp -> {
                final Vec2 dist = powerUp.getPos().subtract(getTankPos());

                return queueCmd("move", dist);
            }).orElse(false);
    }

    private static Vec2 calculateMiddle(Vec2 a, Vec2 b, Vec2 c) {
        final Vec2 mid1 = a.add(b).divide(2);
        final Vec2 mid2 = a.add(c).divide(2);
        final double slope1 = (a.y - mid1.y) / (a.x - mid1.x);
        final double slope2 = (a.y - mid2.y) / (a.x - mid2.x);
        final double x = (slope1 * mid1.x - slope2 * mid2.x + mid1.y - mid2.y) / (slope1 - slope2);
        final double y = (mid2.y / slope2 - mid1.y / slope1 + mid1.x - mid2.x) / (1 / slope2 - 1 / slope1);
        return new Vec2(x, y);
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
        if (powerUp.getType().equals("P")) {
            return points + Game.POINTS_POWERUP_PTS;
        }
        else {
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
        return one.distanceSqr(other) <= 0.0001;
    }

    @Override
    public boolean queueCmd(String cmdStr, Vec2 param) {
        if (areVecsEqual(param, Vec2.zero())) {
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