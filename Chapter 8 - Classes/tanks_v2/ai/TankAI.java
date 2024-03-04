package ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

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

        if (
            Arrays.stream(getTargets())
                .filter(ta -> ta.getPos().distance(getTankPos()) <= getTankShotRange())
                .sorted((t1, t2) -> {
                    if (t1.getPos().distanceSqr(getTankPos()) < t2.getPos().distanceSqr(getTankPos())) {
                        return -1;
                    }
                    else if (t1.getPos().distanceSqr(getTankPos()) > t2.getPos().distanceSqr(getTankPos())) {
                        return 1;
                    }
                    else {
                        return 0;
                    }
                })
                .findFirst()
                .map(t -> {
                    queueCmd("shoot", t.getPos().subtract(getTankPos()));
                    return true;
                })
                .orElse(false)
        ) {
            return true;
        }

        Arrays.stream(getPowerUps())
            .sorted((p1, p2) -> {
                if ((p1.getType().equals("p") && !p2.getType().equals("p")) || 
                p1.getPos().distanceSqr(getTankPos()) < p2.getPos().distanceSqr(getTankPos())) {
                    return -1;
                }
                else if ((!p1.getType().equals("p") && p2.getType().equals("p")) || 
                p1.getPos().distanceSqr(getTankPos()) > p2.getPos().distanceSqr(getTankPos())) {
                    return 1;
                }
                else {
                    return 0;
                }
            })
            .findFirst()
            .ifPresent(powerUp -> {
                final Vec2 dist = powerUp.getPos().subtract(getTankPos());

                if (Math.abs(getTankDir().x) < Math.abs(getTankDir().y)) {
                    queueCmd("move", Vec2.right().multiply(dist.x));
                    queueCmd("move", Vec2.up().multiply(dist.y));
                }
                else {
                    queueCmd("move", Vec2.up().multiply(dist.y));
                    queueCmd("move", Vec2.right().multiply(dist.x));
                }
            });

        return true;
    }

    @Override
    public boolean queueCmd(String cmdStr, Vec2 param) {
        if (param.equals(Vec2.zero())) {
            return false;
        }
        return super.queueCmd(cmdStr, param);
    }
}