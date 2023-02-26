package labyrinth.internal.visualizations

import labyrinth.internal.Coordinates
import labyrinth.internal.Utils.coordinatesOf

class CharacterDisplay private constructor(val width: Int,
                                           val height: Int,
                                           private var content: Map<Coordinates, Char>) {

    companion object {
        operator fun invoke(width: Int, height: Int): CharacterDisplay {
            return CharacterDisplay(width, height, mapOf()).clear()
        }
    }

    fun clear(): CharacterDisplay {
        content = coordinatesOf(width, height).associateBy({ it }, { ' ' })
        return this
    }

    fun draw(x: Int, y: Int, item: String): CharacterDisplay {
        content = content + itemToMap(x, y, item)
        return this
    }

    fun render(): String {
        val charSeq = coordinatesOf(width, height)
            .map { content.get(it) }

        return charSeq
            .windowed(width, width)
            .map { it.joinToString("") }
            .joinToString("\n")
    }

    private fun itemToMap(x: Int, y: Int, item: String): Map<Coordinates, Char> {
        val itemChars = item
            .split("\n")
            .map { it.toCharArray() }

        val height = itemChars.size
        val width = itemChars[0].size

        val itemAsMap = (0 until height).flatMap { dy ->
            (0 until width).map { dx ->
                Pair(Coordinates(x + dx, y + dy), itemChars[dy][dx])
            }
        }.associateBy({ it.first }, { it.second })

        return itemAsMap.filterKeys { isWithinBounds(it) }
    }

    private fun isWithinBounds(coordinates: Coordinates): Boolean {
        return coordinates.x in 0 until width &&
                coordinates.y in 0 until height
    }
}
