package liftkata.internal;

import com.google.common.collect.Lists;

import java.util.List;

public class Floor {

    private final List<Passenger> passengers = Lists.newArrayList();
    private final int number;

    public Floor(int number) {
        this.number = number;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public int getNumber() {
        return number;
    }
}
