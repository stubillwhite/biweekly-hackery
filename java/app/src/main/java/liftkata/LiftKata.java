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

        addPassenger(floors, "A", 4, 1);
        addPassenger(floors, "B", 2, 0);
        addPassenger(floors, "C", 3, 2);

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

    private static void addPassenger(List<Floor> floors, String id, int startingFloor, int destination) {
        final Passenger passengerA = new Passenger(id, destination);
        final Floor passengerAStartingFloor = floors.get(startingFloor);
        passengerAStartingFloor.getWaitingPassengers().add(passengerA);
        passengerA.setCurrentFloor(passengerAStartingFloor);
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
