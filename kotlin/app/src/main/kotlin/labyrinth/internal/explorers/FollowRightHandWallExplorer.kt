package labyrinth.internal.explorers

import labyrinth.internal.Coordinates
import labyrinth.internal.Labyrinth
import labyrinth.internal.LabyrinthExplorer
import labyrinth.internal.Room
import labyrinth.internal.visualizations.AlgorithmVisualizer

class FollowRightHandWallExplorer : LabyrinthExplorer {

    override fun findPathToExit(labyrinth: Labyrinth, visualizer: AlgorithmVisualizer): List<Room> {
        val initialState = State(labyrinth, labyrinth.start, Orientation.North, listOf(labyrinth.start), visualizer, false)

        val finalState = generateSequence(initialState) { nextState(it) }
            .dropWhile { !it.isComplete }
            .first()

        return finalState.path;
    }

    private fun visualizeState(state: State) {
        state.visualizer.visualize(state.labyrinth, state.orientation, state.currentRoom.coordinates)
    }

    enum class Orientation {
        North,
        East,
        South,
        West
    }

    data class State(
        val labyrinth: Labyrinth,
        val currentRoom: Room,
        val orientation: Orientation,
        val path: List<Room>,
        val visualizer: AlgorithmVisualizer,
        val isComplete: Boolean
    ) {
        fun rotateRight(): State {
            val newOrientation = when (orientation) {
                Orientation.North -> Orientation.East
                Orientation.East -> Orientation.South
                Orientation.South -> Orientation.West
                Orientation.West -> Orientation.North
            }
            return copy(orientation = newOrientation)
        }

        fun rotateLeft(): State {
            return rotateRight().rotateRight().rotateRight()
        }

        fun moveForward(): State {
            val delta = when (orientation) {
                Orientation.North -> Coordinates(0, -1)
                Orientation.East -> Coordinates(1, 0)
                Orientation.South -> Coordinates(0, 1)
                Orientation.West -> Coordinates(-1, 0)
            }

            val newLocation = Room(currentRoom.coordinates.plus(delta))
            val newPath = path + newLocation

            return copy(currentRoom = newLocation, path = newPath)
        }
    }

    fun nextState(state: State): State {
        val nextState = if (state.currentRoom == state.labyrinth.end) {
            state.copy(isComplete = true)
        } else {
            if (canMoveRight(state)) {
                state.rotateRight().moveForward()
            } else if (canMoveForward(state)) {
                state.moveForward()
            } else {
                state.rotateLeft()
            }
        }

        visualizeState(nextState)

        return nextState
    }

    private fun roomsAccessibleFromCurrentRoom(state: State): Set<Room> {
        return state.labyrinth.passages
            .map { passage ->
                val (a, b) = passage
                val room = when (state.currentRoom) {
                    a -> b
                    b -> a
                    else -> null
                }
                room
            }
            .filterNotNull()
            .toSet()
    }

    private fun canMoveRight(state: State): Boolean {
        val newLocation = state.rotateRight().moveForward().currentRoom
        return roomsAccessibleFromCurrentRoom(state).contains(newLocation)
    }

    private fun canMoveForward(state: State): Boolean {
        val newLocation = state.moveForward().currentRoom
        return roomsAccessibleFromCurrentRoom(state).contains(newLocation)
    }
}
