from minesweeper.display import strip_margin
from minesweeper.game import Game
from minesweeper.grid import Grid, Cell, Hidden, Empty, Mine, Ping


class StubGrid(Grid):

    @staticmethod
    def with_mine_pattern(s) -> Grid:
        def parse_char(ch: str) -> Cell:
            match ch:
                case "_":
                    return Empty()
                case "*":
                    return Mine()
                case _:
                    raise ValueError("Only empty or mine cells expected")

        def parse_row(row: list[str]) -> list[Cell]:
            return [parse_char(ch) for ch in row]

        chars = [list(row) for row in s.splitlines()]
        cells = [parse_row(row) for row in chars]

        width = len(cells[0])
        height = len(cells)

        grid = StubGrid(width, height, 0)

        grid._cells = cells
        grid._valid_locations = [(x, y) for x in range(width) for y in range(height)]

        grid._init_pings()
        grid._hide_cell_content()

        return grid

    @staticmethod
    def from_string(s) -> Grid:
        def parse_char(ch: str) -> Cell:
            match ch:
                case "_":
                    return Empty()
                case ".":
                    return Hidden(Empty())
                case "!":
                    return Mine()
                case "*":
                    return Hidden(Mine())
                case n:
                    return Ping(int(n))

        def parse_row(row: list[str]) -> list[Cell]:
            return [parse_char(ch) for ch in row]

        chars = [list(row) for row in s.splitlines()]
        cells = [parse_row(row) for row in chars]

        return StubGrid.with_cells(cells)

    @staticmethod
    def with_cells(cells: list[list[Cell]]) -> Grid:
        width = len(cells)
        height = len(cells[0])

        grid = StubGrid(width, height, 0)

        grid._cells = cells
        grid._valid_locations = [(x, y) for x in range(width) for y in range(height)]

        return grid


def grid_with_content(s: str) -> Grid:
    cleaned_str = strip_margin(s)
    return StubGrid.from_string(cleaned_str)


def grid_with_cells(cells: list[list[Cell]]) -> Grid:
    return StubGrid.with_cells(cells)


def grid_with_mine_pattern(s: str) -> Grid:
    cleaned_str = strip_margin(s)
    return StubGrid.with_mine_pattern(cleaned_str)


def game_with_mine_pattern(s: str) -> Game:
    return Game(grid_with_mine_pattern(s))
