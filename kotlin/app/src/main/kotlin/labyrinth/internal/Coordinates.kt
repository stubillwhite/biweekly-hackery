package labyrinth.internal

data class Coordinates(val x: Int, val y: Int) {

    fun plus(other: Coordinates): Coordinates {
        return Coordinates(x + other.x, y + other.y)
    }
}