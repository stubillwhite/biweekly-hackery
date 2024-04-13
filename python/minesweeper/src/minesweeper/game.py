from enum import Enum, auto
from typing import Callable, Iterator

from minesweeper.grid import (
    Grid,
    Hidden,
    Location,
    Empty,
    Mine,
    Cell,
    Flagged,
    Ping,
)


class GameState(Enum):
    IN_PROGRESS = auto()
    WON = auto()
    LOST = auto()


class Game:

    def __init__(self, grid: Grid):
        def is_mine(cell: Cell) -> bool:
            match cell:
                case Mine() | Hidden(Mine()) | Flagged(Hidden(Mine())):
                    return True
                case _:
                    return False

        def is_flagged(cell: Cell) -> bool:
            match cell:
                case Flagged(_):
                    return True
                case _:
                    return False

        self.grid = grid
        self._mine_locations = set(self._locations_matching(is_mine))
        self._flag_locations = set(self._locations_matching(is_flagged))
        self.flag_count = len(self._flag_locations)
        self.mine_count = len(self._mine_locations)
        self.game_state = GameState.IN_PROGRESS

    def flag(self, loc: Location) -> None:
        x, y = loc

        match self.grid.cells[x][y]:
            case Flagged(content):
                self.grid.cells[x][y] = content
                self._flag_locations.remove(loc)
                self.flag_count -= 1

            case content:
                self.grid.cells[x][y] = Flagged(content)
                self._flag_locations.add(loc)
                self.flag_count += 1

        if self._flag_locations == self._mine_locations:
            self.game_state = GameState.WON

    def reveal(self, loc: Location) -> None:
        checked = set()
        to_check = set()

        to_check.add(loc)
        while to_check:
            loc = to_check.pop()
            x, y = loc

            checked.add(loc)

            match self.grid.cells[x][y]:
                case Hidden(Empty()):
                    self._reveal_location(loc)
                    new_to_check = [adj_loc for adj_loc in self.grid.adjacent_locations(loc) if adj_loc not in checked]
                    to_check.update(new_to_check)

                case Hidden(Ping()):
                    self._reveal_location(loc)

                case Hidden(Mine()):
                    self._reveal_location(loc)
                    self.game_state = GameState.LOST

    def _reveal_location(self, loc: Location) -> None:
        (x, y) = loc

        match self.grid.cells[x][y]:
            case Hidden(content):
                self.grid.cells[x][y] = content

    def _locations_matching(self, predicate: Callable[[Cell], bool]) -> Iterator[Location]:
        for x in range(self.grid.width):
            for y in range(self.grid.height):
                if predicate(self.grid.cells[x][y]):
                    yield x, y
