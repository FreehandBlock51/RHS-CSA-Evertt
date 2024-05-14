package logic;

import java.util.ArrayDeque;
import java.util.ArrayList;
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

    private final ArrayDeque<ElevatorRequest> globalFloorRequestQueue = new ArrayDeque<>();
    private final ArrayList<ArrayDeque<Integer>> elevatorFloorRequestQueues = new ArrayList<>();
    private int[] elevatorTargetFloors;
    private double[] elevatorWaitTimes;
    private boolean[] floorRequestStatuses;
    private int[] elevatorStressLevels;
    private static final int MAX_STRESS = 5;
    private static final double ELEVATOR_LOADING_WAIT_TIME = 5;
    private static final double ELEVATOR_UNLOADING_WAIT_TIME = 3; // we don't have to wait as long to unload

    
    // Event: Game has started
    public void onGameStarted(Game game) {
        this.game = game;

        elevatorTargetFloors = new int[game.getElevatorCount()];
        elevatorWaitTimes = new double[game.getElevatorCount()];
        elevatorStressLevels = new int[game.getElevatorCount()];
        floorRequestStatuses = new boolean[game.getFloorCount()];

        // initialize elevator queues
        for (int i = 0; i < game.getElevatorCount(); i++) {
            elevatorFloorRequestQueues.add(new ArrayDeque<>());
        }
    }

    // Event: "outside-the-elevator" request, requesting an elevator.
    //  The event will be triggered with the request is created/enabled & when it is cleared (reqEnable indicates which).
    public void onElevatorRequestChanged(int floorIdx, Direction dir, boolean reqEnable) {
        System.out.println("onElevatorRequestChanged(" + floorIdx + ", " + dir + ", " + reqEnable + ")");

        floorRequestStatuses[floorIdx] |= reqEnable;
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

        // elevatorFloorRequestQueues.get(elevatorIdx).removeIf(i -> i.intValue() == floorIdx);
        // globalFloorRequestQueue.removeIf(i -> i.equals(new ElevatorRequest(floorIdx, travelDirection)));

        if (hasOutsideRequestForFloor(floorIdx)) {
            System.out.println("   requests at floor " + floorIdx);
            elevatorWaitTimes[elevatorIdx] = ELEVATOR_LOADING_WAIT_TIME * (1 + elevatorIdx); // time it takes to travel to the elevator
            floorRequestStatuses[floorIdx] = false;
        }
        else {
            System.out.println("no requests at floor " + floorIdx);
            elevatorWaitTimes[elevatorIdx] = ELEVATOR_UNLOADING_WAIT_TIME;
        }
    }

    public boolean hasOutsideRequestForFloor(int floorIdx) {
        return floorRequestStatuses[floorIdx] ||
         game.hasElevatorRequestDown(floorIdx) ||
         game.hasElevatorRequestUp(floorIdx) ||
         globalFloorRequestQueue.stream().anyMatch(r -> r.floor == floorIdx);
    }

    // Event: Called each frame of the simulation (i.e. called continuously)
    public void onUpdate(double deltaTime) {
        if (game == null) {
            return;
        }

        // System.out.println("update()");

        for (int elev = 0; elev < game.getElevatorCount(); elev++) {
            if (!game.isElevatorIsOnFloor(elev, elevatorTargetFloors[elev])) {
                continue;
            }
            else if (elevatorWaitTimes[elev] > 0) { // wait for the zombies to actually enter the elevator
                elevatorWaitTimes[elev] -= deltaTime;
                // System.out.println("\televator " + elev + ": " + elevatorWaitTimes[elev] + " seconds remaining until door close");
                continue;
            }

            ArrayDeque<Integer> eQ = elevatorFloorRequestQueues.get(elev);

            final int curElev = elev; // for the predicates
            eQ.removeIf(i -> i.intValue() == elevatorTargetFloors[curElev]);
            globalFloorRequestQueue.removeIf(r -> r.floor == elevatorTargetFloors[curElev]);

            // if (eQ.isEmpty()) {
            //     elevatorStressLevels[elev] = 0;
            // }
            // else if (eQ.size() > MAX_STRESS) {
            //     elevatorStressLevels[elev] = MAX_STRESS + 1;
            // }

            // if (!eQ.isEmpty() && !globalFloorRequestQueue.isEmpty()) {
            //     final int primaryTarget = eQ.peek();
            //     ElevatorRequest secondaryTarget = globalFloorRequestQueue.peek();

            //     if (elevatorStressLevels[elev] > MAX_STRESS) {
            //         gotoNextInIndividualQueue(elev);
            //     }
            //     else if ((secondaryTarget.floor <= game.getElevatorFloor(elev) && primaryTarget <= secondaryTarget.floor) ||
            //     (secondaryTarget.floor >= game.getElevatorFloor(elev) && primaryTarget >= secondaryTarget.floor)) {
            //         elevatorStressLevels[elev]++;
            //         gotoNextInGlobalQueue(elev);
            //     }
            //     else {
            //         gotoNextInIndividualQueue(elev);
            //     }
            // }
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
        elevatorTargetFloors[elevatorIdx] = floorIdx;
        return ElevatorController.super.gotoFloor(elevatorIdx, floorIdx);
    }

    private boolean gotoNextInGlobalQueue(int elevatorIdx) {
        return gotoFloor(elevatorIdx, globalFloorRequestQueue.pop().floor);
    }
    private boolean gotoNextInIndividualQueue(int elevatorIdx) {
        return gotoFloor(elevatorIdx, elevatorFloorRequestQueues.get(elevatorIdx).pop());
    }
}