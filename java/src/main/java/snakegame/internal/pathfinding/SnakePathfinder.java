package snakegame.internal.pathfinding;

import com.google.common.collect.Sets;
import snakegame.internal.Location;
import snakegame.internal.Snake;
import snakegame.internal.pathfinding.Pathfinder.CostFunction;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SnakePathfinder {

    // TODO: Needs to be common
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;

    private final Pathfinder<Location> pathfinder;

    public SnakePathfinder() {
        pathfinder = new Pathfinder<>(SnakePathfinder::chebyshevDistance);
    }

    public List<Location> findPath(Snake snake,
                                   Location end) {

        final CostFunction<Location> moveFunction = (startLoc, endLoc) -> {
            if (snake.getBody().contains(endLoc)) {
                return Integer.MAX_VALUE;
            }
            else {
                return 1.0;
            }
        };

        final Location head = snake.getBody().get(0);
        return pathfinder.findPath(head, end, moveFunction, SnakePathfinder::neighboursOf);
    }

    private static Set<Location> neighboursOf(Location location) {
        final int y = location.getY();
        final int x = location.getX();

        final Set<Location> locations = Sets.newHashSet(
                                            new Location(x, y - 1),
                new Location(x - 1, y),     /*                  */  new Location(x + 1, y),
                                            new Location(x, y + 1));

        return locations.stream()
                .filter(SnakePathfinder::isInBounds)
                .collect(Collectors.toSet());
    }

    private static boolean isInBounds(Location loc) {
        return 0 <= loc.getX() && loc.getX() < WIDTH &&
                0 <= loc.getY() && loc.getY() <= HEIGHT;
    }

    private static double chebyshevDistance(Location start,
                                            Location end) {
        return Math.max(
                Math.abs(start.getX() - end.getX()),
                Math.abs(start.getY() - end.getY())
        );
    }
}
