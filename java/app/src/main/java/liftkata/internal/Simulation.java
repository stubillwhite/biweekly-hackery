package liftkata.internal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Simulation implements Stateful {

    private final Elevator elevator;
    private final List<Floor> floors;
    private final Display display;

    public static class Builder {
        private final List<Floor> floors = Lists.newArrayList();

        private Elevator elevator;
        private Display display;

        private Builder() {
        }

        public Builder withElevator(ElevatorDestinationController controller) {
            elevator = new Elevator(controller, floors, 0);
            return this;
        }

        public Builder withFloors(int n) {
            IntStream.range(0, n)
                    .forEach(x -> {
                        final Floor floor = new Floor(x, elevator);
                        floors.add(floor);
                    });

            return this;
        }

        public Builder withPassenger(String id, int startingFloor, int destination) {
            final Passenger passenger = new Passenger(id, destination);
            final Floor passengerAStartingFloor = floors.get(startingFloor);
            passengerAStartingFloor.addWaitingPassenger(passenger);
            passenger.setCurrentFloor(passengerAStartingFloor);
            return this;
        }

        public Builder withDisplay() {
            this.display = new Display();
            return this;
        }

        public Simulation build() {
            return new Simulation(elevator, floors, display);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private Simulation(Elevator elevator, List<Floor> floors, Display display) {
        this.elevator = elevator;
        this.floors = floors;
        this.display = display;
    }

    public Elevator getElevator() {
        return elevator;
    }

    public List<Floor> getFloors() {
        return ImmutableList.copyOf(floors);
    }

    @Override
    public void updateState() {
        final Stream<Stateful> elevators = Stream.of(elevator);
        final Stream<Stateful> elevatorPassengers = Lists.<Stateful>newArrayList(elevator.getPassengers()).stream();
        final Stream<Stateful> waitingPassengers = floors.stream().flatMap(floor -> Lists.newArrayList(floor.getWaitingPassengers()).stream());

        Streams.concat(elevatorPassengers, waitingPassengers, elevators).forEach(Stateful::updateState);
    }

    public void run() {
        while (!isComplete()) {
            display.display(this);
            updateState();
            pause();
        }

        display.display(this);
        pause();
    }

    private boolean isComplete() {
        final List<Passenger> waitingPassengers = floors.stream()
                .flatMap(x -> x.getWaitingPassengers().stream())
                .collect(toList());

        final List<Passenger> ridingPassengers = elevator.getPassengers();

        return waitingPassengers.isEmpty() && ridingPassengers.isEmpty();
    }

    private static void pause() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
