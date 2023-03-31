package gameofblobs.internal;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Display {

    public void display(World world) {
        final Map<Location, Blob> blobsByLocation =
                Maps.uniqueIndex(world.getBlobs(), Blob::getLocation);

        final String asString =
                IntStream.range(0, world.getHeight()).boxed().map(y ->
                        IntStream.range(0, world.getWidth()).boxed().map(x -> characterForLocation(blobsByLocation, x, y)
                                ).collect(Collectors.joining()) + "\n"
                ).collect(Collectors.joining());

        System.out.println(asString);
        System.out.printf("\033[%dA", world.getHeight()+1);
    }

    private String characterForLocation(Map<Location, Blob> blobsByLocation, Integer x, Integer y) {
        final Location location = new Location(x, y);
        if (blobsByLocation.containsKey(location)) {
            return String.format("%2d ", blobsByLocation.get(location).getSize());
        } else {
            return ".. ";
        }
    }
}
