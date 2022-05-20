package marsrover.internal;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class InputParserTest {

    @Test
    public void parseGridGivenValidGridLineThenReturnsGrid() {
        // Given
        final String line = "10 20\n";

        // When
        final Grid grid = new InputParser().parseGrid(line);

        // Then
        assertThat(grid.getWidth()).isEqualTo(11);
        assertThat(grid.getHeight()).isEqualTo(21);
    }

    @Test
    public void parseRoverGivenValidRoverLineThenReturnsRover() {
        // Given
        final String line = "12 13 E";

        // When
        final Rover rover = new InputParser().parseRover(line);

        // Then
        assertThat(rover.getLocation()).isEqualTo(new Location(12, 13));
        assertThat(rover.getBearing()).isEqualTo(Bearing.EAST);
    }

    @Test
    public void parseInstructionsGivenValidInstructionsThenReturnsInstructions() {
        // Given
        final String lines = "LMR";

        // When
        final List<Instruction> instructions = new InputParser().parseInstructions(lines);

        // Then
        assertThat(instructions).containsExactly(Instruction.ROTATE_LEFT, Instruction.MOVE_FORWARD, Instruction.ROTATE_RIGHT);
    }
}
