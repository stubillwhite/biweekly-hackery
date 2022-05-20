package marsrover.internal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InstructionTest {

    @ParameterizedTest
    @MethodSource("provideArgumentsForFromCharacterCode")
    public void fromCharacterCodeGivenValidCharacterCodeThenDecodes(String characterCode, Instruction expected) {
        assertThat(Instruction.fromCharacterCode(characterCode)).isEqualTo(expected);
    }

    private static Stream<Arguments> provideArgumentsForFromCharacterCode() {
        return Stream.of(
                Arguments.of("L", Instruction.ROTATE_LEFT),
                Arguments.of("R", Instruction.ROTATE_RIGHT),
                Arguments.of("M", Instruction.MOVE_FORWARD));
    }

    @Test
    public void fromCharacterCodeGivenInvalidCharacterCodeThenThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            Instruction.fromCharacterCode("INVALID");
        });
    }
}
