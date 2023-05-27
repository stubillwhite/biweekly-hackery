package liftkata.internal;

public class Passenger implements Stateful {

    private enum State {
        CALLING_ELEVATOR,
        WAITING_FOR_ELEVATOR,
        RIDING_ELEVATOR,
        ARRIVED_AT_DESTINATION
    }

    ;

    private final String id;
    private final int destination;

    private State currentState;
    private Floor currentFloor;
    private Elevator currentElevator;

    public Passenger(String id, int destination) {
        this.id = id;
        this.destination = destination;
        this.currentState = State.CALLING_ELEVATOR;
    }

    public String getId() {
        return id;
    }

    public Floor getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(Floor currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getDestinationFloorNumber() {
        return destination;
    }

    @Override
    public void updateState() {
        switch (currentState) {
            case CALLING_ELEVATOR:
                currentFloor.getElevator().callToFloor(currentFloor.getFloorNumber());
                currentState = State.WAITING_FOR_ELEVATOR;
                break;

            case WAITING_FOR_ELEVATOR:
                if (currentFloor.elevatorHasArrived()) {
                    currentElevator = currentFloor.getElevator();

                    currentFloor.getWaitingPassengers().remove(this);
                    currentElevator.board(this);
                    currentFloor = null;

                    currentElevator.callToFloor(destination);
                    currentState = State.RIDING_ELEVATOR;
                }
                break;

            case RIDING_ELEVATOR:
                if (currentElevator.getCurrentFloor().getFloorNumber() == destination &&
                        currentElevator.getCurrentState() == Elevator.State.WAITING) {
                    currentFloor = currentElevator.getCurrentFloor();

                    currentElevator.leave(this);
                    currentFloor.getArrivedPassengers().add(this);
                    currentElevator = null;

                    currentState = State.ARRIVED_AT_DESTINATION;
                }
                break;

            case ARRIVED_AT_DESTINATION:
                break;
        }
    }
}
