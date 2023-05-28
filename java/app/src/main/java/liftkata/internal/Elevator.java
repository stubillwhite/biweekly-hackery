package liftkata.internal;

import com.google.common.collect.ImmutableList;
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
    private final Deque<Integer> destinationQueue = Queues.newArrayDeque();

    private final List<Floor> floors;

    private int currentFloor;
    private State currentState;

    public Elevator(List<Floor> floors, int startingFloor) {
        this.currentState = State.WAITING;
        this.floors = floors;
        this.currentFloor = startingFloor;
    }

    public void board(Passenger passenger) {
        passengers.add(passenger);
    }

    public boolean hasArrivedAt(int destination) {
        return (currentFloor == destination && currentState == State.WAITING);
    }

    public Floor leave(Passenger passenger) {
        final Floor floor = floors.get(currentFloor);
        passengers.remove(passenger);
        return floor;
    }

    public Floor getCurrentFloor() {
        return floors.get(currentFloor);
    }

    public List<Passenger> getPassengers() {
        return ImmutableList.copyOf(passengers);
    }

    public void addDestination(int floorNumber) {
        destinationQueue.add(floorNumber);
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
