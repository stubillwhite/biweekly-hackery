from minesweeper.game import Game
from minesweeper.grid import Grid, Hidden, Mine, Empty, Ping, Flagged


def strip_margin(s: str) -> str:
    lines = s.splitlines()
    indent = lines[1].index("|")
    stripped = [lines[0]] + [line[indent + 1 :] for line in lines[1:]]
    return "\n".join(stripped)


def game_to_str(game: Game) -> str:
    s = "--\n"
    s += f"{_grid_to_str(game.grid)}\n"
    s += strip_margin(
        f"""Mines: {game.mine_count}
           |Flags: {game.flag_count}
           |
           |Actions:
           |  r 1 2 - Reveal location 1 2
           |  f 1 2 - Toggle flag on location 1 2
           |"""
    )
    return s


def _grid_to_str(grid: Grid) -> str:
    s = ""
    for y in range(grid.height):
        for x in range(grid.width):
            match grid.cells[x][y]:
                case Flagged():
                    s += "?"
                case Hidden():
                    s += "."
                case Mine():
                    s += "*"
                case Empty():
                    s += " "
                case Ping(mine_count):
                    s += str(mine_count)
        s += "\n"
    return s
