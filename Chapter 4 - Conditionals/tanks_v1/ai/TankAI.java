package ai;

import java.util.ArrayList;
import java.util.Collection;

import game.PowerUp;
import game.TankAIBase;
import game.Vec2;
import javafx.util.Pair;

public final class TankAI extends TankAIBase {
    static final double EPSILON = 0.1;
    static boolean areVec2sEqual(Vec2 one, Vec2 other) {
        Vec2 difference = one.subtract(other);
        return Math.abs(difference.x) < EPSILON && Math.abs(difference.y) < EPSILON;
    }

    static final String MOVECMD_TYPE = "move";
    static final String TURNCMD_TYPE = "turn";
    static final String FIRECMD_TYPE = "shoot";

    static final String POINTS_POWERUP_TYPE = "P";
    static final String RANGE_POWERUP_TYPE = "R";
    static final String SPEED_POWERUP_TYPE = "S";

    static final int POINTS_POWERUP_BENIFIT = 25;
    static final int RANGE_POWERUP_BENIFIT = 0;
    static final int SPEED_POWERUP_BENIFIT = 10;

    static final int QUEUECMD_POINTS_LOST = 1;

    public String getPlayerName() {
        return "Dalton Carter";  // <---- Put your first name here
    }
    public int getPlayerPeriod() {
        return 1;
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
                    benifit = POINTS_POWERUP_BENIFIT;
                    break;
                case SPEED_POWERUP_TYPE:
                    benifit = SPEED_POWERUP_BENIFIT;
                    break;
                case RANGE_POWERUP_TYPE:
                    benifit = RANGE_POWERUP_BENIFIT;
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