from dataclasses import dataclass
from random import shuffle

type Location = tuple[int, int]  # type: ignore[valid-type]


@dataclass
class Cell:
    _is_abstract = True

    def __init__(self):
        if self._is_abstract:
            raise RuntimeError("Abstract class instantiation.")

    def __init_subclass__(cls):
        cls._is_abstract = False


@dataclass
class Empty(Cell):
    pass


@dataclass
class Mine(Cell):
    pass


@dataclass
class Ping(Cell):
    mine_count: int


@dataclass
class Hidden(Cell):
    content: Cell


@dataclass
class Flagged(Cell):
    content: Cell


class Grid:

    def __init__(self, width: int, height: int, mines: int):
        self.width = width
        self.height = height

        self._cells: list[list[Cell]] = [[Empty() for y in range(height)] for x in range(width)]
        self._valid_locations = [(x, y) for x in range(width) for y in range(height)]

        self._init_mines(mines)
        self._init_pings()
        self._hide_cell_content()

    @property
    def cells(self) -> list[list[Cell]]:
        return self._cells

    def adjacent_locations(self, loc: Location) -> list[Location]:
        (x, y) = loc

        adj_locs = [(x + dx, y + dy) for dx in range(-1, 2) for dy in range(-1, 2)]

        return [adj for adj in adj_locs if adj in self._valid_locations and adj != (x, y)]

    def _init_mines(self, mines: int) -> None:
        mine_locations = self._valid_locations.copy()
        shuffle(mine_locations)

        for x, y in mine_locations[:mines]:
            self._cells[x][y] = Mine()

    def _init_pings(self) -> None:
        for loc in self._valid_locations:
            x, y = loc
            match self.cells[x][y]:
                case Empty():
                    mine_count = sum(
                        map(
                            lambda x: isinstance(self._cells[x[0]][x[1]], Mine),
                            self.adjacent_locations(loc),
                        )
                    )

                    if mine_count > 0:
                        self._cells[x][y] = Ping(mine_count)

    def _hide_cell_content(self) -> None:
        for x, y in self._valid_locations:
            self._cells[x][y] = Hidden(self._cells[x][y])
