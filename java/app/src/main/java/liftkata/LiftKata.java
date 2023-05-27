package liftkata;

import com.google.common.collect.Lists;
import liftkata.internal.*;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class LiftKata {
    public static void main(String[] args) {
        final List<Floor> floors = Lists.newArrayList(
                new Floor(0),
                new Floor(1),
                new Floor(2),
                new Floor(3),
                new Floor(4)
        );

        final Elevator elevator = new Elevator(floors, 0);

        final Passenger passengerA = new Passenger("A", 1);

        final Floor passengerStartingFloor = floors.get(4);
        passengerStartingFloor.getWaitingPassengers().add(passengerA);
        passengerA.setCurrentFloor(passengerStartingFloor);

        final Building building = new Building(elevator, floors);
        final Display display = new Display();

        while (!isComplete(building)) {
            display.display(building);
            building.updateState();
            pause();
        };

        display.display(building);
        pause();

        System.out.println("Done");
    }

    private static boolean isComplete(Building building) {
        final List<Passenger> waitingPassengers = building.getFloors().stream()
                .flatMap(x -> x.getWaitingPassengers().stream())
                .collect(toList());

        final List<Passenger> ridingPassengers = building.getElevator().getPassengers();

        return waitingPassengers.isEmpty() && ridingPassengers.isEmpty();
    }

    private static void pause() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
