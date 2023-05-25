package liftkata.internal;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class Display {

    private static final int FLOOR_WIDTH = 15;
    private static final int LIFT_WIDTH = 15;

    private static final String CEILING = Strings.repeat("─", FLOOR_WIDTH);
    private static final String SPACE = Strings.repeat(" ", FLOOR_WIDTH);

    public void display(Building building) {
        final List<Floor> floors = Lists.reverse(Lists.newArrayList(building.getFloors()));
        final Elevator elevator = building.getElevator();

        for (Floor floor : floors) {
            display(elevator, floor);
        }
    }

    private void display(Elevator elevator, Floor floor) {

        final int floorNumber = floor.getNumber();

        final String waitingFormatStr = String.format("%%-%ds", FLOOR_WIDTH);
        final String liftFormatStr = String.format("%%-%ds", LIFT_WIDTH - 2);

        final String waiting = String.format(waitingFormatStr, passengersToString(floor.getPassengers()));
        final String travelling = String.format(liftFormatStr, passengersToString(elevator.getPassengers()));

        final String liftTopOrSpace = (elevator.getCurrentFloor() == floorNumber) ?
                "╔" + Strings.repeat("═", LIFT_WIDTH - 2) + "╗" :
                Strings.repeat(" ", LIFT_WIDTH);

        final String liftPassengersOrSpace = (elevator.getCurrentFloor() == floorNumber) ?
                "║" + travelling + "║" :
                Strings.repeat(" ", LIFT_WIDTH);

        final String liftBottomOrFloor = (elevator.getCurrentFloor() == floorNumber) ?
                "╚" + Strings.repeat("═", LIFT_WIDTH - 2) + "╝" :
                Strings.repeat("─", LIFT_WIDTH);

        System.out.printf("%d  %s%s%s\n", floorNumber, SPACE, liftTopOrSpace, SPACE);
        System.out.printf("   %s%s%s\n", waiting, liftPassengersOrSpace, SPACE);
        System.out.printf("   %s%s%s\n", CEILING, liftBottomOrFloor, CEILING);
    }

    private String passengersToString(List<Passenger> passengers) {
        return passengers.stream()
                .map(this::passengerToString)
                .collect(Collectors.joining(" "));
    }

    private String passengerToString(Passenger passenger) {
        return String.format("%s%d", passenger.getId(), passenger.getDestinationFloorNumber());
    }

}
