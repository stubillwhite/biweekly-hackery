package labyrinth

import labyrinth.internal.explorers.FollowRightHandWallExplorer
import labyrinth.internal.generators.RecursiveBacktrackingLabyrinthGenerator
import labyrinth.internal.visualizations.AlgorithmVisualizer

object LabyrinthsAreAwesome {

    @JvmStatic
    fun main(args: Array<String>) {
        val visualizer = AlgorithmVisualizer()
        val labyrinth = RecursiveBacktrackingLabyrinthGenerator().generateLabyrinth(10, 10, visualizer)
        val explorer = FollowRightHandWallExplorer()
        explorer.findPathToExit(labyrinth, visualizer)
    }
}
