package liftkata.internal;

import java.util.Optional;

public interface ElevatorDestinationController {

    void setCurrentFloor(int floor);

    void addDestination(int destination);

    Optional<Integer> getNextDestination();
}
