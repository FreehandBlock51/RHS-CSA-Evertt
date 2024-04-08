package ai;


import java.util.Arrays;
import java.util.Dictionary;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.stream.Stream;

import game.Game;
import game.GameObject;
import game.PowerUp;
import game.TankAIBase;
import game.Target;
import game.Util;
import game.Vec2;
import game.World;


@interface wdqAFESRHDXTCJFYKUMNBRAFSEHTDFYUKMJNTRGAEFWGSRHXFGCMHFNbgfnhmdnsrtdjy {}

@wdqAFESRHDXTCJFYKUMNBRAFSEHTDFYUKMJNTRGAEFWGSRHXFGCMHFNbgfnhmdnsrtdjy
public class TankAI extends TankAIBase {
    private static final boolean w = TankAI.class.getAnnotation(wdqAFESRHDXTCJFYKUMNBRAFSEHTDFYUKMJNTRGAEFWGSRHXFGCMHFNbgfnhmdnsrtdjy.class) != null;

    private static void log(String msg) {
        if (w) {
            Util.log(msg);
        }
    }

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
        log("updating AI for tank at " + getTankPos() + "...");

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
            .filter(TankAI::isGameObjectInBounds)
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
                log("Opportunistically looking to shoot opponent at " + getOther().getPos());
                return queueCmd("turn", arg.normalize());
            }
            log("Taking a potshot at opponent at " + getOther().getPos());
            return queueCmd("shoot", arg) &&
                (getOther().getAngVel() > Vec2.distance(getTankPos(), getOther().getPos()) / getTankShotSpeed() ? queueCmd("shoot", arg) : true);
        }
        potCount = 0; // shooting the tank doesn't give a lot of points, so if we do it too much, we risk our opponent beating us

        final Vec2 centroid = calculateCentroid(getPowerUps()[0].getPos(), getPowerUps()[1].getPos(), getPowerUps()[2].getPos());
        final PowerUp[] unrankedPowerUps = Arrays.copyOf(getPowerUps(), getPowerUps().length + 1);
        unrankedPowerUps[unrankedPowerUps.length - 1] = new DummyPowerUp(centroid);
        return Arrays.stream(unrankedPowerUps)
            .sorted(this::comparePowerUps)
            .findFirst()
            .map(powerUp -> {
                log("Moving to powerup at " + powerUp.getPos());

                final Vec2 dist = powerUp.getPos().subtract(getTankPos());
                
                if (dist.x < 1) { dist.x = 0; }
                if (dist.y < 1) { dist.y = 0; }

                return queueCmd("move", dist);
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
                return (int)(points * (1 + Game.POINTS_POWERUP_SPEED / 100));
        
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

    private boolean canOtherTankGetToPositionFirst(Vec2 position) {
        return Vec2.copy(position).distance(getTankPos()) / getTankMoveSpeed() <= Vec2.copy(position).distance(getOther().getPos()) / getOther().getMoveSpeed();
    }

    private int comparePowerUps(PowerUp a, PowerUp b) {
        final boolean aWillBeTakenAway = canOtherTankGetToPositionFirst(a.getPos());
        final boolean bWillBeTakenAway = canOtherTankGetToPositionFirst(b.getPos());
        if (aWillBeTakenAway && !bWillBeTakenAway) {
            return -1;
        }
        else if (!aWillBeTakenAway && bWillBeTakenAway) {
            return 1;
        }

        final int comparedPointValues = comparePowerUpPointValues(a, b);
        if (comparedPointValues == 0) {
            if (aWillBeTakenAway && bWillBeTakenAway) {
                return -compareDistances(a.getPos(), b.getPos(), getOther().getPos());
            }
            return compareDistances(a.getPos(), b.getPos(), getTankPos());
        }
        return comparedPointValues;
    }
    private int compareDistances(Vec2 a, Vec2 b, Vec2 distanceTo) {
        return Double.compare(
            a.distanceSqr(distanceTo),
            b.distanceSqr(distanceTo)
        );
    }

    private static boolean areVecsEqual(Vec2 one, Vec2 other) {
        return one.distanceSqr(other) <= 0.001;
    }

    private boolean moveVertically(double y) {
        return queueCmd("move", new Vec2(0, y));
    }
    private boolean moveHorizontally(double x) {
        return queueCmd("move", new Vec2(x, 0));
    }

    private static final Vec2 minBounds = new Vec2(0.2, 0.2);
    private static final Vec2 maxBounds = Vec2.subtract(Util.maxCoordFrameUnits(), new Vec2(0.2, 0.2));

    private static boolean isGameObjectInBounds(GameObject go) {
        return isVecInBounds(go.getPos());
    }
    private static boolean isVecInBounds(Vec2 vec) {
        return areVecsEqual(vec, Vec2.clamp(vec, minBounds, maxBounds));
    }

    private final class Cmd {
        public final String cmdStr;
        public final Vec2 param;
        public Cmd(String cmdStr, Vec2 param) {
            this.cmdStr = cmdStr;
            this.param = param;
        }
        public boolean queue() {
            return queue(false);
        }
        public boolean queue(boolean bypassQueue) {
            return queueCmd(cmdStr, param, true);
        }
    }

    private final Queue<Cmd> cmdQueue = new ConcurrentLinkedQueue<>();

    @Override
    public boolean queueCmd(String cmdStr, Vec2 param) {
        return queueCmd(cmdStr, param, false);
    }
    private boolean queueCmd(String cmdStr, Vec2 param, boolean bypassQueue) {
        cmdStr = cmdStr.intern(); // because why not?

        // potshots!
        if (Vec2.distance(getTankPos(), getOther().getPos()) < 1.5 &&
        Vec2.dot(getTankDir().unit(), getTankPos().subtract(getOther().getPos()).unit()) > 0.999) {
            return super.queueCmd("shoot", getTankDir().multiply(1.5));
        }
        else if (!bypassQueue && !cmdStr.equals("shoot") && !cmdQueue.isEmpty()) {
            Cmd cmd;
            for (cmd = null; cmd == null; cmd = cmdQueue.remove());
            return cmd.queue(true);
        }

        if (param.lengthSqr() <= 0.01) {
            log("discarding command with zero-length parameter");
            return false;
        }
        
        switch (cmdStr.toLowerCase()) {
            case "move":
                {
                    final Vec2 destination = Vec2.add(getTankPos(), param);
                    if ((Math.abs(param.x) > 0.000001) && (Math.abs(param.y) > 0.000001)) { // Tanks move only horizontally & vertically
                        log("Adjusting diagonal move");
                        final double verticalAngle = 90 * Math.signum(param.y);
                        final double horizontalAngle = 90 + (-90 * Math.signum(param.x));
                        final double ourAngle = getTankAngle();
                        // Try to move in the direction we need to turn the least to get to, but if
                        // that direction has zero distance, move in the other direction
                        if (Math.abs(ourAngle - verticalAngle) < Math.abs(ourAngle - horizontalAngle)) {
                            if (moveVertically(param.y)) {
                                return cmdQueue.add(new Cmd("move", new Vec2(param.x, 0)));
                            }
                            return moveHorizontally(param.x);
                        }
                        else {
                            if (moveHorizontally(param.y)) {
                                return cmdQueue.add(new Cmd("move", new Vec2(0, param.y)));
                            }
                            return moveVertically(param.x);
                        }
                    }
                    else if (Vec2.lengthSqr(param) < 0.001) { // Zero distance
                        log("discarding zero-distance movement");
                        return false;
                    }
                    else if (!isVecInBounds(destination)) { // out of bounds
                        log("adjusting out-of-bounds move");
                        return queueCmd(cmdStr, Vec2.clamp(param, minBounds, maxBounds));
                    }
                }
                break;
            case "turn":
                {
                    if (Vec2.lengthSqr(param) < 0.001) { // Zero direction vector
                        log("discarding zero-direction rotation");
                        return false;
                    }
                }
                break;
            case "shoot":
                {
                    if (Vec2.lengthSqr(param) < 0.001) { // Zero direction vector
                        log("discarding zero-distance shot");
                        return false;
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Unrecognized command!");
        }

        return super.queueCmd(cmdStr, param);
    }
}