package liftkata.internal;

public class Passenger {

    private final String id;
    private final int destination;

    public Passenger(String id, int destination) {

        this.id = id;
        this.destination = destination;
    }

    public String getId() {
        return id;
    }

    public int getDestinationFloorNumber() {
        return destination;
    }
}
