package liftkata.internal;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class Display {

    private static final int FLOOR_WIDTH = 15;
    private static final int ELEVATOR_WIDTH = 15;
    private static final int ELEVATOR_HEIGHT = 3;

    private static final String CEILING = Strings.repeat("─", FLOOR_WIDTH);
    private static final String SPACE = Strings.repeat(" ", FLOOR_WIDTH);

    public void display(Simulation simulation) {
        final List<Floor> floors = Lists.reverse(Lists.newArrayList(simulation.getFloors()));
        final Elevator elevator = simulation.getElevator();

        for (Floor floor : floors) {
            display(elevator, floor);
        }

        System.out.printf("\033[%dA", simulation.getFloors().size() * ELEVATOR_HEIGHT);
    }

    private void display(Elevator elevator, Floor floor) {

        final int floorNumber = floor.getFloorNumber();

        final String waitingFormatStr = String.format("%%-%ds", FLOOR_WIDTH);
        final String liftFormatStr = String.format("%%-%ds", ELEVATOR_WIDTH - 2);
        final String arrivedFormatStr = String.format("%%%ds", FLOOR_WIDTH);

        final String waiting = String.format(waitingFormatStr, passengersToString(floor.getWaitingPassengers()));
        final String travelling = String.format(liftFormatStr, passengersToString(elevator.getPassengers()));
        final String arrived = String.format(arrivedFormatStr, passengersToString(floor.getArrivedPassengers()));

        final int elevatorFloorNumber = elevator.getCurrentFloor().getFloorNumber();
        final String elevatorTopOrSpace = (elevatorFloorNumber == floorNumber) ?
                "╔" + Strings.repeat("═", ELEVATOR_WIDTH - 2) + "╗" :
                Strings.repeat(" ", ELEVATOR_WIDTH);

        final String elevatorPassengersOrSpace = (elevatorFloorNumber == floorNumber) ?
                "║" + travelling + "║" :
                Strings.repeat(" ", ELEVATOR_WIDTH);

        final String elevatorBottomOrFloor = (elevatorFloorNumber == floorNumber) ?
                "╚" + Strings.repeat("═", ELEVATOR_WIDTH - 2) + "╝" :
                Strings.repeat("─", ELEVATOR_WIDTH);

        System.out.printf("%d  %s%s%s\n", floorNumber, SPACE, elevatorTopOrSpace, SPACE);
        System.out.printf("   %s%s%s\n", waiting, elevatorPassengersOrSpace, arrived);
        System.out.printf("   %s%s%s\n", CEILING, elevatorBottomOrFloor, CEILING);
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
