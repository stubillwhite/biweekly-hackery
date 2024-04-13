import re

from minesweeper.display import game_to_str
from minesweeper.game import Game, GameState
from minesweeper.grid import Grid


def main() -> None:
    grid = Grid(10, 10, 2)
    game = Game(grid)
    play_game(game)


def play_game(game: Game) -> None:
    while game.game_state == GameState.IN_PROGRESS:
        print(game_to_str(game))

        match = None
        while not match:
            user_input = input("Enter action: ")
            match = re.match(r"(r|f) (\d+) (\d+)", user_input)

        (action, x_str, y_str) = match.groups()

        x = int(x_str)
        y = int(y_str)

        match action:
            case "r":
                game.reveal((x, y))

            case "f":
                game.flag((x, y))

    message = "You win!" if game.game_state == GameState.WON else "You lose!"

    print(game_to_str(game))
    print()
    print(message)
