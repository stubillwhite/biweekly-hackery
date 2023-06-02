package liftkata.internal;

import com.google.common.collect.Queues;

import java.util.Deque;
import java.util.Optional;

public class FIFODestinationController implements ElevatorDestinationController {

    private final Deque<Integer> destinationQueue = Queues.newArrayDeque();

    private int currentFloor;

    public FIFODestinationController() {
        currentFloor = 0;
    }

    @Override
    public void setCurrentFloor(int floor) {
        currentFloor = floor;

        if (destinationQueue.size() > 0 && destinationQueue.getFirst().equals(currentFloor)) {
            destinationQueue.pop();
        }
    }

    @Override
    public void addDestination(int destination) {
        destinationQueue.add(destination);
    }

    @Override
    public Optional<Integer> getNextDestination() {
        return Optional.ofNullable(destinationQueue.peek());
    }
}
