package marsrover.internal;

import static marsrover.internal.Bearing.*;

public class Rover {

    private Location location;
    private Bearing bearing;

    public Rover(Location location, Bearing bearing) {
        this.location = location;
        this.bearing = bearing;
    }

    public Location getLocation() {
        return location;
    }

    public Bearing getBearing() {
        return bearing;
    }

    public void executeInstruction(Instruction instruction) {
        switch (instruction) {
            case ROTATE_LEFT:
                rotateLeft();
                break;

            case ROTATE_RIGHT:
                rotateLeft();
                rotateLeft();
                rotateLeft();
                break;

            case MOVE_FORWARD:
                moveForward();
                break;

            default:
                throw new IllegalStateException("Unknown instruction " + instruction);
        }
    }

    private void moveForward() {
        location = getLocationAhead();
    }

    private void rotateLeft() {
        switch (bearing) {
            case NORTH:
                bearing = WEST;
                break;
            case EAST:
                bearing = NORTH;
                break;
            case SOUTH:
                bearing = EAST;
                break;
            case WEST:
                bearing = SOUTH;
                break;
            default:
                throw new IllegalStateException("Unknown bearing " + bearing);
        }
    }

    private Location getLocationAhead() {
        switch (bearing) {
            case NORTH:
                return new Location(location.getX(), location.getY() + 1);

            case EAST:
                return new Location(location.getX() + 1, location.getY());

            case SOUTH:
                return new Location(location.getX(), location.getY() - 1);

            case WEST:
                return new Location(location.getX() - 1, location.getY());

            default:
                throw new IllegalStateException("Unknown bearing " + bearing);
        }
    }
}
