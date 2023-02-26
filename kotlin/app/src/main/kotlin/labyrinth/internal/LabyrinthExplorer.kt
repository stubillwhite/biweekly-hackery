package labyrinth.internal

import labyrinth.internal.visualizations.AlgorithmVisualizer

interface LabyrinthExplorer {
    fun findPathToExit(labyrinth: Labyrinth, algorithmVisualizer: AlgorithmVisualizer): List<Room>
}