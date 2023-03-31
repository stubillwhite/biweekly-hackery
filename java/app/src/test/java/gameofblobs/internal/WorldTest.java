package gameofblobs.internal;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static gameofblobs.internal.testcommon.TestUtils.createBlob;
import static gameofblobs.internal.testcommon.TestUtils.createWorld;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommon.EqualityAndHashCodeTester.testEqualityAndHashCode;

public class WorldTest {

    private static final List<Blob> blobsOne = Lists.newArrayList(
            createBlob(1, 1, 1),
            createBlob(2, 2, 2),
            createBlob(3, 3, 3));

    private static final List<Blob> blobsTwo = Lists.newArrayList(
            createBlob(2, 2, 2),
            createBlob(3, 3, 3),
            createBlob(4, 4, 4));

    private static final List<World> equalWorlds = Arrays.asList(
            createWorld(5, 10, Lists.newArrayList(blobsOne)),
            createWorld(5, 10, Lists.newArrayList(blobsOne)));

    private static final List<World> differentWorlds = Arrays.asList(
            createWorld(5, 10, Lists.newArrayList(blobsOne)),
            createWorld(1, 10, Lists.newArrayList(blobsOne)),
            createWorld(5, 10, Lists.newArrayList(blobsTwo)));

    @Test
    public void satisfiesEqualityAndHashCodeContracts() {
        testEqualityAndHashCode(equalWorlds, differentWorlds);
    }

    @Test
    public void nextStateThenUpdatesAllBlobs() {
        // Given
        final World world = createWorld(5, 5, Lists.newArrayList(
                createBlob(1, 1, 1),
                createBlob(3, 3, 2)));

        // When
        final World actual = world.nextState();

        // Then
        assertThat(actual).isEqualTo(createWorld(5, 5, Lists.newArrayList(
                createBlob(1, 1, 1),
                createBlob(2, 2, 2))));
    }

    @Test
    public void nextStateThenMergesCoLocatedBlobs() {
        // Given

        final World world = createWorld(5, 5, Lists.newArrayList(
                createBlob(1, 1, 1),
                createBlob(2, 2, 2)));

        // When
        final World actual = world.nextState();

        // Then
        assertThat(actual).isEqualTo(createWorld(5, 5, Lists.newArrayList(
                createBlob(1, 1, 3))));
    }
}
