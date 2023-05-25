package liftkata.internal;

import com.google.common.collect.Lists;

import java.util.List;

public class Elevator {

    private final List<Passenger> passengers = Lists.newArrayList();

    public Elevator() {
    }

    public void embark(Passenger passenger) {
        passengers.add(passenger);
    }

    public int getCurrentFloor() {
        return 1;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }
}
