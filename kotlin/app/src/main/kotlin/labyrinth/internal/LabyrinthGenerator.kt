package labyrinth.internal

import labyrinth.internal.visualizations.AlgorithmVisualizer

interface LabyrinthGenerator {
    fun generateLabyrinth(width: Int, height: Int, algorithmVisualizer: AlgorithmVisualizer): Labyrinth
}