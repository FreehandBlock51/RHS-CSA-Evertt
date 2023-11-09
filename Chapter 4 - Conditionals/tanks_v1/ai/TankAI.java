package ai;

import java.util.ArrayList;
import java.util.Collection;

import game.Game;
import game.PowerUp;
import game.TankAIBase;
import game.Vec2;
import javafx.util.Pair;

public final class TankAI extends TankAIBase {
    private static final double EPSILON = 0.1;
    private static boolean areVec2sEqual(Vec2 one, Vec2 other) {
        Vec2 difference = one.subtract(other);
        return Math.abs(difference.x) < EPSILON && Math.abs(difference.y) < EPSILON;
    }

    private static final String MOVECMD_TYPE = "move";
    private static final String TURNCMD_TYPE = "turn";
    private static final String FIRECMD_TYPE = "shoot";

    private static final String POINTS_POWERUP_TYPE = "P";
    private static final String RANGE_POWERUP_TYPE = "R";
    private static final String SPEED_POWERUP_TYPE = "S";

    private int getPointsPowerupBenifit() {
        return Game.POINTS_POWERUP_PTS;
    }
    private int getRangePowerupBenifit() {
        return Game.POINTS_POWERUP_RANGE;
    }
    private static final double DESIRED_SPEED = 3;
    private int getSpeedPowerupBenifit() {
        return getTankMoveSpeed() < DESIRED_SPEED ? Game.POINTS_POWERUP_SPEED : Game.POINTS_POWERUP_SPEED / 2;
    }

    public String getPlayerName() {
        return "Dalton";  // <---- Put your first name here
    }
    public int getPlayerPeriod() {
        return 1;
    }

    public final int playerIdx;

    public TankAI() {
        playerIdx = 0; // TODO actually set playerIdx
    }
        
    // You are free to add member variables & methods to this class (and delete this comment).
    //  You should use the methods in its base class (TankAIBase) to query the world. 
    //  Note that you are not allowed to reach into game code directly or make any
    //  modifications to code in the game package. Use your judgement and ask your 
    //  teacher if you are not sure. If it feels like cheating, it probably is.

    public boolean updateAI() {
        if (getTank().hasCommand()) {
            return false; // don't calculate movement if we are still moving
        }
        
        if (getOther() != null && getOther().getPos().distance(getTankPos()) < getTankShotRange()) {
            tryQueueCmd(FIRECMD_TYPE, getOther().getPos().subtract(getTankPos()));
        }
        
        Pair<PowerUp, Integer> cheapestPowerUp = new Pair<>(null, Integer.MIN_VALUE);

        for (PowerUp powerUp : getPowerUps()) {
            int benifit; // how good is the powerup?
            switch (powerUp.getType()) {
                case POINTS_POWERUP_TYPE:
                    benifit = getPointsPowerupBenifit();
                    break;
                case SPEED_POWERUP_TYPE:
                    benifit = getSpeedPowerupBenifit();
                    break;
                case RANGE_POWERUP_TYPE:
                    benifit = getRangePowerupBenifit();
                    break;
            
                default:
                    benifit = 0;
                    break;
            }
            Vec2 distanceToPowerUp = powerUp.getPos().subtract(tank.getPos());
            benifit -= calculateDistanceCost(distanceToPowerUp);
            if (benifit > cheapestPowerUp.getValue()) {
                cheapestPowerUp = new Pair<PowerUp,Integer>(powerUp, benifit);
            }
        }

        if (cheapestPowerUp.getKey() == null) {
            return false;
        }

        Vec2 distanceToMove = cheapestPowerUp.getKey().getPos().subtract(getTankPos());

        ArrayList<Pair<String, Vec2>> cmds = new ArrayList<>();
        cmds.add(new Pair<>(MOVECMD_TYPE, Vec2.right().multiply(distanceToMove.x)));
        cmds.add(new Pair<>(MOVECMD_TYPE, Vec2.up().multiply(distanceToMove.y)));

        return tryBatchQueue(true, cmds);
    }

    double calculateDistanceCost(Vec2 destination) {
        return Math.abs(destination.x * tank.getMoveSpeed()) + Math.abs(destination.y * getTankMoveSpeed());
    }

    public boolean tryQueueCmd(String type, Vec2 param) {
        switch (type) {
            case MOVECMD_TYPE:
                if (areVec2sEqual(param, Vec2.zero())) {
                    return true; // no movement necessary
                }
                if (param.x != 0 && param.y != 0) {
                    return tryQueueCmd(MOVECMD_TYPE, new Vec2(param.x, 0)) ||
                           tryQueueCmd(MOVECMD_TYPE, new Vec2(0, param.y));
                }
                break;
            case TURNCMD_TYPE:
                if (areVec2sEqual(param, tank.forward())) {
                    return true; // did not turn
                }
                break;
            case FIRECMD_TYPE:
                if (areVec2sEqual(param, Vec2.zero())) {
                    return false; // cannot shoot self
                }
                break;
        
            default:
                return false;
        }

        return queueCmd(type, param);
    }

    public boolean tryBatchQueue(boolean forceQueueAll, Collection<Pair<String, Vec2>> cmds) {
        boolean ret = false;

        for (Pair<String,Vec2> cmd : cmds) {
            if (cmd == null) { continue; }
            ret |= tryQueueCmd(cmd.getKey(), cmd.getValue());
            if (!ret && !forceQueueAll) {
                return false;
            }
        }

        return ret;
    }
}