package liftkata.internal;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Display {

    private static final int FLOOR_WIDTH = 15;
    private static final int LIFT_WIDTH = 15;

    private static final String CEILING = Strings.repeat("─", FLOOR_WIDTH);
    private static final String SPACE = Strings.repeat(" ", FLOOR_WIDTH);

    public void display(Simulation simulation) {
        final List<Floor> floors = Lists.reverse(Lists.newArrayList(simulation.getFloors()));
        final Elevator elevator = simulation.getElevator();

        for (Floor floor : floors) {
            display(elevator, floor);
        }

        System.out.printf("\033[%dA", simulation.getFloors().size() * 3);
    }

    private void display(Elevator elevator, Floor floor) {

        final int floorNumber = floor.getFloorNumber();

        final String waitingFormatStr = String.format("%%-%ds", FLOOR_WIDTH);
        final String liftFormatStr = String.format("%%-%ds", LIFT_WIDTH - 2);
        final String arrivedFormatStr = String.format("%%%ds", FLOOR_WIDTH);

        final String waiting = String.format(waitingFormatStr, passengersToString(floor.getWaitingPassengers()));
        final String travelling = String.format(liftFormatStr, passengersToString(elevator.getPassengers()));
        final String arrived = String.format(arrivedFormatStr, passengersToString(floor.getArrivedPassengers()));

        final int elevatorFloorNumber = elevator.getCurrentFloor().getFloorNumber();
        final String liftTopOrSpace = (elevatorFloorNumber == floorNumber) ?
                "╔" + Strings.repeat("═", LIFT_WIDTH - 2) + "╗" :
                Strings.repeat(" ", LIFT_WIDTH);

        final String liftPassengersOrSpace = (elevatorFloorNumber == floorNumber) ?
                "║" + travelling + "║" :
                Strings.repeat(" ", LIFT_WIDTH);

        final String liftBottomOrFloor = (elevatorFloorNumber == floorNumber) ?
                "╚" + Strings.repeat("═", LIFT_WIDTH - 2) + "╝" :
                Strings.repeat("─", LIFT_WIDTH);

        System.out.printf("%d  %s%s%s\n", floorNumber, SPACE, liftTopOrSpace, SPACE);
        System.out.printf("   %s%s%s\n", waiting, liftPassengersOrSpace, arrived);
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
