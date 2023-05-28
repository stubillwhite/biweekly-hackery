package liftkata.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

public class Floor {

    private final int floorNumber;
    private final Elevator elevator;

    private final List<Passenger> waitingPassengers = Lists.newArrayList();
    private final List<Passenger> arrivedPassengers = Lists.newArrayList();

    public Floor(int floorNumber, Elevator elevator) {
        this.floorNumber = floorNumber;
        this.elevator = elevator;
    }

    public void addWaitingPassenger(Passenger passenger) {
        waitingPassengers.add(passenger);
    }

    public List<Passenger> getWaitingPassengers() {
        return ImmutableList.copyOf(waitingPassengers);
    }

    public List<Passenger> getArrivedPassengers() {
        return ImmutableList.copyOf(arrivedPassengers);
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void callElevator() {
        elevator.addDestination(floorNumber);
    }

    public boolean elevatorHasArrived() {
        return elevator.hasArrivedAt(floorNumber);
    }

    public Elevator boardElevator(Passenger passenger) {
        waitingPassengers.remove(passenger);
        elevator.board(passenger);
        return elevator;
    }

    public void arriveAtDestination(Passenger passenger) {
        arrivedPassengers.add(passenger);
    }
}
