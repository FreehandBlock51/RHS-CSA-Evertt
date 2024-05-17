package logic;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

import game.ElevatorController;
import game.Game;
import game.Zombie;

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

        public int getElevatorIndex() {
            return elevatorIdx;
        }

        public int getTargetFloor() {
            return targetFloor;
        }
        public void setTargetFloor(int newTarget, boolean configureTravelDirection) {
            targetFloor = newTarget;
            double floorOffset = targetFloor - game.getElevatorFloor(elevatorIdx);
            if (!configureTravelDirection || floorOffset == 0) {
                game.setElevatorTravelDirection(elevatorIdx, Direction.None);
            }
            else if (floorOffset < 0) {
                game.setElevatorTravelDirection(elevatorIdx, Direction.Down);
            }
            else {
                game.setElevatorTravelDirection(elevatorIdx, Direction.Up);
            }
            reevaluateWaitTime();
        }
        public void reevaluateWaitTime() {
            if (hasArrived()) {
                return;
            }

            final boolean isLoading;
            if (elevatorStressStates[elevatorIdx] ||
             Arrays.stream(elevatorStatuses).anyMatch(s -> 
             s.elevatorIdx < elevatorIdx && s.targetFloor == targetFloor &&
             MyElevatorController.willElevatorAcceptPassenger(game.getElevatorTravelDirection(elevatorIdx), game.getElevatorTravelDirection(s.elevatorIdx)))) {
                isLoading = false;
            }
            else {
                switch (game.getElevatorTravelDirection(elevatorIdx)) {
                    case Up:
                        isLoading = game.hasElevatorRequestUp(targetFloor);
                        break;
                    case Down:
                        isLoading = game.hasElevatorRequestDown(targetFloor);
                        break;
                    default:
                        isLoading = hasOutsideRequestForFloor(targetFloor);
                        break;
                }
            }

            if (isLoading) {
                waitRemaining = calculateLoadingWaitTime(elevatorIdx);
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
    private final ArrayList<ArrayList<Integer>> elevatorFloorRequestQueues = new ArrayList<>();
    private ElevatorStatus[] elevatorStatuses;
    private boolean[] elevatorStressStates;
    private static final double ELEVATOR_LOADING_WAIT_TIME = 4.7;
    private static final double ELEVATOR_WALKING_WAIT_TIME = 3;
    private static final double ELEVATOR_UNLOADING_WAIT_TIME = 1.5; // we don't have to wait as long to unload
    private static final int ELEVATOR_STRESS_THRESHOLD = 5;

    
    public static double calculateLoadingWaitTime(int elevatorIdx) {
        return ELEVATOR_LOADING_WAIT_TIME + (elevatorIdx * ELEVATOR_WALKING_WAIT_TIME);
    }
    

    // Event: Game has started
    public void onGameStarted(Game game) {
        this.game = game;

        elevatorStatuses = new ElevatorStatus[game.getElevatorCount()];
        elevatorStressStates = new boolean[game.getElevatorCount()];

        // initialize reference type collections
        for (int i = 0; i < game.getElevatorCount(); i++) {
            elevatorStatuses[i] = new ElevatorStatus(i);
            elevatorFloorRequestQueues.add(new ArrayList<>());
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

        final ArrayList<Integer> elevatorQueue = elevatorFloorRequestQueues.get(elevatorIdx);

        if (reqEnable) {
            elevatorQueue.add(floorIdx);
        }
        else {
            elevatorQueue.remove(Integer.valueOf(floorIdx));
        }
    }

    // Event: Elevator has arrived at the floor & doors are open.
    public void onElevatorArrivedAtFloor(int elevatorIdx, int floorIdx, Direction travelDirection) {
        System.out.println("onElevatorArrivedAtFloor(" + elevatorIdx + ", " + floorIdx + ", " + travelDirection + ")");

        // if (elevatorStatuses[elevatorIdx].getTargetFloor() == floorIdx) {
        //     elevatorStatuses[elevatorIdx].reevaluateWaitTime();
        //     elevatorFloorRequestQueues.get(elevatorIdx).removeIf(i -> i.intValue() == floorIdx);
        //     globalFloorRequestQueue.removeIf(r -> r.floor == floorIdx && willElevatorAcceptPassenger(r.direction, travelDirection));
        // }
    }

    private static boolean willElevatorAcceptPassenger(Direction requestDirection, Direction elevatorDirection) {
        switch (elevatorDirection) {
            case Up:
                return requestDirection.equals(Direction.Up);    
            case Down:
                return requestDirection.equals(Direction.Down);
            default:
                return true;
        }
    }

    public boolean hasOutsideRequestForFloor(int floorIdx) {
        return game.hasElevatorRequestDown(floorIdx) ||
         game.hasElevatorRequestUp(floorIdx);
    }

    private boolean shouldPurgeGlobalRequest(ElevatorRequest request) {
        switch (request.direction) {
            case Up:
                return !game.hasElevatorRequestUp(request.floor);
            case Down:
                return !game.hasElevatorRequestDown(request.floor);
        
            default:
                return true;
        }
    }
    private boolean shouldPurgeInternalRequest(int elevatorIdx, int floorIdx) {
        return !game.elevatorHasFloorRequest(elevatorIdx, floorIdx);
    }

    // Event: Called each frame of the simulation (i.e. called continuously)
    public void onUpdate(double deltaTime) {
        if (game == null) {
            return;
        }

        globalFloorRequestQueue.removeIf(this::shouldPurgeGlobalRequest);

        // System.out.println("update()");

        for (int elev = 0; elev < game.getElevatorCount(); elev++) {
            if (calculateLoadingWaitTime(elev) >= Zombie.STARVATION_TIME / 2 &&
             elevatorFloorRequestQueues.get(elev).isEmpty()) { // for insane elevator counts
                gotoNextInIndividualQueue(elev);
                continue;
            }

            final ElevatorStatus elevator = elevatorStatuses[elev];
            if (!elevator.hasArrived()) {
                elevator.reevaluateWaitTime();
                continue;
            }
            else if (elevator.isWaiting()) { // wait for the zombies to actually enter the elevator
                elevator.decreaseRemainingWaitTime(deltaTime);
                // System.out.println("\televator " + elev + ": " + elevatorWaitTimes[elev] + " seconds remaining until door close");
                continue;
            }

            ArrayList<Integer> eQ = elevatorFloorRequestQueues.get(elev);
            eQ.removeIf(floor -> shouldPurgeInternalRequest(elevator.getElevatorIndex(), floor));

            eQ.removeIf(i -> i.intValue() == elevator.getTargetFloor());
            globalFloorRequestQueue.removeIf(r -> r.floor == elevator.getTargetFloor());

            elevatorStressStates[elev] |= game.getLevelTimeRemaining() <= 10;
            elevatorStressStates[elev] |= elevatorFloorRequestQueues.get(elev).size() > ELEVATOR_STRESS_THRESHOLD;
            elevatorStressStates[elev] &= !eQ.isEmpty();

            if ((!eQ.isEmpty() && globalFloorRequestQueue.isEmpty()) || elevatorStressStates[elev]) {
                gotoNextInIndividualQueue(elev);
            }
            else if (!eQ.isEmpty() && !globalFloorRequestQueue.isEmpty()) {
                doGlobalVsIndividualDecisionLogic(elev);
            }
            else if (!globalFloorRequestQueue.isEmpty()) {
                gotoNextInGlobalQueue(elev, false);
            }

            /* Might bring it back in the future, but resetting to the middle
             * every time seems to just be a waste of points and time at the moment
             */
            // else {
            //     gotoFloor(elev, game.getFloorCount() / 2);
            // }
        }
    }

    @Override
    public boolean gotoFloor(int elevatorIdx, int floorIdx) {
        return gotoFloor(elevatorIdx, floorIdx, false);
    }

    public boolean gotoFloor(int elevatorIdx, int floorIdx, boolean configureTravelDirection) {
        if (game.isElevatorIsHeadingToFloor(elevatorIdx, floorIdx) ||
         game.isElevatorIsOnFloor(elevatorIdx, floorIdx)) {
            return true;
        }
        elevatorStatuses[elevatorIdx].setTargetFloor(floorIdx, configureTravelDirection);
        return ElevatorController.super.gotoFloor(elevatorIdx, floorIdx);
    }

    private boolean doGlobalVsIndividualDecisionLogic(int elevatorIdx) {
        final ArrayList<Integer> elevatorQueue = elevatorFloorRequestQueues.get(elevatorIdx);
        if (elevatorQueue.isEmpty()) {
            return gotoNextInGlobalQueue(elevatorIdx, false);
        }

        final double currentFloor = game.getElevatorFloor(elevatorIdx);
        elevatorQueue.sort(Comparator.comparingDouble(f -> f - currentFloor));
        final int individualTarget = elevatorQueue.get(0);
        final double targetDir = Math.signum(individualTarget - currentFloor);
        final Direction targetDirEnum;
        if (targetDir < 0) {
            targetDirEnum = Direction.Down;
        }
        else if (targetDir > 0) {
            targetDirEnum = Direction.Up;
        }
        else {
            targetDirEnum = Direction.None;
        }
        
        ElevatorRequest gReq = null;
        for (ElevatorRequest request : globalFloorRequestQueue) {
            if (Arrays.stream(elevatorStatuses).anyMatch(s -> {
                if (s.getTargetFloor() != request.floor) {
                    return false;
                }
                final Direction dir = game.getElevatorTravelDirection(s.getElevatorIndex());
                return dir == null || dir.equals(Direction.None) || dir.equals(targetDirEnum);
            })) {
                continue; // floor is already handled
            }

            if ((request.floor - currentFloor) * targetDir < 0 ||
             (individualTarget - request.floor) * targetDir < 0) {
                continue; // the request isn't on the way
            }
            
            if (!targetDirEnum.equals(Direction.None) && !request.direction.equals(targetDirEnum)) {
                continue; // request is in the wrong direction
            }

            if (gReq != null && Math.abs(gReq.floor - currentFloor) <= Math.abs(request.floor - currentFloor)) {
                continue;
            }

            gReq = request;
        }

        if (gReq != null) {
            globalFloorRequestQueue.remove(gReq);
            return gotoFloor(elevatorIdx, gReq.floor, true);
        }
        else {
            elevatorQueue.remove(Integer.valueOf(individualTarget));
            return gotoFloor(elevatorIdx, individualTarget, !elevatorQueue.isEmpty());
        }
    }

    private boolean isFloorNotHandled(int testedFloor, int excludeElevatorIdx) {
        return Arrays.stream(elevatorStatuses).noneMatch(s ->
         s.getElevatorIndex() != excludeElevatorIdx && s.getTargetFloor() == testedFloor);
    }

    private boolean gotoNextInGlobalQueue(int elevatorIdx, boolean configureTravelDirection) {
        if (globalFloorRequestQueue.isEmpty()) {
            return false;
        }

        int unstressedCount = 0;
        for (boolean stressState : elevatorStressStates) {
            if (!stressState) {
                unstressedCount++;
            }
        }

        if (globalFloorRequestQueue.stream().distinct().count() <= unstressedCount) {
            final int currentFloor = (int)game.getElevatorFloor(elevatorIdx);
            ElevatorRequest bestRequest = null;
            for (ElevatorRequest req : globalFloorRequestQueue) {
                if (!isFloorNotHandled(currentFloor, elevatorIdx)) {
                    continue;
                }
                
                if (bestRequest == null) {
                    bestRequest = req;
                    continue;
                }

                final int reqFloor = req.floor;
                if (Math.abs(reqFloor - currentFloor) < Math.abs(bestRequest.floor - currentFloor)) {
                    bestRequest = req;
                }
            }
            if (bestRequest != null) {
                globalFloorRequestQueue.remove(bestRequest);
                return gotoFloor(elevatorIdx, bestRequest.floor, configureTravelDirection);
            }
        }
        
        int floorToGoTo;
        while (true) {
            if (globalFloorRequestQueue.isEmpty()) {
                return false;
            }
            floorToGoTo = globalFloorRequestQueue.pop().floor;
            final int testedFloor = floorToGoTo;
            if (isFloorNotHandled(testedFloor, elevatorIdx)) {
                return gotoFloor(elevatorIdx, floorToGoTo, configureTravelDirection);
            }
        }
    }

    private int compareInternalRequests(int elevatorIdx, Integer a, Integer b) {
        final double currentFloor = game.getElevatorFloor(elevatorIdx);
        final double distA = a - currentFloor;
        final double distB = b - currentFloor;
        final boolean reverse = game.getElevatorTravelDirection(elevatorIdx).equals(Direction.Up);

        if (distA == 0 && distB != 0) {
            return -1;
        }
        else if (distB == 0 && distA != 0) {
            return 1;
        }
        else if (Math.signum(distA) == Math.signum(distB)) {
            return Double.compare(Math.abs(distA), Math.abs(distB));
        }
        else if (distA > 0) {
            return reverse ? -1 : 1;
        }
        else {
            return reverse ? 1 : -1;
        }
    }

    private boolean gotoNextInIndividualQueue(int elevatorIdx) {
        final ArrayList<Integer> elevatorQueue = elevatorFloorRequestQueues.get(elevatorIdx);
        if (elevatorQueue.isEmpty()) {
            return false;
        }

        elevatorQueue.sort((a, b) -> compareInternalRequests(elevatorIdx, a, b));

        for (int i = 0; i < elevatorQueue.size(); i++) {
            final int floorToGoTo = elevatorQueue.get(i);
            if (Arrays.stream(elevatorStatuses).noneMatch(s -> s.getTargetFloor() == floorToGoTo)) {
                elevatorQueue.removeIf(f -> f.intValue() == floorToGoTo);
                return gotoFloor(elevatorIdx, floorToGoTo, !elevatorQueue.isEmpty());
            }
        }
        return false;
    }
}