package snakegame.internal.pathfinding;

import com.google.common.collect.Sets;
import snakegame.internal.Location;

import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

// https://stackabuse.com/graphs-in-java-a-star-algorithm/
public class Pathfinder {

    private final PriorityQueue<CostedLocation> closedList = new PriorityQueue<>();
    private final PriorityQueue<CostedLocation> openList = new PriorityQueue<>();
    private final Set<Object> nodes;

    private final int WIDTH = 10;
    private final int HEIGHT = 10;

    private final Location start;
    private final Location end;
    private final CostFunction heuristicFunction;
    private final CostFunction moveFunction;

    public interface CostFunction {
        double calculateCost(Location start, Location end);
    }

    private static class CostedLocation implements Comparable {
        public final Location location;
        public final double heuristicCost;
        public final double moveCost;
        public final CostedLocation parent;

        public CostedLocation(Location location,
                              double heuristicCost,
                              double moveCost,
                              CostedLocation parent) {
            this.location = location;
            this.heuristicCost = heuristicCost;
            this.moveCost = moveCost;
            this.parent = parent;
        }

        @Override
        public int compareTo(Object o) {
            final CostedLocation that = CostedLocation.class.cast(o);
            return Double.compare(this.heuristicCost + this.moveCost, that.heuristicCost + that.moveCost);
        }

        @Override
        public String toString() {
            return location.toString();
        }
    }

    public Pathfinder(Location start,
                      Location end,
                      CostFunction heuristicFunction,
                      CostFunction moveFunction) {
        this.start = start;
        this.end = end;
        this.nodes = Sets.newHashSet();
        this.heuristicFunction = heuristicFunction;
        this.moveFunction = moveFunction;
    }

    public CostedLocation nextLocation() {
        openList.add(costLocation(start, null));

        while (!openList.isEmpty()) {
            final CostedLocation n = openList.peek();
            if (n.location.equals(end)) {
                return n;
            }

            neighboursOf(n.location).forEach(neighbour -> {
                final CostedLocation costedNeighbour = costLocation(neighbour, n);

                System.out.printf("Considering %s\n", costedNeighbour);
                if (!openList.contains(costedNeighbour) && !closedList.contains(costedNeighbour)) {
                    // A location that hasn't been considered before
                    openList.add(costedNeighbour);
                    System.out.printf("  Adding to the open list %s\n", openList.stream().map(CostedLocation::toString).collect(Collectors.joining(" ")));
                } else {
                    // A location that has been considered before, but we're finding a different route
                    if (closedList.contains(costedNeighbour)) {
                        closedList.remove(costedNeighbour);
                        openList.add(costedNeighbour);
                        System.out.println("  Cheaper than the previous route; removing from the closed list and adding to the open list");
                    }
                }

                openList.remove(n);
                closedList.add(n);
            });
        }

        return null;
    }

    private CostedLocation costLocation(Location location,
                                        CostedLocation parent) {
        return new CostedLocation(
                location,
                heuristicFunction.calculateCost(location, end),
                parent == null ? 0.0 : moveFunction.calculateCost(parent.location, location),
                parent);
    }

    private Set<Location> neighboursOf(Location location) {
        final int y = location.getY();
        final int x = location.getX();

        final Set<Location> locations = Sets.newHashSet(
                new Location(x - 1, y - 1), new Location(x, y - 1), new Location(x + 1, y - 1),
                new Location(x - 1, y),     /*                  */  new Location(x + 1, y),
                new Location(x - 1, y + 1), new Location(x, y + 1), new Location(x + 1, y + 1));

        return locations.stream()
                .filter(this::isInBounds)
                .collect(Collectors.toSet());
    }

    private boolean isInBounds(Location loc) {
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

    public static void main(String[] args) {

        final Pathfinder pathfinder = new Pathfinder(new Location(3, 3),
                new Location(5, 6),
                Pathfinder::chebyshevDistance,
                (start, end) -> 1.0);

        CostedLocation location = pathfinder.nextLocation();
        while (location != null) {
            System.out.println(location);
            location = location.parent;
        }

        System.out.println("Done");
    }
}
