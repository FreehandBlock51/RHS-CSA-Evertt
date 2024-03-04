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
        // if (tank.hasCommand()) {
        //     return false;
        // }

        if (
            Arrays.stream(getTargets())
                .filter(ta -> ta.getPos().distance(getTankPos()) <= getTankShotRange())
                .sorted(this::compareTargets)
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
            .sorted(this::comparePowerUps)
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

    private int comparePowerUps(PowerUp a, PowerUp b) {
        final int comparedTypes = a.getType().compareTo(b.getType());
        if (comparedTypes == 0) {
            return compareDistances(a.getPos(), b.getPos());
        }
        return comparedTypes;
    }
    private int compareDistances(Vec2 a, Vec2 b) {
        return Double.compare(
            a.distanceSqr(getTankPos()),
            b.distanceSqr(getTankPos())
        );
    }

    @Override
    public boolean queueCmd(String cmdStr, Vec2 param) {
        if (param.equals(Vec2.zero())) {
            return false;
        }
        final boolean result = !super.queueCmd(cmdStr, param);
        if (getOther() != null &&
        getOther().getPos().subtract(getTankPos()).normalize().equals(getTankDir()) &&
        !cmdStr.equals("shoot")) {
            return super.queueCmd("shoot", getTankDir());
        }
        return result;
    }
}