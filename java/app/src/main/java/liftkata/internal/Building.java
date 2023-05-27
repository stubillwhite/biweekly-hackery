package liftkata.internal;

import com.google.common.collect.Lists;

import java.util.List;

public class Building implements Stateful {

    private final Elevator elevator;
    private final List<Floor> floors;

    public Building(Elevator elevator, List<Floor> floors) {
        this.elevator = elevator;
        this.floors = floors;
    }

    public Elevator getElevator() {
        return elevator;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    @Override
    public void updateState() {
        elevator.updateState();

        final List<Passenger> elevatorPassengersToUpdate = Lists.newArrayList(elevator.getPassengers());
        elevatorPassengersToUpdate.forEach(passenger -> passenger.updateState());

        floors.forEach(floor -> {
            final List<Passenger> floorPassengersToUpdate = Lists.newArrayList(floor.getWaitingPassengers());
            floorPassengersToUpdate.forEach(passenger -> passenger.updateState());
        });
    }
}
