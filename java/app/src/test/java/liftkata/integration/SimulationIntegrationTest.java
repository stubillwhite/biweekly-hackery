package liftkata.integration;

import liftkata.internal.ElevatorDestinationController;
import liftkata.internal.FIFODestinationController;
import liftkata.internal.Simulation;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static liftkata.internal.Elevator.State;
import static org.assertj.core.api.Assertions.assertThat;

public class SimulationIntegrationTest {

    @Test
    public void singlePassengerCallsLiftAndRidesToDestination() {
        // Given
        final ElevatorDestinationController controller = new FIFODestinationController();
        final Simulation simulation = Simulation.builder()
                .withElevator(controller)
                .withFloors(2)
                .withPassenger("A", 1, 0)
                .build();

        // Initial state
        assertElevatorPassengerCountIs(simulation, 0);
        assertWaitingPassengersOnFloorCountIs(simulation, 0, 0);
        assertWaitingPassengersOnFloorCountIs(simulation, 1, 1);
        assertArrivedPassengersOnFloorCountIs(simulation, 0, 0);
        assertArrivedPassengersOnFloorCountIs(simulation, 1, 0);

        // Call to first floor
        simulation.updateState();
        assertElevatorNextDestinationIs(controller, 1);
        assertElevatorCurrentFloorIs(simulation, 0);
        assertElevatorStateIs(simulation, State.GOING_UP);

        // Arrive at the floor
        simulation.updateState();
        assertElevatorNextDestinationIsEmpty(controller);
        assertElevatorCurrentFloorIs(simulation, 1);
        assertElevatorStateIs(simulation, State.WAITING);
        assertElevatorPassengerCountIs(simulation, 0);

        // Passenger boards elevator and sets destination
        simulation.updateState();
        assertElevatorNextDestinationIs(controller, 0);
        assertElevatorCurrentFloorIs(simulation, 1);
        assertElevatorStateIs(simulation, State.GOING_DOWN);
        assertElevatorPassengerCountIs(simulation, 1);
        assertWaitingPassengersOnFloorCountIs(simulation, 1, 0);

        // Lift arrives at destination
        simulation.updateState();
        assertElevatorNextDestinationIsEmpty(controller);
        assertElevatorCurrentFloorIs(simulation, 0);
        assertElevatorStateIs(simulation, State.WAITING);

        // Passenger leaves elevator
        simulation.updateState();
        assertElevatorPassengerCountIs(simulation, 0);
        assertArrivedPassengersOnFloorCountIs(simulation, 0, 1);
    }

    private static void assertElevatorNextDestinationIsEmpty(ElevatorDestinationController controller) {
        assertThat(controller.getNextDestination()).isEqualTo(Optional.empty());
    }

    private static void assertElevatorNextDestinationIs(ElevatorDestinationController controller, int value) {
        assertThat(controller.getNextDestination()).isEqualTo(Optional.of(value));
    }

    private static void assertWaitingPassengersOnFloorCountIs(Simulation simulation, int floorNumber, int count) {
        assertThat(simulation.getFloors().get(floorNumber).getWaitingPassengers().size()).isEqualTo(count);
    }

    private static void assertArrivedPassengersOnFloorCountIs(Simulation simulation, int floorNumber, int count) {
        assertThat(simulation.getFloors().get(floorNumber).getArrivedPassengers().size()).isEqualTo(count);
    }

    private static void assertElevatorStateIs(Simulation simulation, State state) {
        assertThat(simulation.getElevator().getState()).isEqualTo(state);
    }

    private static void assertElevatorPassengerCountIs(Simulation simulation, int count) {
        assertThat(simulation.getElevator().getPassengers().size()).isEqualTo(count);
    }

    private static void assertElevatorCurrentFloorIs(Simulation simulation, int floorNumber) {
        assertThat(simulation.getElevator().getCurrentFloor().getFloorNumber()).isEqualTo(floorNumber);
    }
}
