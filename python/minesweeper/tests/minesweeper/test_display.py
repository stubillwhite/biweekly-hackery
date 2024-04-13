from minesweeper.display import game_to_str, strip_margin
from minesweeper.game import Game
from minesweeper.grid import Flagged, Hidden, Mine, Ping, Empty
from tests.minesweeper.testcommon import grid_with_cells


def test_strip_margin_then_strips_up_to_delimiter():
    # Given, When
    actual = strip_margin(
        """Foo
          |Bar
          |Baz"""
    )

    # Then
    assert actual == "Foo\nBar\nBaz"


def test_game_to_str_then_converts_game_to_ascii_string():
    # Given
    grid = grid_with_cells(
        flip_xy(
            [
                [Empty(), Mine(), Ping(1)],
                [Hidden(Empty()), Hidden(Mine()), Hidden(Ping(1))],
                [Flagged(Hidden(Empty())), Flagged(Hidden(Mine())), Flagged(Hidden(Ping(1)))],
            ]
        )
    )
    game = Game(grid)

    # When
    actual = game_to_str(game)

    # Then
    expected = strip_margin(
        """--
          | *1
          |...
          |???
          |
          |Mines: 3
          |Flags: 3
          |
          |Actions:
          |  r 1 2 - Reveal location 1 2
          |  f 1 2 - Toggle flag on location 1 2
          |"""
    )
    assert actual == expected


def flip_xy[T](cells: list[list[T]]) -> list[list[T]]:  # type: ignore[valid-type, name-defined]
    width = len(cells[0])

    flipped = []
    for x in range(width):
        col = [row[x] for row in cells]
        flipped.append(col)

    return flipped
