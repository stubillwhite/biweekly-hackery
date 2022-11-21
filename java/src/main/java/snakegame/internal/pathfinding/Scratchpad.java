package snakegame.internal.pathfinding;

import snakegame.internal.Location;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Scratchpad {
    public static void main(String[] args) {

        IntStream.range(1, 10).boxed()
                .filter(x -> x % 2 == 0)
                .map(x -> x + 1)
                .collect(Collectors.toList());


//        final LocationPathfinder locationPathfinder = new LocationPathfinder();
//
//        final List<Location> path = locationPathfinder.findPath(
//                new Location(1, 3),
//                new Location(5, 9));
//
//        path.forEach(System.out::println);
//
//        System.out.println("Done");
    }
}
