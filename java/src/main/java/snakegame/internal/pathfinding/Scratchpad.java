package snakegame.internal.pathfinding;

import snakegame.internal.Location;

import java.util.List;

public class Scratchpad {
    public static void main(String[] args) {

        final LocationPathfinder locationPathfinder = new LocationPathfinder();

        final List<Location> path = locationPathfinder.findPath(
                new Location(1, 3),
                new Location(5, 9));

        path.forEach(System.out::println);

        System.out.println("Done");
    }
}
