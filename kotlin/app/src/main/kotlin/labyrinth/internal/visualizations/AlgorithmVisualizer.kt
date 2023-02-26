package labyrinth.internal.visualizations

import labyrinth.internal.Coordinates
import labyrinth.internal.Labyrinth
import labyrinth.internal.Room
import labyrinth.internal.Utils.coordinatesOf
import labyrinth.internal.explorers.FollowRightHandWallExplorer

class AlgorithmVisualizer {

    fun visualize(labyrinth: Labyrinth, path: List<Room>) {
        val visualization = labyrinthVisualization(labyrinth)

        path.forEach { visualization.withRoomContent(it.coordinates, ".") }

        visualization
            .show()
    }

    fun visualize(
        labyrinth: Labyrinth,
        explorerOrientation: FollowRightHandWallExplorer.Orientation,
        explorerCoordinates: Coordinates
    ) {
        val explorerSymbol = when (explorerOrientation) {
            FollowRightHandWallExplorer.Orientation.North -> "^"
            FollowRightHandWallExplorer.Orientation.East -> ">"
            FollowRightHandWallExplorer.Orientation.South -> "v"
            FollowRightHandWallExplorer.Orientation.West -> "<"
        }

        labyrinthVisualization(labyrinth)
            .withRoomContent(explorerCoordinates, explorerSymbol)
            .show()
    }

    private fun labyrinthVisualization(labyrinth: Labyrinth): VisualizationBuilder {

        val start = labyrinth.start.coordinates
        val end = labyrinth.end.coordinates

        return VisualizationBuilder(labyrinth)
            .withRooms()
            .withPassages()
            .withRoomContent(start, "S")
            .withRoomContent(end, "E")
    }

    private class VisualizationBuilder(val labyrinth: Labyrinth) {

        private val room = """
           |+---+
           ||   |
           |+---+
           """.trimMargin()

        private val roomWidth = room.split("\n").first().length
        private val roomHeight = room.split("\n").size

        private val characterDisplay = CharacterDisplay(
            labyrinth.width * (roomWidth - 1) + 1,
            labyrinth.height * (roomHeight - 1) + 1
        )

        fun withRooms(): VisualizationBuilder {
            val offsets = coordinatesOf(labyrinth.width, labyrinth.height)
                .map { Pair(it.x * (roomWidth - 1), it.y * (roomHeight - 1)) }

            offsets.forEach { (x, y) -> characterDisplay.draw(x, y, room) }

            return this
        }

        fun withPassages(): VisualizationBuilder {
            labyrinth.passages.forEach { passage ->
                val (x1, y1) = passage.a.coordinates
                val (x2, y2) = passage.b.coordinates

                if (x1 == x2) {
                    val x = (x1 * (roomWidth - 1)) + 1
                    val y = Math.max(y1, y2) * (roomHeight - 1)
                    val blankHorizontalLine = " ".repeat(roomWidth - 2)
                    characterDisplay.draw(x, y, blankHorizontalLine)
                } else {
                    val x = Math.max(x1, x2) * (roomWidth - 1)
                    val y = (y1 * (roomHeight - 1)) + 1
                    val blankVerticalLine = List( roomHeight - 2 ) { " " }.joinToString("\n")
                    characterDisplay.draw(x, y, blankVerticalLine)
                }
            }

            return this
        }

        fun withRoomContent(
            coordinates: Coordinates,
            content: String
        ): VisualizationBuilder {
            val x = coordinates.x * (roomWidth - 1) + (roomWidth / 2)
            val y = coordinates.y * (roomHeight - 1) + (roomHeight / 2)
            characterDisplay.draw(x, y, content)
            return this
        }

        fun show() {
            println("\u001B[${characterDisplay.height + 1}A")
            println(characterDisplay.render());
            Thread.sleep(200)
        }
    }
}
