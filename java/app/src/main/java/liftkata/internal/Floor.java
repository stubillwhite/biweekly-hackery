package liftkata.internal;

import com.google.common.collect.Lists;

import java.util.List;

public class Floor {

    private final int floorNumber;
    private final List<Passenger> waitingPassengers = Lists.newArrayList();
    private final List<Passenger> arrivedPassengers = Lists.newArrayList();

    private Elevator elevator;

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public void setElevator(Elevator elevator) {
        this.elevator = elevator;
    }

    public List<Passenger> getWaitingPassengers() {
        return waitingPassengers;
    }

    public List<Passenger> getArrivedPassengers() {
        return arrivedPassengers;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public boolean elevatorHasArrived() {
        return elevator.getCurrentState() == Elevator.State.WAITING
                && elevator.getCurrentFloor().getFloorNumber() == floorNumber;
    }

    public Elevator getElevator() {
        return elevator;
    }
}
