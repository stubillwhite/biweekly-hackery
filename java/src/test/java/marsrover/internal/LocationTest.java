package marsrover.internal;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static testcommon.EqualityAndHashCodeTester.testEqualityAndHashCode;

class LocationTest {

    private static final List<Location> equalLocations = Arrays.asList(
            new Location(1, 2),
            new Location(1, 2));

    private static final List<Location> differentLocations = Arrays.asList(
            new Location(1, 1),
            new Location(1, 2),
            new Location(2, 2));

    @Test
    public void satisfiesEqualityAndHashCodeContracts() {
        testEqualityAndHashCode(equalLocations, differentLocations);
    }
}