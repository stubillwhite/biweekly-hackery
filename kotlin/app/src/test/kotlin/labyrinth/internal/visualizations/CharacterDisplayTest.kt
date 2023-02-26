package labyrinth.internal.visualizations

import assertk.assert
import assertk.assertions.isEqualTo
import kotlin.test.Test

class CharacterDisplayTest {

    private val display = CharacterDisplay(3, 3)

    private val content = """
        |abc
        |def
        |ghi""".trimMargin()

    @Test
    fun drawGivenItemSpansEdgeThenClipsToWidthAndHeight() {
        // Given, When
        val actual = display
            .draw(1, 1, content)
            .render()

        // Then
        val expected = """
            |   
            | ab
            | de""".trimMargin()

        assert(actual).isEqualTo(expected)
    }

    @Test
    fun drawGivenItemAlreadyDrawnThenOverwrites() {
        // Given, When
        val actual = display
            .draw(0, 0, content)
            .draw(1, 1, content)
            .render()

        // Then
        val expected = """
            |abc
            |dab
            |gde""".trimMargin()

        assert(actual).isEqualTo(expected)
    }

    @Test
    fun clearGivenItemAlreadyDrawnThenClearsDisplay() {
        // Given, When
        val actual = display
            .draw(0, 0, content)
            .clear()
            .render()

        // Then
        val expected = """
            |   
            |   
            |   """.trimMargin()

        assert(actual).isEqualTo(expected)
    }

}
