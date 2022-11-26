package snakegame.internal.pathfinding;

import com.google.common.collect.Sets;
import snakegame.internal.Location;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LocationPathfinder {

    // TODO: Needs to be common
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;

    private final Pathfinder<Location> pathfinder;

    public LocationPathfinder() {
        pathfinder = new Pathfinder<>(
                LocationPathfinder::chebyshevDistance,
                LocationPathfinder::constantMoveCost);
    }

    public List<Location> findPath(Location start,
                                   Location end) {
        return pathfinder.findPath(start, end, LocationPathfinder::neighboursOf);
    }

    private static Set<Location> neighboursOf(Location location) {
        final int y = location.getY();
        final int x = location.getX();

        final Set<Location> locations = Sets.newHashSet(
                                            new Location(x, y - 1),
                new Location(x - 1, y),     /*                  */  new Location(x + 1, y),
                                            new Location(x, y + 1));

        return locations.stream()
                .filter(LocationPathfinder::isInBounds)
                .collect(Collectors.toSet());
    }

    private static boolean isInBounds(Location loc) {
        return 0 <= loc.getX() && loc.getX() < WIDTH &&
                0 <= loc.getY() && loc.getY() <= HEIGHT;
    }

    private static double chebyshevDistance(Location a,
                                            Location b) {
        return Math.max(
                Math.abs(a.getX() - b.getX()),
                Math.abs(a.getY() - b.getY())
        );
    }

    public static double constantMoveCost(Location a,
                                          Location b) {
        return 1.0;
    }
}
