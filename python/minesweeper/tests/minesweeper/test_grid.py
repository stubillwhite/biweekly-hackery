from minesweeper.grid import Grid, Cell, Hidden, Mine
from tests.minesweeper.testcommon import grid_with_mine_pattern, grid_with_content


def test_grid_then_creates_grid_with_specified_attributes():
    def is_mine(cell: Cell) -> bool:
        match cell:
            case Hidden(Mine()):
                return True
            case _:
                return False

    grid = Grid(5, 10, 3)

    assert grid.width == 5
    assert grid.height == 10
    assert len(grid.cells) == 5
    assert len(grid.cells[0]) == 10

    mines = [cell for row in grid.cells for cell in row if is_mine(cell)]
    assert len(mines) == 3


def test_grid_then_calculates_pings_correctly():
    grid = grid_with_mine_pattern(
        """_____
          |_____
          |__*__
          |___*_
          |_____"""
    )

    expected_grid = grid_with_content(
        """_____
          |_111_
          |_1!21
          |_12!1
          |__111"""
    )
    expected_grid._hide_cell_content()

    assert grid.cells == expected_grid.cells


def test_adjacent_locations_given_all_valid_then_returns_all():
    grid = Grid(5, 10, 3)

    expected = [(2, 2), (2, 3), (2, 4), (3, 2), (3, 4), (4, 2), (4, 3), (4, 4)]
    assert grid.adjacent_locations((3, 3)) == expected


def test_adjacent_locations_given_at_edge_then_returns_only_valid_locations():
    grid = Grid(5, 10, 3)

    expected = [(0, 1), (1, 0), (1, 1)]
    assert grid.adjacent_locations((0, 0)) == expected
