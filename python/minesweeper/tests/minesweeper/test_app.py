from io import StringIO
from unittest.mock import PropertyMock, patch

from minesweeper.app import play_game
from minesweeper.game import GameState, Game


def test_play_game_given_game_won_then_exits():
    with (
        patch("minesweeper.app.game_to_str", return_value="mock-display") as mock_display,
        patch("minesweeper.app.Game", spec=Game) as mock_game,
    ):
        type(mock_game).game_state = PropertyMock(side_effect=[GameState.WON, GameState.WON])

        play_game(mock_game)
        mock_display.assert_called_with(mock_game)


def test_play_game_given_game_lost_then_exits():
    with (
        patch("minesweeper.app.game_to_str", return_value="mock-display") as mock_display,
        patch("minesweeper.app.Game", spec=Game) as mock_game,
    ):
        type(mock_game).game_state = PropertyMock(side_effect=[GameState.LOST, GameState.LOST])

        play_game(mock_game)
        mock_display.assert_called_with(mock_game)


def test_play_game_given_reveal_then_reveals_selection():
    with (
        patch("minesweeper.app.game_to_str", return_value="mock-display") as mock_display,
        patch("sys.stdin", StringIO("r 0 0")),
        patch("minesweeper.app.Game", spec=Game) as mock_game,
        patch("minesweeper.app.Game.reveal") as mock_reveal,
    ):
        type(mock_game).game_state = PropertyMock(side_effect=[GameState.IN_PROGRESS, GameState.WON, GameState.WON])

        play_game(mock_game)
        mock_display.assert_called_with(mock_game)
        mock_reveal.assert_called_with((0, 0))


def test_play_game_given_flag_then_flags_selection():
    with (
        patch("minesweeper.app.game_to_str", return_value="mock-display") as mock_display,
        patch("sys.stdin", StringIO("f 0 0")),
        patch("minesweeper.app.Game", spec=Game) as mock_game,
        patch("minesweeper.app.Game.flag") as mock_flag,
    ):
        type(mock_game).game_state = PropertyMock(side_effect=[GameState.IN_PROGRESS, GameState.WON, GameState.WON])

        play_game(mock_game)
        mock_display.assert_called_with(mock_game)
        mock_flag.assert_called_with((0, 0))
