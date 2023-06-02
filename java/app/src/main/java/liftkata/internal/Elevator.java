package liftkata.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;

public class Elevator implements Stateful {

    public enum State {
        WAITING,
        GOING_UP,
        GOING_DOWN
    }

    private final List<Passenger> passengers = Lists.newArrayList();

    private final ElevatorDestinationController controller;
    private final List<Floor> floors;

    private int currentFloor;
    private State currentState;

    public Elevator(ElevatorDestinationController controller, List<Floor> floors, int startingFloor) {
        this.controller = controller;
        controller.setCurrentFloor(startingFloor);

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
        controller.addDestination(floorNumber);
    }

    @Override
    public void updateState() {
        updateCurrentFloor();
        updateCurrentState();
    }

    public State getState() {
        return currentState;
    }


    private void updateCurrentFloor() {
    }

    private void updateCurrentState() {
        final Optional<Integer> nextDestination = controller.getNextDestination();
        switch (currentState) {
            case WAITING:
                if (nextDestination.isPresent()) {
                    final int currentDestination = nextDestination.get();

                    if (currentFloor > currentDestination) {
                        currentState = State.GOING_DOWN;
                    } else if (currentFloor == currentDestination) {
                        currentState = State.WAITING;
                    } else {
                        currentState = State.GOING_UP;
                    }
                }
                break;

            case GOING_UP:
                currentFloor += 1;
                if (nextDestination.isPresent() && currentFloor == nextDestination.get()) {
                    currentState = State.WAITING;
                }
                controller.setCurrentFloor(currentFloor);
                break;

            case GOING_DOWN:
                currentFloor -= 1;
                if (nextDestination.isPresent() && currentFloor == nextDestination.get()) {
                    currentState = State.WAITING;
                }
                controller.setCurrentFloor(currentFloor);
                break;
        }
    }
}
