package marsrover.internal;

import java.util.List;
import java.util.stream.Collectors;

public class Display {

    private final Grid grid;

    public Display(Grid grid) {
        this.grid = grid;
    }

    public Grid getGrid() {
        return grid;
    }

    public String renderGrid() {
        final StringBuilder builder = new StringBuilder();
        for (int y = grid.getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x <= grid.getWidth() - 1; x++) {
                builder.append(renderLocation(new Location(x, y)));
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private String renderLocation(Location location) {
        final List<Rover> roversAtLocation = grid.getRovers().stream()
                .filter(x -> location.equals(x.getLocation()))
                .collect(Collectors.toList());

        if (roversAtLocation.isEmpty()) {
            return ".";
        }
        else {
            return renderRover(roversAtLocation.get(0));
        }
    }

    private String renderRover(Rover rover) {
        switch (rover.getBearing()) {
            case NORTH:
                return "^";
            case EAST:
                return ">";
            case SOUTH:
                return "v";
            case WEST:
                return "<";
            default:
                throw new IllegalStateException("Unknown bearing " + rover.getBearing());
        }
    }
}
