package labyrinth.internal

object Utils {
    fun coordinatesOf(width: Int, height: Int): List<Coordinates> {
        return (0 until height).flatMap { y -> (0 until width).map { x -> Coordinates(x, y) } }
    }
}