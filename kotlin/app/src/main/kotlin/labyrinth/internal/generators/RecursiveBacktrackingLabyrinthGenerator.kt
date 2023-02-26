package labyrinth.internal.generators

import labyrinth.internal.*
import labyrinth.internal.visualizations.AlgorithmVisualizer
import kotlin.random.Random

class RecursiveBacktrackingLabyrinthGenerator : LabyrinthGenerator {

    data class State(
        val width: Int,
        val height: Int,
        val start: Room,
        val end: Room,
        val passages: Set<Passage>,
        val visited: Set<Room>,
        val path: List<Room>,
        val visualizer: AlgorithmVisualizer,
        val isComplete: Boolean
    )

    override fun generateLabyrinth(width: Int, height: Int, algorithmVisualizer: AlgorithmVisualizer): Labyrinth {
        val start = randomRoom(width, height)
        val end = generateSequence { randomRoom(width, height) }.dropWhile { it == start }.first()

        val initialState =
            State(width, height, start, end, setOf(), setOf(start), listOf(start), algorithmVisualizer, false)

        val finalState =
            generateSequence(initialState) { nextState(it) }
                .dropWhile { !it.isComplete }
                .first()

        return toLabyrinth(finalState)
    }

    private fun randomRoom(width: Int, height: Int): Room {
        return Room(Coordinates(Random.nextInt(width), Random.nextInt(height)))
    }

    internal fun nextState(state: State): State {
        val newState = removeVisitedRoomsFromPath(state)

        val nextRoomOnPath = nextRoomOnPathWithUnvisitedNeighbours(newState)

        return if (nextRoomOnPath == null) {
            visualizeState(newState)
            newState.copy(isComplete = true)
        } else {
            val roomToVisit = selectRandomUnvisitedNeighbour(newState, nextRoomOnPath)

            val newPassages = newState.passages + Passage(nextRoomOnPath, roomToVisit)
            val newVisited = newState.visited + roomToVisit
            val newPath = listOf(roomToVisit) + newState.path

            visualizeState(newState)
            newState.copy(passages = newPassages, visited = newVisited, path = newPath)
        }
    }

    private fun removeVisitedRoomsFromPath(state: State): State {
        val newPath = state.path.dropWhile { unvisitedNeighbours(state, it).isEmpty() }
        return state.copy(path = newPath)
    }

    private fun visualizeState(state: State) {
        state.visualizer.visualize(toLabyrinth(state), state.path)
    }

    private fun nextRoomOnPathWithUnvisitedNeighbours(state: State): Room? {
        val newPath = state.path.dropWhile { unvisitedNeighbours(state, it).isEmpty() }
        return newPath.getOrNull(0)
    }

    private fun selectRandomUnvisitedNeighbour(state: State, room: Room): Room {
        return unvisitedNeighbours(state, room).shuffled().first()
    }

    private fun isWithinBounds(state: State, room: Room): Boolean {
        val (x, y) = room.coordinates
        return (x in 0 until state.width) && (y in 0 until state.height)
    }

    private fun neighbours(state: State, room: Room): Set<Room> {
        val deltas = setOf(Coordinates(0, -1), Coordinates(1, 0), Coordinates(0, 1), Coordinates(-1, 0))

        return deltas
            .map { Room(room.coordinates.plus(it)) }
            .filter { isWithinBounds(state, it) }
            .toSet()
    }

    private fun unvisitedNeighbours(state: State, room: Room): Set<Room> {
        return neighbours(state, room).subtract(state.visited)
    }

    private fun toLabyrinth(state: State): Labyrinth {
        return Labyrinth(state.width, state.height, state.passages, state.start, state.end)
    }
}
