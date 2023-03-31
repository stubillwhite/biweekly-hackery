package marsrover.internal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BearingTest {

    @ParameterizedTest
    @MethodSource("provideArgumentsForFromCharacterCode")
    public void fromCharacterCodeGivenValidCharacterCodeThenDecodes(String characterCode, Bearing expected) {
        assertThat(Bearing.fromCharacterCode(characterCode)).isEqualTo(expected);
    }

    private static Stream<Arguments> provideArgumentsForFromCharacterCode() {
        return Stream.of(
                Arguments.of("N", Bearing.NORTH),
                Arguments.of("E", Bearing.EAST),
                Arguments.of("S", Bearing.SOUTH),
                Arguments.of("W", Bearing.WEST));
    }

    @Test
    public void fromCharacterCodeGivenInvalidCharacterCodeThenThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            Bearing.fromCharacterCode("INVALID");
        });
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForGetCharacterCode")
    public void getCharacterCodeThenReturnsCharacterCode(Bearing bearing, String expected) {
        assertThat(bearing.getCharacterCode()).isEqualTo(expected);
    }

    private static Stream<Arguments> provideArgumentsForGetCharacterCode() {
        return Stream.of(
                Arguments.of(Bearing.NORTH, "N"),
                Arguments.of(Bearing.EAST, "E"),
                Arguments.of(Bearing.SOUTH, "S"),
                Arguments.of(Bearing.WEST, "W"));
    }
}
