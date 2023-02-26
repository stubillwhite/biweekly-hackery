package labyrinth.internal.visualizations

import assertk.assert
import assertk.assertions.isEqualTo
import labyrinth.internal.Coordinates
import labyrinth.internal.Labyrinth
import labyrinth.internal.Passage
import labyrinth.internal.Room
import java.io.ByteArrayOutputStream
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.PrintStream
import kotlin.test.Test

class AlgorithmVisualizerTest {

    @Test
    fun renderGivenItemsDrawnThenClipsToWidthAndHeight() {
        // Given
        //  a b
        //  c d
        val a = Room(Coordinates(0, 0))
        val b = Room(Coordinates(1, 0))
        val c = Room(Coordinates(0, 1))
        val d = Room(Coordinates(1, 1))
        val passages = setOf(Passage(a, c), Passage(c, d), Passage(d, b))
        val labyrinth = Labyrinth(2, 2, passages, a, b)
        val path = listOf(c)

        // When
        val actual = captureStdout { AlgorithmVisualizer().visualize(labyrinth, path) }

        // Then
        val expectedCursorEscapeCode = "\u001B[6A"
        val expected = """
            |${expectedCursorEscapeCode}
            |+---+---+
            || S | E |
            |+   +   +
            || .     |
            |+---+---+
            |""".trimMargin()

        assert(actual).isEqualTo(expected)
    }

    private fun captureStdout(f: () -> Unit): String {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))
        f()
        System.setOut(PrintStream(FileOutputStream(FileDescriptor.out)))
        return outputStream.toString()
    }
}
