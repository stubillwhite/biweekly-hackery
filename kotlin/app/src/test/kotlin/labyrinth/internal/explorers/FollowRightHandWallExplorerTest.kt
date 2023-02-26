package labyrinth.internal.explorers

import assertk.assert
import assertk.assertions.isEqualTo
import labyrinth.internal.Coordinates
import labyrinth.internal.Labyrinth
import labyrinth.internal.Passage
import labyrinth.internal.Room
import labyrinth.internal.explorers.FollowRightHandWallExplorer.Orientation
import labyrinth.internal.explorers.FollowRightHandWallExplorer.Orientation.*
import labyrinth.internal.explorers.FollowRightHandWallExplorer.State
import labyrinth.internal.visualizations.AlgorithmVisualizer
import kotlin.test.Test


class FollowRightHandWallExplorerTest {

    // a b
    // Start: a, End: b

    private val a = Room(Coordinates(0, 0))
    private val b = Room(Coordinates(1, 0))

    private val labyrinth = Labyrinth(2, 1, setOf(Passage(a, b)), a, b)

    private val explorer = FollowRightHandWallExplorer()

    @Test
    fun nextStateGivenWallAheadThenTurnsLeft() {
        // Given
        val state = createStateFacing(West)

        // When
        val actual = explorer.nextState(state)

        // Then
        val expected = state.copy(orientation = South)
        assert(actual).isEqualTo(expected)
    }

    @Test
    fun nextStateGivenSpaceAheadThenMovesForward() {
        // Given
        val state = createStateFacing(East)

        // When
        val actual = explorer.nextState(state)

        // Then
        val expected = state.copy(orientation = East, currentRoom = b, path = listOf(a, b), isComplete = false)
        assert(actual).isEqualTo(expected)
    }

    @Test
    fun nextStateGivenSpaceOnTheRightThenMovesRight() {
        // Given
        val state = createStateFacing(North)

        // When
        val actual = explorer.nextState(state)

        // Then
        val expected = state.copy(orientation = East, currentRoom = b, path = listOf(a, b))
        assert(actual).isEqualTo(expected)
    }

    @Test
    fun nextStateGivenAtExitThenIsComplete() {
        // Given
        val state = createStateFacing(East).copy(currentRoom = b, path = listOf(a, b))

        // When
        val actual = explorer.nextState(state)

        // Then
        val expected = state.copy(isComplete = true)
        assert(actual).isEqualTo(expected)
    }

    private fun createStateFacing(orientation: Orientation): State {
        val state = State(
            labyrinth,
            a,
            orientation,
            listOf(a),
            AlgorithmVisualizer(),
            false
        )
        return state
    }
}