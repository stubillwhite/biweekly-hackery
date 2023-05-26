package liftkata.internal;

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
        floors.forEach(floor -> {
            floor.getPassengers().forEach(passenger -> passenger.updateState());
        });
    }
}
