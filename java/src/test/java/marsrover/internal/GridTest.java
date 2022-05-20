package marsrover.internal;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GridTest {

    @Test
    public void getWidthThenReturnsWidth() {
        // Given
        final Grid grid = new Grid(new Location(2, 3));

        // When, Then
        assertThat(grid.getWidth()).isEqualTo(3);
    }

    @Test
    public void getHeightThenReturnsHeight() {
        // Given
        final Grid grid = new Grid(new Location(2, 3));

        // When, Then
        assertThat(grid.getHeight()).isEqualTo(4);
    }

    @Test
    public void getRoversGivenNoRoversAddedThenReturnsEmptyList() {
        // Given
        final Grid grid = new Grid(new Location(2, 3));

        // When, Then
        assertThat(grid.getRovers()).isEqualTo(Collections.EMPTY_LIST);
    }

    @Test
    public void getRoversGivenRoversAddedThenReturnsRovers() {
        // Given
        final Grid grid = new Grid(new Location(2, 3));
        final Rover rover1 = new Rover(new Location(0, 0), Bearing.NORTH);
        final Rover rover2 = new Rover(new Location(1, 1), Bearing.EAST);
        grid.addRover(rover1);
        grid.addRover(rover2);

        // When, Then
        assertThat(grid.getRovers()).containsExactly(rover1, rover2);
    }
}
