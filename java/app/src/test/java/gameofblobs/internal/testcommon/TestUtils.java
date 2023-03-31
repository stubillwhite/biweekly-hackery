package gameofblobs.internal.testcommon;

import gameofblobs.internal.Blob;
import gameofblobs.internal.Location;
import gameofblobs.internal.World;

import java.util.List;

public class TestUtils {

    public static Blob createBlob(int x, int y, int size) {
        return new Blob(createLocation(x, y), size);
    }

    public static Location createLocation(int x, int y) {
        return new Location(x, y);
    }

    public static World createWorld(int width, int height, List<Blob> blobs) {
        return new World(width, height, blobs);
    }
}
