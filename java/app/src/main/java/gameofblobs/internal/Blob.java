package gameofblobs.internal;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Blob {
    private final Location location;
    private final int size;

    public Blob(Location location, int size) {
        this.location = location;
        this.size = size;
    }

    public Location getLocation() {
        return location;
    }

    public int getSize() {
        return size;
    }

    public Blob nextState(World world) {
        return locatePrey(world.getBlobs())
                .map(this::moveTowards)
                .orElse(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Blob blob = (Blob) o;
        return size == blob.size &&
                Objects.equals(location, blob.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, size);
    }

    @Override
    public String toString() {
        return "Blob{" +
                "location=" + location +
                ", size=" + size +
                '}';
    }

    private Optional<Blob> locatePrey(List<Blob> blobs) {
        final Comparator<Blob> byDistance =
                Comparator.comparing(x -> distance(location, x.getLocation()));

        return blobs.stream()
                .filter(x -> this != x)
                .filter(x -> size > x.getSize())
                .min(byDistance);
    }

    private double distance(Location a, Location b) {
        return sqrt(pow(a.getX() - b.getX(), 2.0) + pow(a.getY() - b.getY(), 2.0));
    }

    private Blob moveTowards(Blob prey) {
        final Location preyLocation = prey.getLocation();

        final Location newLocation = new Location(
                oneStepTowards(location.getX(), preyLocation.getX()),
                oneStepTowards(location.getY(), preyLocation.getY())
        );

        return new Blob(newLocation, size);
    }

    private int oneStepTowards(int blobCoordinate, int preyCoordinate) {
        if (blobCoordinate > preyCoordinate) return blobCoordinate - 1;
        else if (blobCoordinate < preyCoordinate) return blobCoordinate + 1;
        else return blobCoordinate;
    }
}
