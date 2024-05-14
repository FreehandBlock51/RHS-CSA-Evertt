package ai;

import game.PowerUp;
import game.TankAIBase;
import game.Target;
import game.Vec2;
 
import java.util.*;
 
public class OtherAI extends TankAIBase {
    private ArrayList<Target> targets = new ArrayList<Target>();
    private ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();
    private Vec2 bestPowerUp = new Vec2(0, 0);
 
    public String getPlayerName() {
        return "Varun";  // <---- Put your first name here
    }
    public int getPlayerPeriod() {
        return 7;                // <---- Put your period here
    }
       
    // You are free to add member variables & methods to this class (and delete this comment).
    //  You should use the methods in its base class (TankAIBase) to query the world.
    //  Note that you are not allowed to reach into game code directly or make any
    //  modifications to code in the game package. Use your judgement and ask your
    //  teacher if you are not sure. If it feels like cheating, it probably is.
 
    public boolean updateAI() {
        shootTargets(getTankPos());
 
        bestPowerup();
 
        if(getTankPos().x != bestPowerUp.x){
            goToPowerUpX();
        }
        else if(getTankPos().y != bestPowerUp.y){
            goToPowerUpY();
        }
 
        return true;
    }
 
    /**
     *
     */
    private boolean sortTargets(){
        targets.clear();
        for(Target thing : getTargets()){
            targets.add(thing);
        }
 
        for(int i = 0; i < targets.size(); i++){
            if(targets.get(i).getPos().distance(getTankPos()) > getTankShotRange() || !isNearAngle(targets.get(i), getTankTurnSpeed())){
                targets.remove(i);
                i--;
            }
        }
 
        return targets.size() > 0;
    }
 
    private boolean isNearAngle(Target targ, double max){
        return Math.abs(Math.tan((getTankPos().y - targ.getPos().y)/(getTankPos().x - targ.getPos().x))) - Math.abs(getTankAngle()) < max;
    }
 
    private void shootTargets(Vec2 tankPos){
        if(sortTargets()){
            for(Target thing : targets){
                queueCmd("shoot", thing.getPos().minus(tankPos));
            }
        }
    }
 
    private void goToPowerUpX(){
        queueCmd("move", new Vec2((bestPowerUp.x) - getTankPos().x, 0));
    }
 
    private void goToPowerUpY(){
        queueCmd("move", new Vec2(0, (bestPowerUp.y) - getTankPos().y));
    }
 
    private void bestPowerup(){
        powerUps.clear();
        for(PowerUp thing : getPowerUps()){
            powerUps.add(thing);
        }
 
        PowerUp best = powerUps.get(0);
        for(int i = 1; i < powerUps.size(); i++){
            if(comparePowerUp(powerUps.get(i), best)){
                best = powerUps.get(i);
            }
        }
        bestPowerUp = best.getPos();
    }
 
    private boolean comparePowerUp(PowerUp p1, PowerUp p2){
        return ratePowerUp(p2) < ratePowerUp(p1);
    }
    private double ratePowerUp(PowerUp p){
        double posScore = 1/(Math.abs(p.getPos().x-getTankPos().x) + Math.abs((p.getPos().y-getTankPos().y)));
 
        double typeScore = 0;
        if(p.getType() == "S" || p.getType() == "R"){
            typeScore = 3;
        }
 
        typeScore *= (1/5);
       
        double targetScore = 0;
 
        for(Target thing : getTargets()){
            if(p.getPos().distance(thing.getPos()) < getTankShotRange()){
                targetScore++;
            }
        }
        targetScore *= (1/3);
 
        return posScore * 20 + typeScore * 2 + targetScore * 20;
    }
}