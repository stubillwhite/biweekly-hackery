package marsrover.internal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static marsrover.internal.Bearing.*;
import static marsrover.internal.Instruction.*;
import static org.assertj.core.api.Assertions.assertThat;

class RoverTest {

    private static final Location INITIAL_LOCATION = new Location(1, 1);
    private static final Bearing INITIAL_BEARING = NORTH;

    private Rover rover;

    @BeforeEach
    public void setUp() {
        rover = new Rover(INITIAL_LOCATION, INITIAL_BEARING);
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForExecuteInstruction")
    public void executeInstructionsThenEndsInExpectedLocationAndBearing(List<Instruction> instructions,
                                                                        Location expectedLocation,
                                                                        Bearing expectedBearing) {
        instructions.forEach(x -> rover.executeInstruction(x));
        assertThat(rover.getLocation()).isEqualTo(expectedLocation);
        assertThat(rover.getBearing()).isEqualTo(expectedBearing);
    }

    private static Stream<Arguments> provideArgumentsForExecuteInstruction() {
        return Stream.of(
                Arguments.of(Collections.EMPTY_LIST, new Location(1, 1), NORTH),
                Arguments.of(Arrays.asList(MOVE_FORWARD), new Location(1, 2), NORTH),
                Arguments.of(Arrays.asList(ROTATE_RIGHT, MOVE_FORWARD), new Location(2, 1), EAST),
                Arguments.of(Arrays.asList(ROTATE_LEFT, MOVE_FORWARD), new Location(0, 1), WEST),
                Arguments.of(Arrays.asList(ROTATE_RIGHT, ROTATE_RIGHT, MOVE_FORWARD), new Location(1, 0), SOUTH)
        );
    }
}