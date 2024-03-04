package ai;

import game.PowerUp;
import game.Tank;
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
        if (tank.hasCommand()) {
            return false;
        }

        Target t = null;

        for (Target target : getTargets()) {
            if (target.getPos().distance(getTankPos()) > getTankShotRange()) {
                continue;
            }
            if (t == null || t.getPos().distance(getTankPos()) > target.getPos().distance(getTankPos())) {
                t = target;
            }
        }
        if (t != null) {
            queueCmd("shoot", t.getPos().subtract(getTankPos()));
            return true;
        }

        PowerUp powerUp = getPowerUp();

        for (PowerUp p : getPowerUps()) {
            if (powerUp.getType().equals("p") && !p.getType().equals("p")) {
                continue;
            }
            if (p.getType().equals("p") || p.getPos().distance(getTankPos()) < powerUp.getPos().distance(getTankPos())) {
                powerUp = p;
            }
        }

        final Vec2 dist = powerUp.getPos().subtract(getTankPos());

        queueCmd("move", Vec2.right().multiply(dist.x));
        queueCmd("move", Vec2.up().multiply(dist.y));

        return true;
    }
}