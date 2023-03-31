package gameofblobs.internal;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimaps;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class World {

    private final int width;
    private final int height;
    private final List<Blob> blobs;

    public World(int width, int height, List<Blob> blobs) {
        this.width = width;
        this.height = height;
        this.blobs = blobs;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Blob> getBlobs() {
        return blobs;
    }

    public World nextState() {
        final List<Blob> newBlobs = blobs.stream()
                .map(x -> x.nextState(this))
                .collect(Collectors.toList());

        return new World(width, height, mergeBlobs(newBlobs));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        World world = (World) o;
        return width == world.width &&
                height == world.height &&
                (blobs.containsAll(world.blobs) && world.blobs.containsAll(blobs));
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, blobs);
    }

    @Override
    public String toString() {
        return "World{" +
                "width=" + width +
                ", height=" + height +
                ", blobs=" + blobs +
                '}';
    }

    private List<Blob> mergeBlobs(List<Blob> blobs) {
        final ImmutableListMultimap<Location, Blob> blobsByLocation = Multimaps.index(blobs, Blob::getLocation);

        return blobsByLocation.asMap().entrySet().stream()
                .map(entry -> new Blob(entry.getKey(), entry.getValue().stream().mapToInt(Blob::getSize).sum()))
                .collect(Collectors.toList());
    }
}
