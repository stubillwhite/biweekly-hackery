package marsrover.internal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DisplayTest {

    @Test
    public void renderGridGivenNoRoversThenReturnsStringRepresentingGrid() {
        // Given
        final Grid grid = new Grid(new Location(1, 2));
        final Display display = new Display(grid);

        // When, Then
        assertThat(display.renderGrid()).isEqualTo("..\n..\n..\n");
    }

    @Test
    public void renderGridGivenRoversThenReturnsStringRepresentingGridAndRovers() {
        // Given
        final Grid grid = new Grid(new Location(2, 2));
        grid.addRover(new Rover(new Location(1, 2), Bearing.NORTH));
        grid.addRover(new Rover(new Location(0, 1), Bearing.WEST));
        grid.addRover(new Rover(new Location(2, 1), Bearing.EAST));
        grid.addRover(new Rover(new Location(1, 0), Bearing.SOUTH));
        final Display display = new Display(grid);

        // When, Then
        assertThat(display.renderGrid()).isEqualTo(".^.\n<.>\n.v.\n");
    }
}
