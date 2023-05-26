package liftkata.internal;

import com.google.common.collect.Lists;

import java.util.List;

public class Floor {

    private final List<Passenger> passengers = Lists.newArrayList();
    private final int floorNumber;

    private Elevator elevator;

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public void setElevator(Elevator elevator) {
        this.elevator = elevator;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void callElevator() {
        elevator.callToFloor(floorNumber);
    }
}
