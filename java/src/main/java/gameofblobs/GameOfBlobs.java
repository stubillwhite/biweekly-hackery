package gameofblobs;

import com.google.common.collect.Lists;
import gameofblobs.internal.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GameOfBlobs {

    public static void main(String[] args) {
        final int width = 40;
        final int height = 40;

        final List<Blob> blobs = randomBlobs(width, height, 50);
        final World world = new World(width, height, blobs);

        final Display display = new Display();
        final Simulation simulation = new Simulation(world, display);
        simulation.run();
    }

    private static List<Blob> randomBlobs(int width, int height, int count) {
        final List<Location> locations = Lists.newArrayList();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                locations.add(new Location(x, y));
            }
        }

        Collections.shuffle(locations);

        final Random random = new Random();
        return locations.stream()
                .limit(count)
                .map(x -> new Blob(x, 1 + random.nextInt(2)))
                .collect(Collectors.toList());
    }
}
