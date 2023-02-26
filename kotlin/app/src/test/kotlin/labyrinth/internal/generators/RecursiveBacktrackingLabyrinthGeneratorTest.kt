package labyrinth.internal.generators

import assertk.assert
import assertk.assertions.isEqualTo
import labyrinth.internal.Coordinates
import labyrinth.internal.Passage
import labyrinth.internal.Room
import labyrinth.internal.visualizations.AlgorithmVisualizer
import labyrinth.internal.generators.RecursiveBacktrackingLabyrinthGenerator.State
import kotlin.test.Test

class RecursiveBacktrackingLabyrinthGeneratorTest {

    private val a = Room(Coordinates(0, 0))
    private val b = Room(Coordinates(1, 0))
    private val c = Room(Coordinates(2, 0))
    private val d = Room(Coordinates(0, 1))
    private val e = Room(Coordinates(1, 1))
    private val f = Room(Coordinates(2, 1))

    private val generator = RecursiveBacktrackingLabyrinthGenerator()

    @Test
    fun nextStateGivenUnvisitedRoomsExistAtPathEndThenVisitsNextUnvisitedRoom() {
        // Given
        //   a b
        //   d e
        // start: a, end: b, path: [d, a]
        val state = State(
            2,
            2,
            a,
            b,
            setOf(Passage(a, d)),
            setOf(a, d),
            listOf(d, a),
            AlgorithmVisualizer(),
            false
        )

        // When
        val actual = generator.nextState(state)

        // Then
        val newRoom = e
        val expected = state.copy(
            passages = state.passages.plus(Passage(d, newRoom)),
            visited = state.visited.plus(newRoom),
            path = listOf(newRoom) + state.path
        )

        assert(actual).isEqualTo(expected)
    }

    @Test
    fun nextStateGivenUnvisitedRoomsExistThenBacktracksAndVisitsNextUnvisitedRoom() {
        // Given
        //   a b c
        // start: b, end: c, path: [c, b]
        val state = State(
            3,
            1,
            b,
            c,
            setOf(Passage(b, c)),
            setOf(b, c),
            listOf(c, b),
            AlgorithmVisualizer(),
            false
        )

        // When
        val actual = generator.nextState(state)

        // Then
        val newRoom = a
        val expected = state.copy(
            passages = state.passages.plus(Passage(b, newRoom)),
            visited = state.visited.plus(newRoom),
            path = listOf(newRoom, b)
        )

        assert(actual).isEqualTo(expected)
    }

    @Test
    fun nextStateGivenNoUnvisitedRoomsExistThenComplete() {
        // Given
        //   a b c
        // start: a, end: c, path: [c, b, a]
        val state = State(
            3,
            1,
            b,
            c,
            setOf(Passage(b, c), Passage(b, a)),
            setOf(b, c, a),
            listOf(c, b, a),
            AlgorithmVisualizer(),
            false
        )

        // When
        val actual = generator.nextState(state)

        // Then
        val expected = state.copy(path = listOf(), isComplete = true)

        assert(actual).isEqualTo(expected)
    }
}