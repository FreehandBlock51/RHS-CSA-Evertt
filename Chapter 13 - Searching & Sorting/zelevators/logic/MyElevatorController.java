package logic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

import game.Elevator;
import game.ElevatorController;
import game.Game;
import game.Simulation;

public class MyElevatorController implements ElevatorController {
    // Private member data
    private Game game;

    // Students should implement this function to return their name
    public String getStudentName() {
        return "Dalton Carter";
    }
    public int getStudentPeriod() {
        return 1;
    }

    private static final class ElevatorRequest {
        public final Direction direction;
        public final int floor;
        public ElevatorRequest(int floor, Direction direction) {
            this.floor = floor;
            this.direction = direction;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null || !(other instanceof ElevatorRequest)) {
                return false;
            }
            ElevatorRequest req = (ElevatorRequest)other;
            return req.floor == floor && Objects.equals(req.direction, direction);
        }

        @Override
        public int hashCode() {
            return Objects.hash(floor, direction);
        }
    }

    private final class ElevatorStatus {
        private final int elevatorIdx;
        private int targetFloor;
        private double waitRemaining;

        public ElevatorStatus(int elevatorIdx) {
            this.elevatorIdx = elevatorIdx;
            targetFloor = -1;
            waitRemaining = 0;
        }

        public int getTargetFloor() {
            return targetFloor;
        }
        public void setTargetFloor(int newTarget) {
            targetFloor = newTarget;
            if (hasOutsideRequestForFloor(newTarget)) {
                waitRemaining = ELEVATOR_LOADING_WAIT_TIME * (1 + elevatorIdx);
            }
            else {
                waitRemaining = ELEVATOR_UNLOADING_WAIT_TIME;
            }
        }
        public boolean hasArrived() {
            if (targetFloor < 0 || targetFloor >= game.getFloorCount()) {
                return true;
            }
            return game.isElevatorIsOnFloor(elevatorIdx, targetFloor);
        }
        public boolean isWaiting() {
            return waitRemaining > 0; 
        }
        public void decreaseRemainingWaitTime(double timePassed) {
            waitRemaining -= timePassed;
        }
    }

    private final ArrayDeque<ElevatorRequest> globalFloorRequestQueue = new ArrayDeque<>();
    private final ArrayList<ArrayDeque<Integer>> elevatorFloorRequestQueues = new ArrayList<>();
    private ElevatorStatus[] elevatorStatuses;
    private static final double ELEVATOR_LOADING_WAIT_TIME = 5;
    private static final double ELEVATOR_UNLOADING_WAIT_TIME = 1; // we don't have to wait as long to unload

    
    // Event: Game has started
    public void onGameStarted(Game game) {
        this.game = game;

        elevatorStatuses = new ElevatorStatus[game.getElevatorCount()];

        // initialize reference type collections
        for (int i = 0; i < game.getElevatorCount(); i++) {
            elevatorStatuses[i] = new ElevatorStatus(i);
            elevatorFloorRequestQueues.add(new ArrayDeque<>());
        }
    }

    // Event: "outside-the-elevator" request, requesting an elevator.
    //  The event will be triggered with the request is created/enabled & when it is cleared (reqEnable indicates which).
    public void onElevatorRequestChanged(int floorIdx, Direction dir, boolean reqEnable) {
        System.out.println("onElevatorRequestChanged(" + floorIdx + ", " + dir + ", " + reqEnable + ")");

        if (reqEnable) {
            globalFloorRequestQueue.offer(new ElevatorRequest(floorIdx, dir));
        }
        else {
            globalFloorRequestQueue.remove(new ElevatorRequest(floorIdx, dir));
        }
    }

    // Event: "inside-the-elevator" request, requesting to go to a floor.
    //  The event will be triggered with the request is created/enabled & when it is cleared (reqEnable indicates which).
    public void onFloorRequestChanged(int elevatorIdx, int floorIdx, boolean reqEnable) {
        System.out.println("onFloorRequestChanged(" + elevatorIdx + ", " + floorIdx + ", " + reqEnable + ")");

        final ArrayDeque<Integer> elevatorQueue = elevatorFloorRequestQueues.get(elevatorIdx);

        if (reqEnable) {
            elevatorQueue.offer(floorIdx);
        }
        else {
            elevatorQueue.remove(floorIdx);
        }
    }

    // Event: Elevator has arrived at the floor & doors are open.
    public void onElevatorArrivedAtFloor(int elevatorIdx, int floorIdx, Direction travelDirection) {
        System.out.println("onElevatorArrivedAtFloor(" + elevatorIdx + ", " + floorIdx + ", " + travelDirection + ")");
    }

    public boolean hasOutsideRequestForFloor(int floorIdx) {
        return game.hasElevatorRequestDown(floorIdx) ||
         game.hasElevatorRequestUp(floorIdx);
    }

    // Event: Called each frame of the simulation (i.e. called continuously)
    public void onUpdate(double deltaTime) {
        if (game == null) {
            return;
        }

        // System.out.println("update()");

        for (int elev = 0; elev < game.getElevatorCount(); elev++) {
            final ElevatorStatus elevator = elevatorStatuses[elev];
            if (!elevator.hasArrived()) {
                continue;
            }
            else if (elevator.isWaiting()) { // wait for the zombies to actually enter the elevator
                elevator.decreaseRemainingWaitTime(deltaTime);
                // System.out.println("\televator " + elev + ": " + elevatorWaitTimes[elev] + " seconds remaining until door close");
                continue;
            }

            ArrayDeque<Integer> eQ = elevatorFloorRequestQueues.get(elev);
            eQ.removeIf(i -> i.intValue() == elevator.getTargetFloor());
            globalFloorRequestQueue.removeIf(r -> r.floor == elevator.getTargetFloor());

            if (!eQ.isEmpty()) {
                gotoNextInIndividualQueue(elev);
            }
            else if (!globalFloorRequestQueue.isEmpty()) {
                gotoNextInGlobalQueue(elev);
            }
            else {
                gotoFloor(elev, game.getFloorCount() / 2);
            }
        }
    }

    @Override
    public boolean gotoFloor(int elevatorIdx, int floorIdx) {
        if (game.isElevatorIsHeadingToFloor(elevatorIdx, floorIdx) ||
        game.isElevatorIsOnFloor(elevatorIdx, floorIdx)) {
            return true;
        }
        elevatorStatuses[elevatorIdx].setTargetFloor(floorIdx);
        return ElevatorController.super.gotoFloor(elevatorIdx, floorIdx);
    }

    private boolean gotoNextInGlobalQueue(int elevatorIdx) {
        return gotoFloor(elevatorIdx, globalFloorRequestQueue.pop().floor);
    }
    private boolean gotoNextInIndividualQueue(int elevatorIdx) {
        return gotoFloor(elevatorIdx, elevatorFloorRequestQueues.get(elevatorIdx).pop());
    }
}