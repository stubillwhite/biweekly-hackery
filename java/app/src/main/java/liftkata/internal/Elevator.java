package liftkata.internal;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

import java.util.Deque;
import java.util.List;

public class Elevator implements Stateful {

    public enum State {
        WAITING,
        GOING_UP,
        GOING_DOWN
    }

    private final List<Passenger> passengers = Lists.newArrayList();
    private final List<Floor> floors;

    private int currentFloor;
    private State currentState;
    private Deque<Integer> destinationQueue = Queues.newArrayDeque();

    public Elevator(List<Floor> floors, int startingFloor) {
        this.currentState = State.WAITING;
        this.floors = floors;
        this.currentFloor = startingFloor;

        floors.forEach(x -> x.setElevator(this));
    }

    public void board(Passenger passenger) {
        passengers.add(passenger);
    }

    public void leave(Passenger passenger) {
        passengers.remove(passenger);
    }

    public Floor getCurrentFloor() {
        return floors.get(currentFloor);
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void callToFloor(int floorNumber) {
        destinationQueue.add(floorNumber);
    }

    public State getCurrentState() {
        return currentState;
    }

    @Override
    public void updateState() {
        updateCurrentState();
        updateCurrentFloor();
    }

    private void updateCurrentFloor() {
        switch (currentState) {
            case GOING_UP:
                currentFloor += 1;
                break;

            case WAITING:
                break;

            case GOING_DOWN:
                currentFloor -= 1;
                break;
        }
    }

    private void updateCurrentState() {
        if (!destinationQueue.isEmpty()) {
            final Integer currentDestination = destinationQueue.peek();

            if (currentFloor > currentDestination) {
                currentState = State.GOING_DOWN;
            } else if (currentFloor == currentDestination) {
                currentState = State.WAITING;
                destinationQueue.pop();
            } else {
                currentState = State.GOING_UP;
            }
        }
    }
}
