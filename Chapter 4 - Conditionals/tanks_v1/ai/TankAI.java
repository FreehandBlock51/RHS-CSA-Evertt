package ai;

import game.Tank;
import game.TankAIBase;
import game.Vec2;

public class TankAI extends TankAIBase {

    static final String MOVECMD_TYPE = "move";
    static final String FIRECMD_TYPE = "shoot";

    public String getPlayerName() {
        return "Dalton Carter";  // <---- Put your first name here
    }
    public int getPlayerPeriod() {
        return -1;
    }
        
    // You are free to add member variables & methods to this class (and delete this comment).
    //  You should use the methods in its base class (TankAIBase) to query the world. 
    //  Note that you are not allowed to reach into game code directly or make any
    //  modifications to code in the game package. Use your judgement and ask your 
    //  teacher if you are not sure. If it feels like cheating, it probably is.

    public boolean updateAI() {
        Tank other = this.getOther();

        Vec2 myPos = this.tank.getPos();
        Vec2 otherPos = other.getPos();
        if (myPos.distance(otherPos) <= this.getTankShotRange()) { // if we are within shooting range, fire
            this.queueCmd(FIRECMD_TYPE, otherPos.subtract(myPos).normalize());
            return true;
        }

        // otherwise, move towards the other tank
        Vec2 movement = other.getPos().subtract(this.tank.getPos())
            .normalize()
            .multiply(this.getTankMoveSpeed());
        this.queueCmd(MOVECMD_TYPE, movement);

        return true;
    }
}