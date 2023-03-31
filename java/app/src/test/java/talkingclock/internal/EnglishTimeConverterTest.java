package talkingclock.internal;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class EnglishTimeConverterTest {

    private final EnglishTimeConverter converter = new EnglishTimeConverter();

    @ParameterizedTest
    @MethodSource("provideArgumentsForFOO")
    public void executeInstructionsThenEndsInExpectedLocationAndBearing(String timeStr,
                                                                        String expectedEnglishStr) {
        assertThat(converter.convertToEnglish(timeStr)).isEqualTo(expectedEnglishStr);
    }

    private static Stream<Arguments> provideArgumentsForFOO() {
        return Stream.of(
                Arguments.of("00:00", "It is twelve a.m."),
                Arguments.of("01:30", "It is one thirty a.m."),
                Arguments.of("12:05", "It is twelve oh five p.m."),
                Arguments.of("14:01", "It is two oh one p.m."),
                Arguments.of("20:29", "It is eight twenty nine p.m."),
                Arguments.of("21:00", "It is nine p.m.")
        );
    }

}