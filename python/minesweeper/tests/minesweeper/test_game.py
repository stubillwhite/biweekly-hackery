from minesweeper.grid import Flagged
from tests.minesweeper.testcommon import game_with_mine_pattern, grid_with_content
from minesweeper.game import GameState


def test_reveal_given_location_is_empty_then_recursively_reveals_all_connected_empty_cells():
    # Given
    game = game_with_mine_pattern(
        """_____
          |_____
          |__*__
          |_____
          |_____"""
    )

    # When
    game.reveal((0, 0))

    # Then
    expected_grid = grid_with_content(
        """_____
          |_111_
          |_1*1_
          |_111_
          |_____"""
    )

    assert game.grid.cells == expected_grid.cells
    assert game.game_state == GameState.IN_PROGRESS


def test_reveal_given_location_contains_mine_then_game_is_lost():
    # Given
    game = game_with_mine_pattern(
        """*_
          |__"""
    )

    # When
    game.reveal((0, 0))

    # Then
    assert game.game_state == GameState.LOST


def test_flag_given_no_flag_exists_in_location_then_sets_flag():
    # Given
    game = game_with_mine_pattern(
        """*_
          |__"""
    )

    # When
    game.flag((1, 0))

    # Then
    assert game.flag_count == 1
    assert isinstance(game.grid.cells[1][0], Flagged)


def test_flag_given_flag_exists_in_location_then_clears_flag():
    # Given
    game = game_with_mine_pattern(
        """*_
          |__"""
    )
    game.flag((1, 0))

    # When
    game.flag((1, 0))

    # Then
    assert game.flag_count == 0
    assert not isinstance(game.grid.cells[1][0], Flagged)


def test_flag_given_other_mines_exist_then_game_continues():
    # Given
    game = game_with_mine_pattern(
        """*_
          |_*"""
    )

    # When
    game.flag((0, 0))

    # Then
    assert game.game_state == GameState.IN_PROGRESS


def test_flag_given_other_mines_exist_then_game_is_won():
    # Given
    game = game_with_mine_pattern(
        """*_
          |_*"""
    )

    # When
    game.flag((0, 0))
    game.flag((1, 1))

    # Then
    assert game.game_state == GameState.WON
