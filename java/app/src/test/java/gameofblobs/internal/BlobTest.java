package gameofblobs.internal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static gameofblobs.internal.testcommon.TestUtils.createBlob;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommon.EqualityAndHashCodeTester.testEqualityAndHashCode;

public class BlobTest {

    private static final List<Blob> equalBlobs = Arrays.asList(
            new Blob(new Location(1, 2), 3),
            new Blob(new Location(1, 2), 3));

    private static final List<Blob> differentBlobs = Arrays.asList(
            new Blob(new Location(1, 2), 3),
            new Blob(new Location(1, 1), 3),
            new Blob(new Location(2, 2), 3),
            new Blob(new Location(1, 2), 4));

    @Test
    public void satisfiesEqualityAndHashCodeContracts() {
        testEqualityAndHashCode(equalBlobs, differentBlobs);
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForNextStateGivenNoPreyThenNoAction")
    public void nextStateGivenNoPreyThenNoAction(Blob current, List<Blob> blobs, Blob expected) {
        final World world = new World(3, 3, blobs);
        assertThat(current.nextState(world)).isEqualTo(expected);
    }

    private static Stream<Arguments> provideArgumentsForNextStateGivenNoPreyThenNoAction() {
        final Blob blob = createBlob(1, 1, 2);
        return Stream.of(
                Arguments.of(blob, List.of(blob, createBlob(0, 0, 2)), blob),
                Arguments.of(blob, List.of(blob, createBlob(0, 0, 3)), blob),
                Arguments.of(blob, List.of(blob), blob)
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForNextStateGivenPreyThenMovesTowardsPrey")
    public void nextStateGivenPreyThenMovesTowardsPrey(Blob current, List<Blob> blobs, Blob expected) {
        final World world = new World(3, 3, blobs);
        assertThat(current.nextState(world)).isEqualTo(expected);
    }

    private static Stream<Arguments> provideArgumentsForNextStateGivenPreyThenMovesTowardsPrey() {
        final Blob blob = createBlob(1, 1, 2);
        return Stream.of(
                Arguments.of(blob, List.of(blob, createBlob(0, 0, 1)), createBlob(0, 0, 2)), // Above left
                Arguments.of(blob, List.of(blob, createBlob(1, 0, 1)), createBlob(1, 0, 2)), // Above
                Arguments.of(blob, List.of(blob, createBlob(2, 0, 1)), createBlob(2, 0, 2)), // Above right
                Arguments.of(blob, List.of(blob, createBlob(2, 1, 1)), createBlob(2, 1, 2)), // Right
                Arguments.of(blob, List.of(blob, createBlob(2, 2, 1)), createBlob(2, 2, 2)), // Below right
                Arguments.of(blob, List.of(blob, createBlob(1, 2, 1)), createBlob(1, 2, 2)), // Below
                Arguments.of(blob, List.of(blob, createBlob(0, 2, 1)), createBlob(0, 2, 2)), // Below left
                Arguments.of(blob, List.of(blob, createBlob(0, 1, 1)), createBlob(0, 1, 2))  // Left
        );
    }
}
