package elevatorpitch;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

class Elevator {

    private final Set<Person> occupants;
    private final Queue<Integer> destinations;

    private int currentFloor;

    public Elevator() {
        this.occupants = new HashSet<>();
        this.destinations = new ArrayDeque<>();
        this.currentFloor = 0;
    }



    ElevatorState getState();

    int getCurrentFloor();

    void setDestination(int destination);
}
