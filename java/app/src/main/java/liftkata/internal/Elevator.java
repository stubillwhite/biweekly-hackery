package liftkata.internal;

import com.google.common.collect.Lists;

import java.util.List;

public class Elevator implements Stateful {


    private enum State {
        WAITING,
        GOING_UP,
        GOING_DOWN
    }

    private final List<Passenger> passengers = Lists.newArrayList();
    private final List<Floor> floors;

    private int currentFloor;
    private State currentState;
    private int currentDestination;

    public Elevator(List<Floor> floors, int startingFloor) {
        this.currentState = State.WAITING;
        this.floors = floors;
        this.currentFloor = startingFloor;

        floors.forEach(x -> x.setElevator(this));
    }

    public void embark(Passenger passenger) {
        passengers.add(passenger);
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void callToFloor(int floorNumber) {
        currentDestination = floorNumber;
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
        if (currentFloor > currentDestination) {
            currentState = State.GOING_DOWN;
        } else if (currentFloor == currentDestination) {
            currentState = State.WAITING;
        } else {
            currentState = State.GOING_UP;
        }
    }
}
