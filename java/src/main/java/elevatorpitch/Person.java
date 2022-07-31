package elevatorpitch;

class Person {

    // TODO: Hashcode and equals

    private final int originalFloor;
    private final int destinationFloor;
    private final Elevator elevator;

    private PersonState state;

    public Person(int originalFloor, int destinationFloor, Elevator elevator) {
        this.originalFloor = originalFloor;
        this.destinationFloor = destinationFloor;
        this.elevator = elevator;
        this.state = PersonState.CALLING_ELEVATOR;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public int getOriginalFloor() {
        return originalFloor;
    }

    public PersonState getState() {
        return state;
    }

    public void update() {
        switch (state) {

            case CALLING_ELEVATOR:
                elevator.setDestination(originalFloor);
                break;

            case WAITING_TO_BOARD:
                if (elevator.getState() == ElevatorState.WAITING && originalFloor == elevator.getCurrentFloor()) {
                    state = PersonState.RIDING_ELEVATOR;
                }
                break;

            case RIDING_ELEVATOR:
                if (destinationFloor == elevator.getCurrentFloor()) {
                    state = PersonState.ARRIVED_AT_DESTINATION;
                }
                break;

            case ARRIVED_AT_DESTINATION:
                break;
        }
    }
}
