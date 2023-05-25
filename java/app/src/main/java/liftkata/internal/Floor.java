package liftkata.internal;

import com.google.common.collect.Lists;

import java.util.List;

public class Floor {

    private final List<Passenger> passengers = Lists.newArrayList();

    public Floor() {
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }
}
